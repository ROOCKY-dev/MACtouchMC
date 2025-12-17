package com.roockydev.mactouchmc.layout;

import com.roockydev.mactouchmc.Icons;
import com.roockydev.mactouchmc.TBButton;
import com.roockydev.mactouchmc.components.TBButtonWidget;
import com.thizzer.jtouchbar.common.ImagePosition;
import com.thizzer.jtouchbar.item.view.TouchBarButton.ButtonType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ScreenshotRecorder;

public class InGameLayout extends BaseLayout {

    private final MinecraftClient mcc = MinecraftClient.getInstance();

    public InGameLayout() {
        super("inGame");
        initWidgets();
    }

    private void initWidgets() {
        // Toggle HUD
        TBButton hudBuilder = new TBButton(ButtonType.ON_OFF)
            .setEnabled(!mcc.options.hudHidden)
            .setIcon(Icons.TOGGLE_HUD)
            .setImagePosition(ImagePosition.ONLY);
            
        TBButtonWidget hudWidget = new TBButtonWidget("toggle_hud", hudBuilder);
        hudWidget.getView().setAction(view -> {
             boolean newState = !mcc.options.hudHidden;
             mcc.options.hudHidden = newState;
             hudWidget.getView().setTitle(newState ? "disabled" : "enabled");
             hudWidget.getView().setImage(Icons.TOGGLE_HUD.getDefaultIcon(!newState));
        });
        addWidget(hudWidget);

        // Screenshot
        TBButton screenshotBuilder = new TBButton(ButtonType.MOMENTARY_PUSH_IN)
            .setTitle("Screenshot")
            .setIcon(Icons.SCREENSHOT)
            .setImagePosition(ImagePosition.ONLY);
            
        TBButtonWidget screenshotWidget = new TBButtonWidget("screenshot", screenshotBuilder);
        screenshotWidget.getView().setAction(view -> 
            ScreenshotRecorder.saveScreenshot(mcc.runDirectory, mcc.getFramebuffer(), 
                    text -> mcc.execute(() -> mcc.inGameHud.getChatHud().addMessage(text)))
        );
        addWidget(screenshotWidget);
        
        // Debug Popover Button (Logic to open Popover typically involves a PopoverItem)
        // For this architecture, we might need a specific Widget for Popovers or handle it here manually
        // Since PopoverItem wraps a layout, let's keep it simple for now and just add the item directly
        // But ideally, we create a DebugLayout and wrap it.
        // For now, let's leave the Popover structure for the next step (DebugLayout)
    }
}
