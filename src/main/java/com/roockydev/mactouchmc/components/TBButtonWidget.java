package com.roockydev.mactouchmc.components;

import com.roockydev.mactouchmc.TBButton;
import com.roockydev.mactouchmc.api.TouchBarWidget;
import com.thizzer.jtouchbar.item.TouchBarItem;
import com.thizzer.jtouchbar.item.view.TouchBarButton;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

/**
 * A standard button widget wrapper for the Touch Bar.
 */
public class TBButtonWidget implements TouchBarWidget {

    private final String id;
    private final TouchBarItem item;
    private final TouchBarButton buttonView;
    private final TBButton builder; // Keep reference to builder logic if needed, or just the view

    public TBButtonWidget(String id, TBButton builder) {
        this.id = id;
        this.builder = builder;
        this.buttonView = builder.build();
        this.item = new TouchBarItem(id, buttonView);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public TouchBarItem getTouchBarItem() {
        return item;
    }

    @Override
    public void update(MinecraftClient client) {
        // Logic to update button state (enabled/disabled, text, icon) can go here
        // For now, most buttons are static or updated via their action callbacks directly
    }
    
    public TouchBarButton getView() {
        return buttonView;
    }
}
