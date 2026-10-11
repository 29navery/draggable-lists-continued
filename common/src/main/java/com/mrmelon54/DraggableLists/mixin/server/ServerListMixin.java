package com.mrmelon54.DraggableLists.mixin.server;

import com.mrmelon54.DraggableLists.duck.ServerListDuckProvider;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;

@Mixin(ServerList.class)
public class ServerListMixin implements ServerListDuckProvider {
    @Shadow @Final private List<ServerData> serverList;
    @Override public void draggable_lists$moveItem(ServerData source, ServerData target) {
        int from = serverList.indexOf(source), to = serverList.indexOf(target);
        if (from < 0 || to < 0 || from == to) return;
        serverList.remove(from);
        serverList.add(to, source);
    }
}
