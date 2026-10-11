package com.mrmelon54.DraggableLists.mixin.packs;
import com.mrmelon54.DraggableLists.DragList;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackSelectionScreen.class)
public abstract class PackSelectionScreenMixin {
    @Shadow private TransferableSelectionList selectedPackList;
    @Shadow private TransferableSelectionList availablePackList;
    @Inject(method = "onClose", at = @At("HEAD"))
    private void draggable_lists$close(CallbackInfo ci) {
        if (selectedPackList != null) ((DragList)selectedPackList).draggable_lists$cancelDrag();
        if (availablePackList != null) ((DragList)availablePackList).draggable_lists$cancelDrag();
    }
}
