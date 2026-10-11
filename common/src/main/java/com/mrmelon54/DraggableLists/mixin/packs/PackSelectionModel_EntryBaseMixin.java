package com.mrmelon54.DraggableLists.mixin.packs;

import com.mrmelon54.DraggableLists.duck.AbstractPackDuckProvider;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;

@Mixin(PackSelectionModel.EntryBase.class)
public abstract class PackSelectionModel_EntryBaseMixin implements AbstractPackDuckProvider, PackSelectionModel.Entry {
    @Shadow protected abstract List<Pack> getSelfList();
    @Shadow protected abstract void move(int direction);

    @Override
    public void draggable_lists$moveTo(PackSelectionModel.Entry target) {
        if (isFixedPosition() || target.isFixedPosition()) return;
        List<Pack> packs = getSelfList();
        int from = -1, to = -1;
        for (int i = 0; i < packs.size(); i++) {
            if (packs.get(i).getId().equals(getId())) from = i;
            if (packs.get(i).getId().equals(target.getId())) to = i;
        }
        if (from < 0 || to < 0) return;
        // Use vanilla moves to preserve fixed pack barriers and all change callbacks.
        int step = Integer.compare(to, from);
        for (int i = from; i != to; i += step) {
            if (step > 0 ? !canMoveDown() : !canMoveUp()) break;
            move(step);
        }
    }
}
