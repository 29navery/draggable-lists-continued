package com.mrmelon54.DraggableLists.mixin;
import com.mrmelon54.DraggableLists.Cursor;
import com.mrmelon54.DraggableLists.DragManager;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MinecraftMixin {
    @Inject(method = "removed", at = @At("HEAD"))
    private void draggable_lists$removed(CallbackInfo ci) { DragManager.cancelActive(); Cursor.reset(); }
}
