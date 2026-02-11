package com.roockydev.mactouchmc.config;

import com.roockydev.mactouchmc.Logger;
import org.apache.logging.log4j.Level;

public class ConfigParser {
    public static TouchBarButtonConfig parse(String definition) {
        TouchBarButtonConfig config = new TouchBarButtonConfig();
        try {
            String[] parts = definition.split(",");
            if (parts.length >= 1) config.name = parts[0].trim();
            if (parts.length >= 2) config.icon = parts[1].trim();
            if (parts.length >= 3) config.color = parts[2].trim();
            if (parts.length >= 4) config.type = TouchBarButtonConfig.ActionType.valueOf(parts[3].trim());
            if (parts.length >= 5) config.keyId = Integer.parseInt(parts[4].trim());
        } catch (Exception e) {
            Logger.log(Level.ERROR, "Failed to parse button definition: " + definition);
        }
        return config;
    }

    public static String serialize(TouchBarButtonConfig config) {
        return String.format("%s,%s,%s,%s,%d", 
            config.name, config.icon, config.color, config.type.name(), config.keyId);
    }
}
