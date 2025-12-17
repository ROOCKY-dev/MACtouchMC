package com.roockydev.mactouchmc.layout;

import com.roockydev.mactouchmc.api.TouchBarLayout;
import com.thizzer.jtouchbar.JTouchBar;
import org.lwjgl.glfw.GLFWNativeCocoa;

/**
 * Handles the currently active layout and displaying it on the Touch Bar.
 */
public class LayoutManager {

    private static LayoutManager instance;
    private TouchBarLayout currentLayout;
    private long windowHandle;

    private LayoutManager() {}

    public static LayoutManager getInstance() {
        if (instance == null) {
            instance = new LayoutManager();
        }
        return instance;
    }

    public void setWindowHandle(long handle) {
        this.windowHandle = handle;
    }

    public void setLayout(TouchBarLayout layout) {
        if (layout == null) return;
        
        // Only switch if different (or force update logic if needed)
        if (this.currentLayout != layout) {
            this.currentLayout = layout;
            show(layout.getTouchBar());
        }
    }

    private void show(JTouchBar touchBar) {
        if (windowHandle != 0) {
            touchBar.show(windowHandle);
        }
    }

    public void update() {
        if (currentLayout != null) {
            // Future compatibility: call currentLayout.update() here
        }
    }

    public TouchBarLayout getCurrentLayout() {
        return currentLayout;
    }
}
