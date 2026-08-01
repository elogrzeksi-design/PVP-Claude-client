package com.claudeclient.modules;

import net.minecraft.client.MinecraftClient;

/**
 * Nadpisuje wartość gamma gry (opcje wideo -> jasność) niezależną,
 * konfigurowalną wartością, aktywną tylko gdy moduł jest włączony.
 * Zakres 0.0 (domyślne) - 100.0 (pełny "night vision").
 */
public class GammaModule extends Module {

	private double gammaValue = 5.0;
	private double vanillaGammaBackup;

	public GammaModule() {
		super("Gamma", "Ustawia poziom jasności sceny (do 100)", false);
	}

	public double getGammaValue() {
		return gammaValue;
	}

	public void setGammaValue(double value) {
		this.gammaValue = Math.max(0.0, Math.min(100.0, value));
		applyIfEnabled();
	}

	@Override
	protected void onEnable() {
		vanillaGammaBackup = MinecraftClient.getInstance().options.getGamma().getValue();
		applyIfEnabled();
	}

	@Override
	protected void onDisable() {
		MinecraftClient.getInstance().options.getGamma().setValue(vanillaGammaBackup);
	}

	private void applyIfEnabled() {
		if (enabled) {
			MinecraftClient.getInstance().options.getGamma().setValue(gammaValue);
		}
	}
}
