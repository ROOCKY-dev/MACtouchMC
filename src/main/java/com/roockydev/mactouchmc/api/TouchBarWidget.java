package com.roockydev.mactouchmc.api;

import com.thizzer.jtouchbar.item.TouchBarItem;
import net.minecraft.client.MinecraftClient;

/**
 * Represents a dynamic element on the Touch Bar.
 */
public interface TouchBarWidget {
    
    /**
     * @return The unique identifier for this widget.
     */
    String getID();

    /**
     * @return The JTouchBar item representation.
     */
    TouchBarItem getTouchBarItem();

    /**
     * Called every client tick to update the widget's state.
     * Use this to change icons, text, or enabled state based on game context.
     * 
     * @param client The Minecraft client instance.
     */
    void update(MinecraftClient client);
}
