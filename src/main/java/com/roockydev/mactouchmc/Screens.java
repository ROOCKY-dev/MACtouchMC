package com.roockydev.mactouchmc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

public enum Screens {
    TITLE_SCREEN(TitleScreen.class),
    SELECT_WORLD(SelectWorldScreen.class),
    CREATE_NEW_WORLD(CreateWorldScreen.class),
    PLAY_MULTIPLAYER(MultiplayerScreen.class),
    OPTIONS(OptionsScreen.class),
    GAME_MENU(GameMenuScreen.class),
    INGAME(null);

    private final Class<? extends Screen> screenClass;

    Screens(Class<? extends Screen> screenClass) {
        this.screenClass = screenClass;
    }

    public static Screens getActive() {
        Screen current = MinecraftClient.getInstance().currentScreen;
        if (current == null) return INGAME;
        
        for (Screens s : values()) {
            if (s.screenClass != null && s.screenClass.isInstance(current)) {
                return s;
            }
        }
        return null; 
    }
}
