package com.mrmelon54.DraggableLists.mixin.server;

import com.mrmelon54.DraggableLists.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import com.mrmelon54.DraggableLists.duck.ServerListDuckProvider;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;

@Mixin(ServerSelectionList.class)
public abstract class ServerSelectionListMixin extends ObjectSelectionList<ServerSelectionList.Entry> implements DragList {
    @Shadow @Final private JoinMultiplayerScreen screen;
    @Shadow public abstract void updateOnlineServers(ServerList servers);

    @Unique private final DragManager<ServerSelectionList.Entry> draggable_lists$manager = new DragManager<>(this, this::draggable_lists$move);
    public ServerSelectionListMixin(Minecraft minecraft, int width, int height, int y, int itemHeight) { super(minecraft, width, height, y, itemHeight); }
    @Unique private void draggable_lists$move(DragItem<?> source, DragItem<?> target) {
        ServerList servers = screen.getServers();
        ((ServerListDuckProvider)servers).draggable_lists$moveItem((ServerData)source.draggable_lists$getUnderlyingData(), (ServerData)target.draggable_lists$getUnderlyingData());
        servers.save();
        updateOnlineServers(servers);
        for (ServerSelectionList.Entry entry : children()) {
            if (entry instanceof DragItem<?> item && item.draggable_lists$getUnderlyingData() == source.draggable_lists$getUnderlyingData()) {
                setSelected(entry);
                break;
            }
        }
    }
    @Override public void draggable_lists$cancelDrag() { draggable_lists$manager.cancel(); }
    @Override public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (DraggableLists.CONFIG.serverDraggingEnabled.isEnabled() && draggable_lists$manager.mouseDragged(event)) return true;
        return super.mouseDragged(event, dx, dy);
    }
    @Override public boolean mouseReleased(MouseButtonEvent event) {
        boolean handled = draggable_lists$manager.mouseReleased(event);
        return super.mouseReleased(event) || handled;
    }
    @Override public boolean mouseScrolled(double x, double y, double dx, double dy) {
        return draggable_lists$manager.isDragging() || super.mouseScrolled(x, y, dx, dy);
    }
    @Override protected void extractListItems(GuiGraphicsExtractor graphics, int x, int y, float delta) {
        draggable_lists$manager.renderListItems(graphics, x, y, delta, entry -> extractItem(graphics, x, y, delta, entry));
    }
    @Override public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int x, int y, float delta) {
        super.extractWidgetRenderState(graphics, x, y, delta);
        draggable_lists$manager.renderOverlay(graphics, x, y, delta);
    }
}
