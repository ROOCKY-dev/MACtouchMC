package com.roockydev.mactouchmc.layout;

import com.roockydev.mactouchmc.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

/**
 * Manages detection of the current game context.
 * Used to switch layouts or update widget states.
 */
public class ContextManager {

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean isChatOpen() {
        return client.currentScreen instanceof ChatScreen;
    }

    public static boolean isInventoryOpen() {
        return client.currentScreen instanceof HandledScreen;
    }

    public static boolean isInGame() {
        return client.world != null && client.player != null;
    }

    public static boolean isDebugOpen() {
        return client.getDebugHud().shouldShowDebugHud(); // F3 menu
    }

    public static Screens getCurrentScreenType() {
        return Screens.getActive();
    }
}
