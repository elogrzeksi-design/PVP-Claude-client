package com.claudeclient.gui;

import com.claudeclient.modules.Module;
import com.claudeclient.modules.ModuleManager;
import com.claudeclient.util.Theme;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class ClientSettingsScreen extends Screen {

	private final Screen parent;
	private static final int COLUMNS = 2;
	private static final int BUTTON_WIDTH = 150;
	private static final int BUTTON_HEIGHT = 20;
	private static final int PADDING = 8;

	public ClientSettingsScreen(Screen parent) {
		super(Text.of("Claude Client - Ustawienia"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		List<Module> modules = ModuleManager.getAll().values().stream().toList();

		int startX = width / 2 - (COLUMNS * BUTTON_WIDTH + PADDING) / 2;
		int startY = 60;

		for (int i = 0; i < modules.size(); i++) {
			Module module = modules.get(i);
			int col = i % COLUMNS;
			int row = i / COLUMNS;

			int x = startX + col * (BUTTON_WIDTH + PADDING);
			int y = startY + row * (BUTTON_HEIGHT + PADDING);

			addDrawableChild(ButtonWidget.builder(buttonLabel(module), button -> {
						module.toggle();
						button.setMessage(buttonLabel(module));
					})
					.dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT)
					.build());
		}
	}

	private Text buttonLabel(Module module) {
		return Text.of(module.getName() + " [" + (module.isEnabled() ? "ON" : "OFF") + "]");
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

		super.render(context, mouseX, mouseY, delta);
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
