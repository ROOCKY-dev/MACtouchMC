package com.roockydev.mactouchmc.api;

import com.thizzer.jtouchbar.JTouchBar;
import net.minecraft.client.MinecraftClient;

/**
 * Represents a full Touch Bar layout (Main Menu, In-Game, Debug, etc.).
 */
public interface TouchBarLayout {
    
    /**
     * @return The JTouchBar instance for this layout.
     */
    JTouchBar getTouchBar();

    /**
     * Called every tick to update all widgets in this layout.
     * 
     * @param client The Minecraft client instance.
     */
    default void update(MinecraftClient client) {
        // Default implementation: layouts can override to manage specific widget updates
    }
}
