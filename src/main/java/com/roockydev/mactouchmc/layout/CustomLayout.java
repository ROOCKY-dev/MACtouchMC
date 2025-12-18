package com.roockydev.mactouchmc.layout;

import com.roockydev.mactouchmc.Icons;
import com.roockydev.mactouchmc.MacTouchMC;
import com.roockydev.mactouchmc.components.TBButtonWidget;
import com.roockydev.mactouchmc.config.ModConfig;
import com.roockydev.mactouchmc.config.TouchBarButtonConfig;
import com.roockydev.mactouchmc.input.VirtualInputHandler;
import com.thizzer.jtouchbar.item.view.TouchBarButton.ButtonType;
import java.util.List;

public class CustomLayout extends BaseLayout {

    private final MacTouchMC mod;

    public CustomLayout(MacTouchMC mod) {
        super("custom");
        this.mod = mod;
        initWidgets();
    }

    private void initWidgets() {
        // Back Button
        TBButtonWidget backWidget = new TBButtonWidget("back", new com.roockydev.mactouchmc.TBButton(ButtonType.MOMENTARY_PUSH_IN).setTitle("Back"));
        backWidget.getView().setAction(view -> {
            com.roockydev.mactouchmc.layout.LayoutManager.getInstance().setLayout(mod.getInGameLayout());
        });
        addWidget(backWidget);

        List<String> defs = ModConfig.getInstance().buttonDefinitions;
        for (int i = 0; i < defs.size(); i++) {
            String def = defs.get(i);
            TouchBarButtonConfig btnConfig = com.roockydev.mactouchmc.config.ConfigParser.parse(def);
            // Determine button type
            ButtonType type = btnConfig.type == TouchBarButtonConfig.ActionType.TOGGLE ? ButtonType.ON_OFF : ButtonType.MOMENTARY_PUSH_IN;
            
            // If not found, use default
            Icons icon = Icons.TOGGLE_HUD; // Default fallback
            try {
                // Try to find icon by name if it matches enum
                if (btnConfig.icon != null) {
                     icon = Icons.valueOf(btnConfig.icon.toUpperCase());
                }
            } catch (IllegalArgumentException ignored) {}

            TBButtonWidget widget = new TBButtonWidget(
                "custom_" + i + "_" + btnConfig.name.toLowerCase().replace(" ", "_"),
                new com.roockydev.mactouchmc.TBButton(type)
                    .setTitle(btnConfig.name)
                    //.setIcon(icon) // Use text for now or implement dynamic icons later
                    .setButtonType(type)
            );
            
            final int keyId = btnConfig.keyId != -1 ? btnConfig.keyId : 0; // Fallback to 0 if not assigned yet (should be assigned on save)
            widget.getView().setAction(view -> {
                // Determine 'isDown' state for toggle buttons, or always true for press?
                // For MOMENTARY, JTouchBar might fire just once on press?
                // Usually JTouchBar fires action on click/release.
                // For simplicity, we treat it as a press-release sequence or just a press for toggle.
                
                // For Virtual Keys, we just inject the key press event.
                // If it's a TOGGLE type on TouchBar, it stays down visualy.
                // We'll pass the visual state if possible, but for now just press.
                
                // Assuming action implies "pressed"
                boolean isPressed = true; // Or derive from view state if available
                
                VirtualInputHandler.handleButtonPress(keyId, isPressed);
            });
            
            addWidget(widget);
        }
    }
    
    // Method to refresh layout when config changes
    public void refresh() {
        this.widgets.clear();
        // Create a fresh TouchBar to ensure no ID conflicts or state issues
        this.touchBar = new com.thizzer.jtouchbar.JTouchBar();
        this.touchBar.setCustomizationIdentifier("custom");
        
        // Re-init
        initWidgets();
    }
}
