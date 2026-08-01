package com.claudeclient.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

/**
 * Pokazuje na HUD, które klawisze ruchu (WSAD + spacja) i przyciski myszy
 * są aktualnie wciskane. Czysto wizualny moduł, nie modyfikuje inputu.
 */
public class KeystrokesModule extends Module {

	public KeystrokesModule() {
		super("Keystrokes", "Wyświetla wciskane klawisze WSAD i kliki myszy", true);
	}

	public boolean isForward() {
		return isPressed(MinecraftClient.getInstance().options.forwardKey);
	}

	public boolean isBack() {
		return isPressed(MinecraftClient.getInstance().options.backKey);
	}

	public boolean isLeft() {
		return isPressed(MinecraftClient.getInstance().options.leftKey);
	}

	public boolean isRight() {
		return isPressed(MinecraftClient.getInstance().options.rightKey);
	}

	public boolean isJump() {
		return isPressed(MinecraftClient.getInstance().options.jumpKey);
	}

	private boolean isPressed(KeyBinding binding) {
		return binding.isPressed();
	}
}
