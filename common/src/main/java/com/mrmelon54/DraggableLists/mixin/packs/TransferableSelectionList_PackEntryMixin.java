package com.mrmelon54.DraggableLists.mixin.packs;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mrmelon54.DraggableLists.DragItem;
import com.mrmelon54.DraggableLists.DraggableLists;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TransferableSelectionList.PackEntry.class)
public abstract class TransferableSelectionList_PackEntryMixin extends ObjectSelectionList.Entry<TransferableSelectionList.Entry> implements DragItem<PackSelectionModel.Entry> {
    @Shadow @Final private PackSelectionModel.Entry pack;
    @Override public PackSelectionModel.Entry draggable_lists$getUnderlyingData() { return pack; }
    @Override public boolean draggable_lists$canDrag() { return !pack.isFixedPosition(); }

    @WrapWithCondition(method = "extractContent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private boolean draggable_lists$showSprite(GuiGraphicsExtractor graphics, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
        return !DraggableLists.CONFIG.disableResourcePackArrows || !sprite.getPath().contains("move_");
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void draggable_lists$disableArrows(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (!DraggableLists.CONFIG.disableResourcePackArrows) return;
        double relativeX = event.x() - getContentX(), relativeY = event.y() - getContentY();
        if (!pack.canSelect() && relativeX >= 16 && relativeX < 32 && relativeY >= 0 && relativeY < 32) cir.setReturnValue(true);
    }
}
