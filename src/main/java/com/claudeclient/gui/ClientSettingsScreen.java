package com.claudeclient.gui;

import com.claudeclient.modules.Module;
import com.claudeclient.modules.ModuleManager;
import com.claudeclient.util.Theme;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderPipelines;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ClientSettingsScreen extends Screen {

	private static final Identifier TOGGLE_ON = Identifier.of("claudeclient", "textures/gui/crosshair/toggle_on.png");
	private static final Identifier TOGGLE_OFF = Identifier.of("claudeclient", "textures/gui/crosshair/toggle_off.png");
	private final Screen parent;
	private static final int COLUMNS = 3;
	private static final int CARD_WIDTH = 170;
	private static final int CARD_HEIGHT = 64;
	private static final int GAP = 10;
	private static final int TOGGLE_WIDTH = 40;
	private static final int TOGGLE_HEIGHT = 16;

	private record CardInfo(int x, int y, Module module, int toggleX, int toggleY) {
	}

	private final List<CardInfo> cards = new ArrayList<>();
	private int panelX, panelY, panelWidth, panelHeight;

	public ClientSettingsScreen(Screen parent) {
		super(Text.of("Claude Client - Ustawienia"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();
		cards.clear();

		List<Module> modules = ModuleManager.getAll().values().stream().toList();
		int rows = (int) Math.ceil(modules.size() / (double) COLUMNS);

		panelWidth = COLUMNS * CARD_WIDTH + (COLUMNS + 1) * GAP;
		panelHeight = 50 + rows * CARD_HEIGHT + (rows + 1) * GAP;
		panelX = width / 2 - panelWidth / 2;
		panelY = height / 2 - panelHeight / 2;

		for (int i = 0; i < modules.size(); i++) {
			Module module = modules.get(i);
			int col = i % COLUMNS;
			int row = i / COLUMNS;

			int x = panelX + GAP + col * (CARD_WIDTH + GAP);
			int y = panelY + 50 + GAP + row * (CARD_HEIGHT + GAP);

			int toggleX = x + CARD_WIDTH - TOGGLE_WIDTH - 10;
			int toggleY = y + CARD_HEIGHT - TOGGLE_HEIGHT - 8;

			cards.add(new CardInfo(x, y, module, toggleX, toggleY));

			addDrawableChild(ButtonWidget.builder(Text.of(""), button -> module.toggle())
					.dimensions(toggleX, toggleY, TOGGLE_WIDTH, TOGGLE_HEIGHT)
					.build());
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		context.fill(0, 0, width, height, 0x90101010);

		context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, Theme.BG_PANEL);
		drawFrame(context, panelX, panelY, panelWidth, panelHeight, Theme.ORANGE);

		context.fill(panelX, panelY, panelX + panelWidth, panelY + 34, Theme.withAlpha(Theme.ORANGE_DARK, 255));
		context.drawTextWithShadow(textRenderer, Text.of("CLAUDE CLIENT"), panelX + 12, panelY + 12, Theme.WHITE);
		String hint = "Escape, aby zamknąć";
		context.drawTextWithShadow(textRenderer, Text.of(hint),
				panelX + panelWidth - textRenderer.getWidth(hint) - 12, panelY + 12, 0xFFFFE0B0);

		for (CardInfo card : cards) {
			boolean enabled = card.module().isEnabled();
			int cardBg = enabled ? Theme.withAlpha(Theme.ORANGE_DARK, 90) : Theme.BG_BUTTON;
			context.fill(card.x(), card.y(), card.x() + CARD_WIDTH, card.y() + CARD_HEIGHT, cardBg);

			int borderColor = enabled ? Theme.YELLOW : 0xFF3A3A3A;
			drawFrame(context, card.x(), card.y(), CARD_WIDTH, CARD_HEIGHT, borderColor);

			int iconSize = 20;
			int iconColor = enabled ? Theme.YELLOW : Theme.DISABLED;
			context.fill(card.x() + 10, card.y() + 10, card.x() + 10 + iconSize, card.y() + 10 + iconSize, iconColor);

			context.drawTextWithShadow(textRenderer, card.module().getName(),
					card.x() + 10, card.y() + 10 + iconSize + 6,
					enabled ? Theme.WHITE : Theme.OFF_WHITE);
		}

		super.render(context, mouseX, mouseY, delta);

		for (CardInfo card : cards) {
			Identifier texture = card.module().isEnabled() ? TOGGLE_ON : TOGGLE_OFF;
			context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture,
					card.toggleX(), card.toggleY(), TOGGLE_WIDTH, TOGGLE_HEIGHT);
		}
	}

	private void drawFrame(DrawContext context, int x, int y, int w, int h, int color) {
		context.drawHorizontalLine(x, x + w - 1, y, color);
		context.drawHorizontalLine(x, x + w - 1, y + h - 1, color);
		context.drawVerticalLine(x, y, y + h - 1, color);
		context.drawVerticalLine(x + w - 1, y, y + h - 1, color);
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
