package com.claudeclient.mixin;

import com.claudeclient.modules.FreecamModule;
import com.claudeclient.modules.ModuleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Nadpisuje pozycję i rotację kamery renderowania wartościami z FreecamModule,
 * TAIL-injectem po standardowej aktualizacji (która śledzi ciało gracza).
 * Ciało gracza w świecie gry i na serwerze pozostaje nieruszone -
 * modyfikowana jest wyłącznie lokalna kamera kliencka.
 */
@Mixin(Camera.class)
public abstract class CameraMixin {

	@Shadow
	protected abstract void setPos(Vec3d pos);

	@Shadow
	protected abstract void setRotation(float yaw, float pitch);

	@Inject(method = "update", at = @At("TAIL"))
	private void claudeclient$applyFreecam(net.minecraft.world.World area, net.minecraft.entity.Entity focusedEntity,
											boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
		FreecamModule freecam = (FreecamModule) ModuleManager.get("Freecam");
		if (freecam == null || !freecam.isEnabled() || freecam.getFreecamPos() == null) {
			return;
		}

		setPos(freecam.getFreecamPos());
		setRotation(freecam.getFreecamYaw(), freecam.getFreecamPitch());
	}
}
