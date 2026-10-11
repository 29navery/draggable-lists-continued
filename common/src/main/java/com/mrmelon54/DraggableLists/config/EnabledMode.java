package com.mrmelon54.DraggableLists.config;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;

public enum EnabledMode {
    DISABLED,
    REQUIRES_MODIFIER,
    ENABLED;

    public boolean isEnabled() {
        return switch (this) {
            case DISABLED -> false;
            case REQUIRES_MODIFIER -> (InputConstants.isKeyDown(InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(InputConstants.KEY_RSHIFT));
            case ENABLED -> true;
        };
    }
}
