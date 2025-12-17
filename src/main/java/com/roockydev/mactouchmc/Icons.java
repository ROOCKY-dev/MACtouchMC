package com.roockydev.mactouchmc;

import com.thizzer.jtouchbar.common.Image;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.resource.Resource;
import org.apache.logging.log4j.Level;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Enum defining all icons used in the Touch Bar.
 * Handles resource loading and scaling.
 * 
 * Part of MACtouchMC by ROOCKYdev.
 * Based on MCTouchBar by MaximumFX.
 */
public enum Icons {
    TOGGLE_HUD(true),
    SCREENSHOT,
    DEBUG_SCREEN(true),
    F3_HELP,
    F3_RELOAD_CHUNKS,
    F3_SHOW_HITBOXES,
    F3_COPY_LOCATION,
    F3_CLEAR_CHAT,
    F3_CYCLE_RENDER_DISTANCE(true),
    F3_SHOW_CHUNK_BOUNDARIES,
    F3_ADVANCED_TOOLTIPS(true),
    F3_COPY_DATA,
    F3_CYCLE_GAMEMODE,
    F3_TOGGLE_AUTO_PAUSE,
    F3_RELOAD_RESOURCE_PACKS,
    F3_PAUSE_WITHOUT_PAUSE_MENU,
    DISABLE_SHADERS,
    CYCLE_CAMERA,
    STREAM_ON_OF,
    PAUSE_STREAM,
    TOGGLE_FULLSCREEN(true);

    private boolean hasDisabled;
    Icons() {
        this.hasDisabled = false;
    }
    Icons(boolean hasDisabled) {
        this.hasDisabled = hasDisabled;
    }

    public boolean hasDisabled() {
        return hasDisabled;
    }

    private static Map<Icons, Image> enabledIcons;
    private static Map<Icons, Image> disabledIcons;

    public static void init() {
        enabledIcons = new HashMap<>();
        disabledIcons = new HashMap<>();
        for (Icons icon: Icons.values()) {
            icon.getDefaultIcon();
            if (icon.hasDisabled()) icon.getDefaultIcon(false);
        }
    }
    public static void reload() {
        Logger.log(Level.INFO, "Reloading icons.");
        enabledIcons = new HashMap<>();
        disabledIcons = new HashMap<>();
        for (Icons icon: Icons.values()) {
            icon.getIcon();
            if (icon.hasDisabled()) icon.getIcon(false);
        }
    }

    public Image getDefaultIcon() {
        return getDefaultIcon(true);
    }
    public Image getDefaultIcon(boolean enabled) {
        if (enabled && enabledIcons.containsKey(this)) return enabledIcons.get(this);
        if (!enabled && disabledIcons.containsKey(this)) return disabledIcons.get(this);
        
        String assetPath = "/assets/mactouchmc/icons/" + this.name().toLowerCase() + (enabled ? "" : "_disabled") + ".png";
        InputStream stream = MacTouchMC.class.getResourceAsStream(assetPath);
        
        if (stream == null && !enabled) {
             // Fallback to enabled icon if disabled is missing
             assetPath = "/assets/mactouchmc/icons/" + this.name().toLowerCase() + ".png";
             stream = MacTouchMC.class.getResourceAsStream(assetPath);
        }

        try {
            if (stream == null) {
                Logger.log(Level.ERROR, "Stream null for " + assetPath);
                return new Image(new byte[]{});
            }
            try (InputStream s = stream) {
                Image img = getScaledImage(s);
                if (enabled) enabledIcons.put(this, img);
                else disabledIcons.put(this, img);
                return img;
            }
        } catch (IOException e) {
            Logger.log(Level.ERROR, "Unable to load image \"" + assetPath + "\".");
            return new Image(new byte[]{});
        }
    }

    public Image getIcon() {
        return getIcon(true);
    }
    public Image getIcon(boolean enabled) {
        if (enabled && enabledIcons.containsKey(this)) return enabledIcons.get(this);
        if (!enabled && disabledIcons.containsKey(this)) return disabledIcons.get(this);
        
        String fileName = "icons/" + this.name().toLowerCase() + (enabled ? "" : "_disabled") + ".png";
        
        try {
            Identifier identifier = Identifier.of("mactouchmc", fileName);
            Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(identifier);
            
            if (resource.isEmpty() && !enabled) {
                // Fallback
                identifier = Identifier.of("mactouchmc", "icons/" + this.name().toLowerCase() + ".png");
                resource = MinecraftClient.getInstance().getResourceManager().getResource(identifier);
            }

            if (resource.isPresent()) {
                try (InputStream stream = resource.get().getInputStream()) {
                    Image img = getScaledImage(stream);
                    if (enabled) enabledIcons.put(this, img);
                    else disabledIcons.put(this, img);
                    return img;
                }
            } else {
                 return getDefaultIcon(enabled);
            }
        } catch (IOException e) {
            Logger.log(Level.ERROR, "Unable to load image \"mactouchmc:" + fileName + "\", trying default image.");
            return getDefaultIcon(enabled);
        }
    }

    public Image getScaledImage(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        byte[] imageBytes = buffer.toByteArray();
        
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        if (originalImage == null) return new Image(new byte[]{});

        int imgSize = 44;
        int scaled = 32;

        if (originalImage.getWidth() >= imgSize) {
            return new Image(new ByteArrayInputStream(imageBytes)); 
        }

        BufferedImage newImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(originalImage, (imgSize - scaled)/2, (imgSize - scaled)/2, scaled, scaled, null);
        g2.dispose();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(newImage, "png", stream);
        return new Image(new ByteArrayInputStream(stream.toByteArray()));
    }
}
