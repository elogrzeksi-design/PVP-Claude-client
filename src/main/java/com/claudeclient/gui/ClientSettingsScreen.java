package com.claudeclient.gui;

import com.claudeclient.modules.Module;
import com.claudeclient.modules.ModuleManager;
import com.claudeclient.util.Theme;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClientSettingsScreen extends Screen {

	private final Screen parent;
	private static final int COLUMNS = 2;
	private static final int BUTTON_WIDTH = 150;
	private static final int BUTTON_HEIGHT = 20;
	private static final int PADDING = 8;

	private final List<ButtonRect> buttons = new ArrayList<>();

	private record ButtonRect(int x, int y, int width, int height, Module module) {
		boolean contains(double mx, double my) {
			return mx >= x && mx <= x + width && my >= y && my <= y + height;
		}
	}

	public ClientSettingsScreen(Screen parent) {
		super(Text.of("Claude Client - Ustawienia"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();
		buttons.clear();

		List<Module> modules = ModuleManager.getAll().values().stream().toList();

		int startX = width / 2 - (COLUMNS * BUTTON_WIDTH + PADDING) / 2;
		int startY = 60;

		for (int i = 0; i < modules.size(); i++) {
			int col = i % COLUMNS;
			int row = i / COLUMNS;

			int x = startX + col * (BUTTON_WIDTH + PADDING);
			int y = startY + row * (BUTTON_HEIGHT + PADDING);

			buttons.add(new ButtonRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, modules.get(i)));
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		drawStarfield(context);

		context.drawCenteredTextWithShadow(
				textRenderer,
				Text.of("CLAUDE CLIENT"),
				width / 2, 20, Theme.ORANGE
		);
		context.drawCenteredTextWithShadow(
				textRenderer,
				Text.of("Ustawienia (Escape, aby zamknąć)"),
				width / 2, 34, Theme.YELLOW
		);

		for (ButtonRect btn : buttons) {
			boolean hovered = btn.contains(mouseX, mouseY);
			Module module = btn.module();

			int bg = module.isEnabled() ? Theme.withAlpha(Theme.ORANGE_DARK, 220)
					: (hovered ? Theme.BG_BUTTON_HOVER : Theme.BG_BUTTON);
			context.fill(btn.x(), btn.y(), btn.x() + btn.width(), btn.y() + btn.height(), bg);

			int borderColor = module.isEnabled() ? Theme.YELLOW : 0xFF4A4A4A;
			context.drawHorizontalLine(btn.x(), btn.x() + btn.width() - 1, btn.y(), borderColor);
			context.drawHorizontalLine(btn.x(), btn.x() + btn.width() - 1, btn.y() + btn.height() - 1, borderColor);
			context.drawVerticalLine(btn.x(), btn.y(), btn.y() + btn.height() - 1, borderColor);
			context.drawVerticalLine(btn.x() + btn.width() - 1, btn.y(), btn.y() + btn.height() - 1, borderColor);

			int textColor = module.isEnabled() ? Theme.WHITE : Theme.OFF_WHITE;
			context.drawCenteredTextWithShadow(textRenderer, module.getName(),
					btn.x() + btn.width() / 2, btn.y() + (btn.height() - 8) / 2, textColor);

			String state = module.isEnabled() ? "ON" : "OFF";
			int stateColor = module.isEnabled() ? Theme.YELLOW : Theme.DISABLED;
			context.drawTextWithShadow(textRenderer, state,
					btn.x() + btn.width() - textRenderer.getWidth(state) - 6,
					btn.y() + (btn.height() - 8) / 2, stateColor);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (ButtonRect btn : buttons) {
			if (btn.contains(mouseX, mouseY)) {
				btn.module().toggle();
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	private void drawStarfield(DrawContext context) {
		context.fill(0, 0, width, height, Theme.BG_DARK);
		java.util.Random random = new java.util.Random(42);
		for (int i = 0; i < 80; i++) {
			int sx = random.nextInt(width);
			int sy = random.nextInt(height);
			context.fill(sx, sy, sx + 1, sy + 1, 0x50FFFFFF);
		}
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public void close() {
		if (client != null) {
			client.setScreen(parent);
		}
	}
}
