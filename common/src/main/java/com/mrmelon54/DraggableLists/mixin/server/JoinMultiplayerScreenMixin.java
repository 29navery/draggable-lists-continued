package com.mrmelon54.DraggableLists.mixin.server;
import com.mrmelon54.DraggableLists.DragList;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public abstract class JoinMultiplayerScreenMixin {
    @Shadow protected ServerSelectionList serverSelectionList;
    @Inject(method = "onClose", at = @At("HEAD"))
    private void draggable_lists$close(CallbackInfo ci) {
        if (serverSelectionList != null) ((DragList)serverSelectionList).draggable_lists$cancelDrag();
    }
}
