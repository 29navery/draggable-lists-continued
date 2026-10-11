import java.net.*;
import java.nio.file.*;
import java.util.*;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

/** Applies every required mixin to the actual target-version Minecraft bytecode. */
public final class MixinSmoke {
    public static void main(String[] args) throws Exception {
        System.setProperty("mixin.env.disableRefMap", "true");
        URL[] urls = Arrays.stream(System.getProperty("java.class.path").split(java.io.File.pathSeparator))
            .map(p -> { try { return Path.of(p).toUri().toURL(); } catch (Exception e) { throw new RuntimeException(e); } })
            .toArray(URL[]::new);
        Launch.blackboard = new HashMap<>();
        Launch.blackboard.put("TweakClasses", new ArrayList<String>());
        Launch.blackboard.put("Tweaks", new ArrayList<>());
        Launch.classLoader = new LaunchClassLoader(urls);
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.");
        Launch.classLoader.addClassLoaderExclusion("org.objectweb.asm.");
        MixinBootstrap.init();
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        Mixins.addConfiguration("draggable_lists-common.mixins.json");
        var phase = MixinEnvironment.class.getDeclaredMethod("gotoPhase", MixinEnvironment.Phase.class);
        phase.setAccessible(true);
        phase.invoke(null, MixinEnvironment.Phase.DEFAULT);
        MixinEnvironment.getCurrentEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        var transformer = (IMixinTransformer)MixinEnvironment.getCurrentEnvironment().getActiveTransformer();
        String[] targets = {
            "net.minecraft.client.gui.screens.Screen",
            "net.minecraft.client.gui.screens.packs.PackSelectionModel$EntryBase",
            "net.minecraft.client.gui.screens.packs.PackSelectionScreen",
            "net.minecraft.client.gui.screens.packs.TransferableSelectionList",
            "net.minecraft.client.gui.screens.packs.TransferableSelectionList$PackEntry",
            "net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen",
            "net.minecraft.client.gui.screens.multiplayer.ServerSelectionList",
            "net.minecraft.client.gui.screens.multiplayer.ServerSelectionList$OnlineServerEntry",
            "net.minecraft.client.multiplayer.ServerList"
        };
        for (String name : targets) {
            var in = Launch.classLoader.getResourceAsStream(name.replace('.', '/') + ".class");
            if (in == null) throw new AssertionError("Missing target " + name);
            byte[] original = in.readAllBytes();
            byte[] modified = transformer.transformClassBytes(name, name, original);
            if (Arrays.equals(original, modified)) throw new AssertionError("Mixin did not transform " + name);
            System.out.println("VERIFIED " + name);
        }
        System.out.println("All nine Minecraft target classes transformed successfully.");
    }
}
