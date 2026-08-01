package com.claudeclient.modules;

import net.minecraft.util.Identifier;

/**
 * Podmienia domyślny celownik gry na jedną z customowych tekstur
 * dostarczonych w resources/assets/claudeclient/textures/gui/crosshair/.
 * InGameHudMixin odczytuje wybraną teksturę i podmienia renderowanie krzyżyka.
 */
public class CrosshairModule extends Module {

	public enum CrosshairStyle {
		DEFAULT("textures/gui/crosshair/default.png"),
		DOT("textures/gui/crosshair/dot.png"),
		CROSS_ORANGE("textures/gui/crosshair/cross_orange.png"),
		CIRCLE("textures/gui/crosshair/circle.png");

		private final String path;

		CrosshairStyle(String path) {
			this.path = path;
		}

		public Identifier getTexture() {
			return Identifier.of("claudeclient", path);
		}
	}

	private CrosshairStyle style = CrosshairStyle.CROSS_ORANGE;

	public CrosshairModule() {
		super("Custom Crosshair", "Podmienia celownik na customową teksturę", false);
	}

	public CrosshairStyle getStyle() {
		return style;
	}

	public void setStyle(CrosshairStyle style) {
		this.style = style;
	}

	public void cycleStyle() {
		CrosshairStyle[] values = CrosshairStyle.values();
		style = values[(style.ordinal() + 1) % values.length];
	}
}
