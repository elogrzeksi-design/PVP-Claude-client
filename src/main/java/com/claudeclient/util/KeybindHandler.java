package com.claudeclient.util;

import com.claudeclient.gui.ClientSettingsScreen;
import com.claudeclient.modules.FreecamModule;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class KeybindHandler {

	private static final KeyBinding.Category CATEGORY =
			KeyBinding.Category.create(Identifier.of("claudeclient", "general"));

	private static KeyBinding openSettingsKey;
	private static KeyBinding toggleFreecamKey;

	private KeybindHandler() {
	}

	public static void register() {
		openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.claudeclient.settings",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_SHIFT,
				CATEGORY
		));

		toggleFreecamKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.claudeclient.freecam",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F4,
				CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(KeybindHandler::onTick);
	}

	private static void onTick(MinecraftClient client) {
		while (openSettingsKey.wasPressed()) {
			if (client.currentScreen == null) {
				client.setScreen(new ClientSettingsScreen(null));
			}
		}

		while (toggleFreecamKey.wasPressed()) {
			FreecamModule freecam = (FreecamModule) com.claudeclient.modules.ModuleManager.get("Freecam");
			if (freecam != null) {
				freecam.toggle();
			}
		}
	}
}
