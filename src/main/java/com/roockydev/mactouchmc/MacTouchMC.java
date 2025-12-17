package com.roockydev.mactouchmc;

import com.roockydev.mactouchmc.layout.DebugLayout;
import com.roockydev.mactouchmc.layout.InGameLayout;
import com.roockydev.mactouchmc.layout.LayoutManager;
import com.roockydev.mactouchmc.layout.MenuLayout;
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
                    
                    // Initialize Layout Manager and Layouts
                    LayoutManager layoutManager = LayoutManager.getInstance();
                    
                    // Create Layouts
                    MenuLayout menuLayout = new MenuLayout();
                    InGameLayout inGameLayout = new InGameLayout();
                    DebugLayout debugLayout = new DebugLayout(this); // Pass mod instance for reload/log access
                    
                    // Wire up Popover logic for InGameLayout to point to DebugLayout
                    // This is a manual step since we need to cross-reference layouts
                    com.thizzer.jtouchbar.item.PopoverTouchBarItem popover = new com.thizzer.jtouchbar.item.PopoverTouchBarItem("popover");
                    popover.setCollapsedRepresentation(new com.roockydev.mactouchmc.TBButton(com.thizzer.jtouchbar.item.view.TouchBarButton.ButtonType.MOMENTARY_PUSH_IN).setTitle("Debug").setIcon(Icons.DEBUG_SCREEN).build());
                    popover.setCollapsedRepresentationLabel("Debug");
                    popover.setShowsCloseButton(true);
                    popover.setPopoverTouchBar(debugLayout.getTouchBar());
                    inGameLayout.getTouchBar().addItem(popover);

                    Logger.log(Level.INFO, "TouchBar content created successfully.");
                    
                    if (mcClient.getWindow() != null) {
                        // Extracting the Cocoa Window Handle (NSWindow) required for the Touch Bar API
                        windowHandle = GLFWNativeCocoa.glfwGetCocoaWindow(mcClient.getWindow().getHandle());
                        layoutManager.setWindowHandle(windowHandle);
                        
                        Logger.log(Level.INFO, "Showing Default Info TouchBar.");
                        layoutManager.setLayout(menuLayout); // Initial View

                        // Register Tick Listener to switch bars based on context
                        ClientTickEvents.END_CLIENT_TICK.register(client -> {
                            // Check if screen changed
                            if (activeScreen != Screens.getActive()) {
                                activeScreen = Screens.getActive();
                                Logger.log(Level.DEBUG, "Screen switched to: " + activeScreen);
                                
                                if (activeScreen == Screens.INGAME || activeScreen == Screens.GAME_MENU) {
                                    layoutManager.setLayout(inGameLayout);
                                } else {
                                    layoutManager.setLayout(menuLayout); // Main menu or unknown -> Info Bar
                                    // TODO: logic for debugLayout specific triggering? 
                                    // DebugLayout is typically accessed VIA the InGameLayout popover, not standalone.
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
    public void reload() {
        Logger.log(Level.INFO, "Reloading MACtouchMC assets and layouts...");
        Icons.reload();
        // Re-init layouts? For simplicity just log. 
        // Real implementation would need to rebuild layout objects or clear them.
        Logger.log(Level.INFO, "Reload complete (Layout refresh requires restart in current impl).");
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
