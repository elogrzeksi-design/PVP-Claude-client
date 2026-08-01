package com.claudeclient.mixin;

import com.claudeclient.modules.CpsModule;
import com.claudeclient.modules.FreecamModule;
import com.claudeclient.modules.ModuleManager;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Przechwytuje surowe zdarzenia myszy dla dwóch celów:
 *  1) zliczanie kliknięć do modułu CPS,
 *  2) przekazywanie delty ruchu do FreecamModule, gdy freecam jest aktywny -
 *     tak, żeby obrót kamery freecam nie obracał jednocześnie ciała gracza
 *     (co zdradzałoby freecam innym graczom obserwującym naszą postać).
 */
@Mixin(Mouse.class)
public abstract class MouseMixin {

	@Inject(method = "onMouseButton", at = @At("HEAD"))
	private void claudeclient$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		if (action != 1) { // GLFW_PRESS
			return;
		}

		CpsModule cps = (CpsModule) ModuleManager.get("CPS");
		if (cps == null || !cps.isEnabled()) {
			return;
		}

		if (button == 0) { // GLFW_MOUSE_BUTTON_LEFT
			cps.registerLeftClick();
		} else if (button == 1) { // GLFW_MOUSE_BUTTON_RIGHT
			cps.registerRightClick();
		}
	}

	@Inject(method = "onCursorPos", at = @At("HEAD"), cancellable = true)
	private void claudeclient$onCursorPos(long window, double x, double y, CallbackInfo ci) {
		FreecamModule freecam = (FreecamModule) ModuleManager.get("Freecam");
		if (freecam == null || !freecam.isEnabled() || freecam.getFreecamPos() == null) {
			return;
		}

		// Freecam sam liczy deltę względem ostatniej znanej pozycji kursora
		// i steruje własną rotacją - oryginalna metoda gry (która obróciłaby
		// ciało gracza) jest anulowana.
		freecam.updateFromRawCursor(x, y);
		ci.cancel();
	}
}
