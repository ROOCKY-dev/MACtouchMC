package com.roockydev.mactouchmc.layout;

import com.roockydev.mactouchmc.api.TouchBarLayout;
import com.roockydev.mactouchmc.api.TouchBarWidget;
import com.thizzer.jtouchbar.JTouchBar;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseLayout implements TouchBarLayout {

    protected JTouchBar touchBar;
    protected final List<TouchBarWidget> widgets;

    public BaseLayout(String id) {
        this.touchBar = new JTouchBar();
        this.touchBar.setCustomizationIdentifier(id);
        this.widgets = new ArrayList<>();
    }

    protected void addWidget(TouchBarWidget widget) {
        this.widgets.add(widget);
        this.touchBar.addItem(widget.getTouchBarItem());
    }

    @Override
    public JTouchBar getTouchBar() {
        return touchBar;
    }

    @Override
    public void update(MinecraftClient client) {
        for (TouchBarWidget widget : widgets) {
            widget.update(client);
        }
    }
}
