package com.claudeclient.mixin;

import com.claudeclient.modules.HitboxHighlightModule;
import com.claudeclient.modules.ModuleManager;
import com.claudeclient.util.BoxOutlineRenderer;
import com.claudeclient.util.Theme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.WorldRenderContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Wstrzykuje rysowanie czerwonego obrysu wokół hitboxa gracza znajdującego się
 * w zasięgu ataku (dane dostarcza HitboxHighlightModule, który sam respektuje
 * naturalny raycast silnika - moduł nie widzi celów przez ściany).
 */
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

	@Inject(method = "render", at = @At("TAIL"))
	private void claudeclient$renderHitboxHighlight(WorldRenderContext context, CallbackInfo ci) {
		HitboxHighlightModule module = (HitboxHighlightModule) ModuleManager.get("Hitbox Highlight");
		if (module == null || !module.isEnabled()) {
			return;
		}

		PlayerEntity target = module.getCurrentTarget();
		if (target == null) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		float tickDelta = context.tickCounter().getTickProgress(true);

		// Interpolowana pozycja hitboxa (żeby obrys płynnie podążał za ruchem celu,
		// zamiast "skakać" co tick serwera).
		Box interpolatedBox = target.getBoundingBox().offset(
				target.lastRenderX + (target.getX() - target.lastRenderX) * tickDelta - target.getX(),
				target.lastRenderY + (target.getY() - target.lastRenderY) * tickDelta - target.getY(),
				target.lastRenderZ + (target.getZ() - target.lastRenderZ) * tickDelta - target.getZ()
		);

		// Przesunięcie boxa do współrzędnych lokalnych względem kamery,
		// zgodnie z tym, jak WorldRenderer pozycjonuje macierz przed renderowaniem encji.
		Vec3d cameraPos = context.camera().getPos();
		Box localBox = interpolatedBox.offset(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		MatrixStack matrices = context.matrixStack();
		matrices.push();
		BoxOutlineRenderer.drawOutline(matrices, context.consumers(), localBox, Theme.HITBOX_HIGHLIGHT);
		matrices.pop();
	}
}
