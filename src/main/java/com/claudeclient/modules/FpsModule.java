package com.claudeclient.modules;

import net.minecraft.client.MinecraftClient;

/**
 * Wyświetla aktualną liczbę klatek na sekundę w rogu ekranu.
 * Odczytuje wartość bezpośrednio z debugowego licznika MinecraftClient.
 */
public class FpsModule extends Module {

	public FpsModule() {
		super("FPS", "Wyświetla licznik klatek na sekundę", true);
	}

	public int getCurrentFps() {
		return MinecraftClient.getInstance().getCurrentFps();
	}
}
