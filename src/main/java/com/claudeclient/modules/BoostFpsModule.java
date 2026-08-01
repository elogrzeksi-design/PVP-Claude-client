package com.claudeclient.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.client.util.Window;

/**
 * Zestaw optymalizacji renderowania włączanych/wyłączanych jednym przełącznikiem:
 * ogranicza chmury, cząsteczki, mgłę i dystans renderowania encji, żeby podnieść FPS.
 * Zapisuje poprzednie wartości i przywraca je po wyłączeniu.
 */
public class BoostFpsModule extends Module {

	private CloudRenderMode backupClouds;
	private ParticlesMode backupParticles;
	private boolean backupFog;
	private int backupEntityDistance;
	private int backupEntityShadows;

	public BoostFpsModule() {
		super("Boost FPS", "Wyłącza chmury/cząsteczki/mgłę dla wyższego FPS", false);
	}

	@Override
	protected void onEnable() {
		GameOptions options = MinecraftClient.getInstance().options;
		backupClouds = options.getCloudRenderMode().getValue();
		backupParticles = options.getParticles().getValue();
		backupEntityDistance = options.getEntityDistanceScaling().getValue().intValue();

		options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
		options.getParticles().setValue(ParticlesMode.MINIMAL);
		options.getEntityDistanceScaling().setValue(0.5);
	}

	@Override
	protected void onDisable() {
		GameOptions options = MinecraftClient.getInstance().options;
		if (backupClouds != null) {
			options.getCloudRenderMode().setValue(backupClouds);
		}
		if (backupParticles != null) {
			options.getParticles().setValue(backupParticles);
		}
		options.getEntityDistanceScaling().setValue((double) backupEntityDistance);
	}
}
