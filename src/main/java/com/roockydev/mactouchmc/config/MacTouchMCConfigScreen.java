package com.roockydev.mactouchmc.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class MacTouchMCConfigScreen {
    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("MACtouchMC Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Custom Buttons"))
                        .option(ListOption.<String>createBuilder()
                                .name(Text.of("Button Definitions"))
                                .description(OptionDescription.of(Text.of("Format: Name,Icon,Color,Type(PRESS/TOGGLE)")))
                                .binding(
                                        ModConfig.getInstance().buttonDefinitions, 
                                        () -> ModConfig.getInstance().buttonDefinitions,
                                        (newList) -> ModConfig.getInstance().buttonDefinitions = new java.util.ArrayList<>(newList)
                                )
                                .controller(opt -> StringControllerBuilder.create(opt))
                                .initial("New Button")
                                .build())
                        .build())
                .save(ModConfig::save)
                .build()
                .generateScreen(parent);
    }
}
