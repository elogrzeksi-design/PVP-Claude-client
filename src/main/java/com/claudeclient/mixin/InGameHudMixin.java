package com.claudeclient.mixin;

import com.claudeclient.modules.CrosshairModule;
import com.claudeclient.modules.ModuleManager;
import com.claudeclient.util.Theme;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
		int length = 4;
		int thickness = 1;

		context.fill(centerX - length, centerY - thickness / 2, centerX - 1, centerY + thickness / 2 + 1, Theme.YELLOW);
		context.fill(centerX + 1, centerY - thickness / 2, centerX + length, centerY + thickness / 2 + 1, Theme.YELLOW);
		context.fill(centerX - thickness / 2, centerY - length, centerX + thickness / 2 + 1, centerY - 1, Theme.YELLOW);
		context.fill(centerX - thickness / 2, centerY + 1, centerX + thickness / 2 + 1, centerY + length, Theme.YELLOW);
		context.fill(centerX - 1, centerY - 1, centerX + 2, centerY + 2, Theme.ORANGE);

		ci.cancel();
	}
}
