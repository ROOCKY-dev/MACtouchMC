package com.roockydev.mactouchmc.input;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class VirtualInputHandler {

    private static final int BASE_KEY = GLFW.GLFW_KEY_F13; // Start mapping from F13

    public static void handleButtonPress(int buttonIndex, boolean isdown) {
        if (MinecraftClient.getInstance() == null || MinecraftClient.getInstance().getWindow() == null) return;

        // Map button index to a virtual key code (F13, F14, etc.)
        int keyCode = BASE_KEY + buttonIndex;
        
        // Safety check to prevent mapping to invalid keys if too many buttons
        if (keyCode > GLFW.GLFW_KEY_LAST) return; 

        long window = MinecraftClient.getInstance().getWindow().getHandle();
        
        // Inject the key event directly into Minecraft's keyboard listener
        // usage: onKey(window, key, scancode, action, modifiers)
        // action: 1 = PRESS, 0 = RELEASE
        int action = isdown ? GLFW.GLFW_PRESS : GLFW.GLFW_RELEASE;
        
        // Inject the key
        if (MinecraftClient.getInstance().keyboard != null) {
             int scancode = GLFW.glfwGetKeyScancode(keyCode);
             System.out.println("[DEBUG] VirtualInputHandler: Injecting Key=" + keyCode + " Scancode=" + scancode + " Action=" + action);
             MinecraftClient.getInstance().keyboard.onKey(window, keyCode, scancode, action, 0);
             
             // If this was a PRESS action (from TouchBar tap), schedule a RELEASE in 2 ticks
             if (action == GLFW.GLFW_PRESS) {
                  KeyReleaseManager.scheduleRelease(keyCode, 2);
             }
        } else {
             System.err.println("Keyboard instance is null!");
        }
    }
}
