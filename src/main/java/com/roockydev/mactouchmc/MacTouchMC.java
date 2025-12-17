package com.roockydev.mactouchmc;

import com.thizzer.jtouchbar.JTouchBar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFWNativeCocoa;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main Entry point for MACtouchMC.
 * Handles initialization of the Touch Bar and event listeners for Minecraft.
 * 
 * @author ROOCKYdev
 * @author MaximumFX (Original Author)
 */
public class MacTouchMC implements ClientModInitializer {

    private static MinecraftClient mcClient;
    private static long windowHandle;
    private Screens activeScreen;
    private Bars touchBars;

    private static JTouchBar inGameTouchBar;
    private static JTouchBar mainTouchBar;

    public static boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");

    @Override
    public void onInitializeClient() {
        if (IS_MAC) {
            Logger.log(Level.INFO, "Initialised MACtouchMC");
            mcClient = MinecraftClient.getInstance();
            Logger.log(Level.INFO, "Waiting on window...");

            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                Helper.init(this);
                Logger.log(Level.INFO, "Loading icons...");
                Icons.init();
                Logger.log(Level.INFO, "Creating TouchBars...");
                touchBars = new Bars();
                touchBars.init(this);
                mainTouchBar = touchBars.getInfoBar();
                inGameTouchBar = touchBars.getInGameBar();
                Logger.log(Level.INFO, "TouchBar content created");
                
                if (mcClient.getWindow() != null) {
                    windowHandle = GLFWNativeCocoa.glfwGetCocoaWindow(mcClient.getWindow().getHandle());
                    Logger.log(Level.INFO, "Info TouchBar Shown");
                    show(mainTouchBar); // Show default bar initially

                    ClientTickEvents.END_CLIENT_TICK.register(client -> {
                        // Page specific bars logic
                        if (activeScreen != Screens.getActive()) {
                            activeScreen = Screens.getActive();
                            Logger.log(Level.DEBUG, activeScreen);
                            
                            if (activeScreen == Screens.INGAME || activeScreen == Screens.GAME_MENU) {
                                show(inGameTouchBar);
                            } else {
                                show(touchBars.getDebugBar()); // Show debug bar or main bar for other screens
                            }
                        }
                    });
                } else {
                    Logger.log(Level.ERROR, "Can't setup TouchBar, window is null");
                }
            }, 5, TimeUnit.SECONDS);
        } else {
            Logger.log(Level.FATAL, "Can't initialize MACtouchMC. This device is not a Mac.");
        }
    }

    void reload() {
        Logger.log(Level.INFO, "Reloading MACtouchMC...");
        Icons.reload();
        touchBars.reload();
        mainTouchBar = touchBars.getInfoBar();
        inGameTouchBar = touchBars.getInGameBar();
        
        // Refresh current view based on state
        if (activeScreen == Screens.INGAME || activeScreen == Screens.GAME_MENU) {
             show(inGameTouchBar);
        } else {
             show(touchBars.getDebugBar());
        }
        Logger.log(Level.INFO, "Reloaded MACtouchMC.");
    }

    void show(JTouchBar touchBar) {
        if (IS_MAC && windowHandle != 0) {
            touchBar.show(windowHandle);
        }
    }
    
    public void debugWarn(final String string, final Object... arr) {
        // Implementation moved to Helper or kept here? Kept here for compatibility.
        if (mcClient.inGameHud != null) {
           // Placeholder for future implementation
        }
    }
}
