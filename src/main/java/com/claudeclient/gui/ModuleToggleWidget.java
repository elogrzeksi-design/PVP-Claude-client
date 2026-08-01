package com.claudeclient.gui;

import com.claudeclient.modules.Module;
import com.claudeclient.util.Theme;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

/**
 * Przycisk-przełącznik dla pojedynczego modułu, stylizowany na wygląd
 * przycisków z głównego menu Clienta (ciemny prostokąt, obwódka,
 * pomarańczowo/żółty akcent gdy aktywny).
 */
public class ModuleToggleWidget extends ClickableWidget {

	private final Module module;

	public ModuleToggleWidget(int x, int y, int width, int height, Module module) {
		super(x, y, width, height, Text.literal(module.getName()));
		this.module = module;
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		boolean hovered = isMouseOver(mouseX, mouseY);
		int bg = module.isEnabled() ? Theme.withAlpha(Theme.ORANGE_DARK, 220)
				: (hovered ? Theme.BG_BUTTON_HOVER : Theme.BG_BUTTON);

		context.fill(getX(), getY(), getX() + width, getY() + height, bg);

		int borderColor = module.isEnabled() ? Theme.YELLOW : 0xFF4A4A4A;
		context.drawBorder(getX(), getY(), width, height, borderColor);

		TextRenderer tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
		int textColor = module.isEnabled() ? Theme.WHITE : Theme.OFF_WHITE;
		context.drawCenteredTextWithShadow(tr, module.getName(), getX() + width / 2, getY() + (height - 8) / 2, textColor);

		String state = module.isEnabled() ? "ON" : "OFF";
		int stateColor = module.isEnabled() ? Theme.YELLOW : Theme.DISABLED;
		context.drawTextWithShadow(tr, state, getX() + width - tr.getWidth(state) - 6, getY() + (height - 8) / 2, stateColor);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		module.toggle();
	}

	@Override
	protected void appendClickableNarrations(net.minecraft.client.gui.screen.narration.NarrationMessageBuilder builder) {
		builder.put(net.minecraft.client.gui.screen.narration.NarrationPart.TITLE, module.getName());
	}

	public Module getModule() {
		return module;
	}
}
