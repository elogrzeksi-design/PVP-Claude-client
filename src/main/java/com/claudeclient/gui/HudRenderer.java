package com.claudeclient.gui;

import com.claudeclient.modules.*;
import com.claudeclient.util.Theme;
import com.claudeclient.util.Waypoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;

/**
 * Rysuje wszystkie elementy HUD włączonych modułów: FPS, CPS, keystrokes,
 * listę waypointów z dystansem. Wywoływane co klatkę z HudRenderCallback.
 */
public final class HudRenderer {

	private HudRenderer() {
	}

	public static void render(DrawContext context, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null) {
			return;
		}
		TextRenderer tr = client.textRenderer;

		int y = 8;
		y = renderFps(context, tr, y);
		y = renderCps(context, tr, y);
		renderKeystrokes(context, tr);
		renderWaypoints(context, tr);
	}

	private static int renderFps(DrawContext context, TextRenderer tr, int y) {
		FpsModule module = (FpsModule) ModuleManager.get("FPS");
		if (module == null || !module.isEnabled()) {
			return y;
		}
		String text = "FPS: " + module.getCurrentFps();
		context.drawTextWithShadow(tr, text, 8, y, Theme.YELLOW);
		return y + 10;
	}

	private static int renderCps(DrawContext context, TextRenderer tr, int y) {
		CpsModule module = (CpsModule) ModuleManager.get("CPS");
		if (module == null || !module.isEnabled()) {
			return y;
		}
		String text = "CPS: " + module.getLeftCps() + " / " + module.getRightCps();
		context.drawTextWithShadow(tr, text, 8, y, Theme.ORANGE);
		return y + 10;
	}

	private static void renderKeystrokes(DrawContext context, TextRenderer tr) {
		KeystrokesModule module = (KeystrokesModule) ModuleManager.get("Keystrokes");
		if (module == null || !module.isEnabled()) {
			return;
		}

		int baseX = context.getScaledWindowWidth() - 70;
		int baseY = context.getScaledWindowHeight() - 70;
		int keySize = 18;

		drawKey(context, tr, baseX + keySize, baseY, keySize, "W", module.isForward());
		drawKey(context, tr, baseX, baseY + keySize, keySize, "A", module.isLeft());
		drawKey(context, tr, baseX + keySize, baseY + keySize, keySize, "S", module.isBack());
		drawKey(context, tr, baseX + keySize * 2, baseY + keySize, keySize, "D", module.isRight());
		drawKey(context, tr, baseX, baseY + keySize * 2, keySize * 3, "SPACE", module.isJump());
	}

	private static void drawKey(DrawContext context, TextRenderer tr, int x, int y, int size, String label, boolean pressed) {
		int bg = pressed ? Theme.withAlpha(Theme.ORANGE, 220) : Theme.withAlpha(Theme.BG_BUTTON, 200);
		context.fill(x, y, x + size - 1, y + size - 1, bg);
		context.drawBorder(x, y, size - 1, size - 1, pressed ? Theme.YELLOW : 0xFF555555);
		int textColor = pressed ? Theme.WHITE : Theme.OFF_WHITE;
		context.drawCenteredTextWithShadow(tr, label, x + (size - 1) / 2, y + (size - 1) / 2 - 4, textColor);
	}

	private static void renderWaypoints(DrawContext context, TextRenderer tr) {
		WaypointModule module = (WaypointModule) ModuleManager.get("Waypoints");
		if (module == null || !module.isEnabled()) {
			return;
		}

		int x = context.getScaledWindowWidth() - 8;
		int y = 8;
		for (Waypoint wp : module.getWaypoints()) {
			if (!wp.isVisible()) {
				continue;
			}
			double dist = module.distanceTo(wp);
			String text = wp.getName() + " - " + Math.round(dist) + "m";
			int width = tr.getWidth(text);
			context.drawTextWithShadow(tr, text, x - width, y, wp.getColor());
			y += 10;
		}
	}
}
