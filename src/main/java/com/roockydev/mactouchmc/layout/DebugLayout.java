package com.roockydev.mactouchmc.layout;

import com.roockydev.mactouchmc.Helper;
import com.roockydev.mactouchmc.Icons;
import com.roockydev.mactouchmc.MacTouchMC;
import com.roockydev.mactouchmc.TBButton;
import com.roockydev.mactouchmc.components.TBButtonWidget;
import com.thizzer.jtouchbar.common.ImagePosition;
import com.thizzer.jtouchbar.item.view.TouchBarButton.ButtonType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Locale;

public class DebugLayout extends BaseLayout {

    private final MacTouchMC mod;
    private final MinecraftClient mcc = MinecraftClient.getInstance();

    public DebugLayout(MacTouchMC mod) {
        super("debug");
        this.mod = mod;
        initWidgets();
    }

    private void initWidgets() {
        // Reload Chunks
        addWidget(createButton("f3_reload_chunks", "Reload chunks", Icons.F3_RELOAD_CHUNKS, ButtonType.MOMENTARY_PUSH_IN, view -> {
            mcc.worldRenderer.reload();
            mod.debugWarn("debug.reload_chunks.message");
        }));

        // Show Hitboxes
        boolean hitboxes = mcc.getEntityRenderDispatcher().shouldRenderHitboxes();
        // Manually set initial title/icon since builder setEnabled sets title
        TBButtonWidget showHitboxes = createWidget("f3_show_hitboxes", hitboxes ? "enabled" : "disabled", Icons.F3_SHOW_HITBOXES, ButtonType.ON_OFF);
        // showHitboxes.getView().setEnabled(hitboxes); // Not available on View
        showHitboxes.getView().setImage(Icons.F3_SHOW_HITBOXES.getDefaultIcon(hitboxes));
        
        showHitboxes.getView().setAction(view -> {
            boolean current = mcc.getEntityRenderDispatcher().shouldRenderHitboxes();
            mcc.getEntityRenderDispatcher().setRenderHitboxes(!current);
            boolean newState = mcc.getEntityRenderDispatcher().shouldRenderHitboxes();
            showHitboxes.getView().setTitle(newState ? "enabled" : "disabled");
            showHitboxes.getView().setImage(Icons.F3_SHOW_HITBOXES.getDefaultIcon(newState)); // Fix: use newState, not false
            mod.debugWarn(newState ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
        });
        addWidget(showHitboxes);
        // Show Chunk Boundaries
        TBButtonWidget chunkBoundaries = createWidget("f3_show_chunk_boundaries", "disabled", Icons.F3_SHOW_CHUNK_BOUNDARIES, ButtonType.ON_OFF);
        // chunkBoundaries.getView().setEnabled(false); // remove
        chunkBoundaries.getView().setAction(view -> {
            boolean show = mcc.debugRenderer.toggleShowChunkBorder();
            chunkBoundaries.getView().setTitle(show ? "enabled" : "disabled");
            chunkBoundaries.getView().setImage(Icons.F3_SHOW_CHUNK_BOUNDARIES.getDefaultIcon(show));
            mod.debugWarn(show ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
        });
        addWidget(chunkBoundaries);

        // Copy Data
        addWidget(createButton("f3_copy_data", "Copy data", Icons.F3_COPY_DATA, ButtonType.MOMENTARY_PUSH_IN, view -> {
             if (mcc.player != null && !mcc.player.hasReducedDebugInfo()) {
                Helper.copyLookAt(mcc.player.hasPermissionLevel(2), !Screen.hasShiftDown());
             }
        }));

        // Cycle Gamemode
        addWidget(createButton("f3_cycle_gamemode", "Cycle gamemode", Icons.F3_CYCLE_GAMEMODE, ButtonType.SWITCH, view -> {
             if (mcc.player != null) {
                 if (!mcc.player.hasPermissionLevel(2)) mod.debugWarn("debug.creative_spectator.error");
                 else if (mcc.player.isCreative()) mcc.player.networkHandler.sendChatCommand("gamemode spectator");
                 else if (mcc.player.isSpectator() || (!mcc.player.isCreative() && !mcc.player.isSpectator())) mcc.player.networkHandler.sendChatCommand("gamemode creative");
             }
        }));
        
        // Reload Resources
        addWidget(createButton("f3_reload_resource_packs", "Reload resources", Icons.F3_RELOAD_RESOURCE_PACKS, ButtonType.MOMENTARY_PUSH_IN, view -> {
             mod.debugWarn("debug.reload_resourcepacks.message");
             mcc.reloadResources();
             mod.reload(); // Reload Mod logic
        }));
        
        // Pause without Menu
        addWidget(createButton("f3_pause_without_pause_menu", "Pause without menu", Icons.F3_PAUSE_WITHOUT_PAUSE_MENU, ButtonType.MOMENTARY_PUSH_IN, view -> mcc.setScreen(new GameMenuScreen(true))));
    }
    
    // Helper to reduce boilerplate
    private TBButtonWidget createWidget(String id, String title, Icons icon, ButtonType type) {
         TBButton builder = new TBButton(type)
            .setTitle(title)
            .setIcon(icon)
            .setImagePosition(ImagePosition.ONLY);
         return new TBButtonWidget(id, builder);
    }
    
    private TBButtonWidget createButton(String id, String title, Icons icon, ButtonType type, com.thizzer.jtouchbar.item.view.action.TouchBarViewAction action) {
        TBButtonWidget widget = createWidget(id, title, icon, type);
        widget.getView().setAction(action);
        return widget;
    }
}
