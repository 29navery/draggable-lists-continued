package com.mrmelon54.DraggableLists.mixin.packs;

import com.mrmelon54.DraggableLists.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import com.mrmelon54.DraggableLists.duck.AbstractPackDuckProvider;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;

@Mixin(TransferableSelectionList.class)
public abstract class TransferableSelectionListMixin extends ObjectSelectionList<TransferableSelectionList.Entry> implements DragList {

    @Unique private final DragManager<TransferableSelectionList.Entry> draggable_lists$manager = new DragManager<>(this, this::draggable_lists$move);
    public TransferableSelectionListMixin(Minecraft minecraft, int width, int height, int y, int itemHeight) { super(minecraft, width, height, y, itemHeight); }
    @Unique private void draggable_lists$move(DragItem<?> source, DragItem<?> target) {
        if (source.draggable_lists$getUnderlyingData() instanceof AbstractPackDuckProvider duck)
            duck.draggable_lists$moveTo((PackSelectionModel.Entry)target.draggable_lists$getUnderlyingData());
    }
    @Override public void draggable_lists$cancelDrag() { draggable_lists$manager.cancel(); }
    @Override public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (DraggableLists.CONFIG.resourcePackDraggingEnabled.isEnabled() && draggable_lists$manager.mouseDragged(event)) return true;
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
