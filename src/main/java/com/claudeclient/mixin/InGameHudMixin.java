package com.claudeclient.mixin;

import com.claudeclient.modules.CrosshairModule;
import com.claudeclient.modules.ModuleManager;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Podmienia teksturę renderowanego celownika, gdy moduł Custom Crosshair
 * jest włączony. Wanilijowy crosshair jest pomijany na rzecz wybranej
 * tekstury z CrosshairModule.
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	private void claudeclient$renderCustomCrosshair(net.minecraft.client.gui.DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter, CallbackInfo ci) {
		CrosshairModule module = (CrosshairModule) ModuleManager.get("Custom Crosshair");
		if (module == null || !module.isEnabled()) {
			return;
		}

		int centerX = context.getScaledWindowWidth() / 2;
		int centerY = context.getScaledWindowHeight() / 2;
		int size = 15;

		context.drawTexture(
				module.getStyle().getTexture(),
				centerX - size / 2, centerY - size / 2,
				0, 0, size, size, size, size
		);

		ci.cancel();
	}
}
