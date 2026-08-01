package com.claudeclient;

import com.claudeclient.gui.HudRenderer;
import com.claudeclient.modules.*;
import com.claudeclient.util.KeybindHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punkt wejścia moda Claude Client (Fabric, client-side only).
 * Rejestruje wszystkie moduły, keybindy i callbacki renderowania HUD.
 */
public class ClaudeClient implements ClientModInitializer {

	public static final String MOD_ID = "claudeclient";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Inicjalizacja Claude Client...");

		registerModules();
		KeybindHandler.register();

		HudRenderCallback.EVENT.register(HudRenderer::render);
		ClientTickEvents.END_CLIENT_TICK.register(client -> ModuleManager.tickAll());

		LOGGER.info("Claude Client załadowany pomyślnie.");
	}

	private void registerModules() {
		ModuleManager.register(new FpsModule());
		ModuleManager.register(new CpsModule());
		ModuleManager.register(new KeystrokesModule());
		ModuleManager.register(new GammaModule());
		ModuleManager.register(new BoostFpsModule());
		ModuleManager.register(new CrosshairModule());
		ModuleManager.register(new HitboxHighlightModule());
		ModuleManager.register(new FreecamModule());
		ModuleManager.register(new WaypointModule());
		ModuleManager.register(new AutoTotemModule());
	}
}
