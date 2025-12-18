package com.roockydev.mactouchmc.mixin;

import com.roockydev.mactouchmc.config.ConfigParser;
import com.roockydev.mactouchmc.config.ModConfig;
import com.roockydev.mactouchmc.config.TouchBarButtonConfig;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.lwjgl.glfw.GLFW;

@Mixin(InputUtil.Key.class)
public abstract class KeyMixin {



    @Shadow private int code;

    @Inject(method = "getLocalizedText", at = @At("HEAD"), cancellable = true)
    private void  localizeCustomKeys(CallbackInfoReturnable<Text> cir) {
        // Range F13 (299/302?) to F24
        // GLFW_KEY_F13 = 302
        if (this.code >= GLFW.GLFW_KEY_F13 && this.code <= GLFW.GLFW_KEY_F25) { // Support reasonable range
            int keyId = this.code - GLFW.GLFW_KEY_F13;
            
            // Check ModConfig
            if (ModConfig.getInstance() != null) {
                for (String def : ModConfig.getInstance().buttonDefinitions) {
                    try {
                        TouchBarButtonConfig config = ConfigParser.parse(def);
                        if (config.keyId == keyId)  {
                            cir.setReturnValue(Text.of(config.name));
                            return;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }
}
