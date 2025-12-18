package com.roockydev.mactouchmc.input;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyReleaseManager {
    
    // Map of KeyCode -> Ticks remaining until release
    private static final Map<Integer, Integer> keysToRelease = new ConcurrentHashMap<>();

    public static void scheduleRelease(int outputKeyCode, int ticks) {
        keysToRelease.put(outputKeyCode, ticks);
    }

    public static void tick() {
        if (keysToRelease.isEmpty()) return;
        
        Iterator<Map.Entry<Integer, Integer>> iterator = keysToRelease.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            int key = entry.getKey();
            int ticks = entry.getValue();
            
            if (ticks <= 0) {
                // Time to release
                releaseKey(key);
                iterator.remove();
            } else {
                // Decrement
                entry.setValue(ticks - 1);
            }
        }
    }

    private static void releaseKey(int keyCode) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getWindow() == null || client.keyboard == null) return;
        
        long window = client.getWindow().getHandle();
        int scancode = GLFW.glfwGetKeyScancode(keyCode);
        
        // Inject RELEASE
        // System.out.println("[DEBUG] VirtualInputHandler: Releasing Key=" + keyCode);
        client.execute(() -> {
             if (client.keyboard != null) {
                  client.keyboard.onKey(window, keyCode, scancode, GLFW.GLFW_RELEASE, 0);
             }
        });
    }
}
