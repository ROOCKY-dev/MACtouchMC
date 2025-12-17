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
 * <p><strong>Contributor Note:</strong> This class is responsible for the lifecycle of the Touch Bar.
 * It detects the OS, waits for the window to be created, and registers the Client Tick Event
 * to switch between Touch Bar layouts (e.g. In-Game vs Menu).</p>
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

    // We strictly check for "mac" to prevent crashes on Windows/Linux environments even if the jar is loaded.
    public static boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");

    @Override
    public void onInitializeClient() {
        if (IS_MAC) {
            Logger.log(Level.INFO, "Initialised MACtouchMC - Validating environment...");
            mcClient = MinecraftClient.getInstance();
            Logger.log(Level.INFO, "Waiting for Minecraft Window to be ready...");

            // We schedule initialization 5 seconds later to ensure the LWJGL window is fully created and handleable.
            // Attempts to access window handle too early will result in 0 or crashes.
            Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                try {
                    Helper.init(this);
                    Logger.log(Level.INFO, "Loading icons from assets/mactouchmc/icons...");
                    Icons.init();
                    
                    Logger.log(Level.INFO, "Creating TouchBar layouts...");
                    touchBars = new Bars();
                    touchBars.init(this);
                    
                    // Cache references to common bars
                    mainTouchBar = touchBars.getInfoBar();
                    inGameTouchBar = touchBars.getInGameBar();
                    
                    Logger.log(Level.INFO, "TouchBar content created successfully.");
                    
                    if (mcClient.getWindow() != null) {
                        // Extracting the Cocoa Window Handle (NSWindow) required for the Touch Bar API
                        windowHandle = GLFWNativeCocoa.glfwGetCocoaWindow(mcClient.getWindow().getHandle());
                        
                        Logger.log(Level.INFO, "Showing Default Info TouchBar.");
                        show(mainTouchBar); // Show default bar initially

                        // Register Tick Listener to switch bars based on context
                        ClientTickEvents.END_CLIENT_TICK.register(client -> {
                            // Check if screen changed
                            if (activeScreen != Screens.getActive()) {
                                activeScreen = Screens.getActive();
                                Logger.log(Level.DEBUG, "Screen switched to: " + activeScreen);
                                
                                if (activeScreen == Screens.INGAME || activeScreen == Screens.GAME_MENU) {
                                    show(inGameTouchBar);
                                } else {
                                    show(touchBars.getDebugBar()); // TODO: Maybe split InfoBar and DebugBar logic clearer?
                                }
                            }
                        });
                    } else {
                        Logger.log(Level.ERROR, "CRITICAL: Minecraft Window handle is null. Touch Bar cannot be attached.");
                    }
                } catch (Exception e) {
                    Logger.log(Level.ERROR, "Failed to initialize Touch Bar: " + e.getMessage());
                    e.printStackTrace();
                }
            }, 5, TimeUnit.SECONDS);
        } else {
            Logger.log(Level.FATAL, "Aborting MACtouchMC initialization: OS is not macOS.");
        }
    }

    /**
     * Reloads all resources and recreates the Touch Bars.
     * Useful for debugging or when resource packs change.
     */
    void reload() {
        Logger.log(Level.INFO, "Reloading MACtouchMC assets and layouts...");
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
        Logger.log(Level.INFO, "Reload complete.");
    }

    /**
     * displays the given JTouchBar on the official Cocoa Window.
     * @param touchBar The JTouchBar instance to display.
     */
    void show(JTouchBar touchBar) {
        if (IS_MAC && windowHandle != 0) {
            touchBar.show(windowHandle);
        }
    }
    
    /**
     * Displays a warning or debug message to the user.
     * currently logs to console, but planned to use In-Game Chat or Toast notifications.
     */
    public void debugWarn(final String string, final Object... arr) {
        // Implementation moved to Helper or kept here? Kept here for compatibility.
        if (mcClient.inGameHud != null) {
           // Placeholder for future implementation: Show Toast or Chat message
           Logger.log(Level.INFO, "[Debug] " + string); // Temporary fallback
        }
    }
}
