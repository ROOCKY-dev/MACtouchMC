package com.roockydev.mactouchmc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("mactouchmc.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig INSTANCE;

    public List<String> buttonDefinitions = new ArrayList<>();

    public ModConfig() {
        // Add default example
        buttonDefinitions.add("Shield In Hand,key.swapHands,shield,FF0000,PRESS");
    }

    public static ModConfig getInstance() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (INSTANCE == null) {
            INSTANCE = new ModConfig();
        }
    }

    public static void save() {
        validateIds();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void validateIds() {
        if (INSTANCE == null) return;
        List<TouchBarButtonConfig> configs = new ArrayList<>();
        java.util.Set<Integer> usedIds = new java.util.HashSet<>();

        // First pass: Parse and collect existing IDs
        for (String def : INSTANCE.buttonDefinitions) {
            TouchBarButtonConfig config = ConfigParser.parse(def);
            configs.add(config);
            if (config.keyId != -1) {
                usedIds.add(config.keyId);
            }
        }

        // Second pass: Assign IDs to new buttons
        for (TouchBarButtonConfig config : configs) {
            if (config.keyId == -1) {
                int newId = 0;
                while (usedIds.contains(newId)) {
                    newId++;
                }
                config.keyId = newId;
                usedIds.add(newId);
            }
        }

        // Write back to strings
        INSTANCE.buttonDefinitions = new ArrayList<>();
        for (TouchBarButtonConfig config : configs) {
            INSTANCE.buttonDefinitions.add(ConfigParser.serialize(config));
        }
    }
}
