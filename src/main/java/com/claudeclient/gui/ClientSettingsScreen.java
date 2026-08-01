package com.claudeclient.gui;

import com.claudeclient.modules.Module;
import com.claudeclient.modules.ModuleManager;
import com.claudeclient.util.Theme;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Ekran ustawień otwierany Prawym Shiftem. Stylistyka nawiązuje do menu
 * głównego widocznego na screenie: ciemne, gwiaździste tło, gradientowy
 * nagłówek pomarańczowo-żółty oraz szare przyciski z jasną obwódką.
 */
public class ClientSettingsScreen extends Screen {

	private final Screen parent;
	private static final int COLUMNS = 2;
	private static final int BUTTON_WIDTH = 150;
	private static final int BUTTON_HEIGHT = 20;
	private static final int PADDING = 8;

	public ClientSettingsScreen(Screen parent) {
		super(Text.literal("Claude Client - Ustawienia"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		List<Module> modules = ModuleManager.getAll().values().stream().toList();

		int startX = width / 2 - (COLUMNS * BUTTON_WIDTH + PADDING) / 2;
		int startY = 60;

		for (int i = 0; i < modules.size(); i++) {
			int col = i % COLUMNS;
			int row = i / COLUMNS;

			int x = startX + col * (BUTTON_WIDTH + PADDING);
			int y = startY + row * (BUTTON_HEIGHT + PADDING);

			addDrawableChild(new ModuleToggleWidget(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, modules.get(i)));
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		drawStarfield(context);

		// Gradientowy tytuł, stylizowany na logo z ekranu głównego
		context.drawCenteredTextWithShadow(
				textRenderer,
				Text.literal("CLAUDE CLIENT").formatted(net.minecraft.util.Formatting.BOLD),
				width / 2, 20, Theme.ORANGE
		);
		context.drawCenteredTextWithShadow(
				textRenderer,
				Text.literal("Ustawienia (Prawy Shift, aby zamknąć)"),
				width / 2, 34, Theme.YELLOW
		);

		super.render(context, mouseX, mouseY, delta);
	}

	private void drawStarfield(DrawContext context) {
		context.fill(0, 0, width, height, Theme.BG_DARK);
		java.util.Random random = new java.util.Random(42); // deterministyczny wzór "gwiazd"
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
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		// Prawy Shift zamyka ekran tak samo jak go otwiera (klawisz-przełącznik)
		if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT) {
			close();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void close() {
		if (client != null) {
			client.setScreen(parent);
		}
	}
}
