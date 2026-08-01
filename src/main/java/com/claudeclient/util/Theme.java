package com.claudeclient.util;

/**
 * Centralna paleta kolorów moda Claude Client.
 * Motyw: pomarańczowy / biały / żółty.
 */
public final class Theme {

	private Theme() {
	}

	// Główne akcenty
	public static final int ORANGE = 0xFFFF8C1A;
	public static final int ORANGE_DARK = 0xFFCC6600;
	public static final int YELLOW = 0xFFFFD54A;
	public static final int WHITE = 0xFFFFFFFF;
	public static final int OFF_WHITE = 0xFFE8E8E8;

	// Tła
	public static final int BG_DARK = 0xE0141414;
	public static final int BG_PANEL = 0xF01E1E1E;
	public static final int BG_BUTTON = 0xFF2A2A2A;
	public static final int BG_BUTTON_HOVER = 0xFF3A3A3A;

	// Stany
	public static final int ENABLED = ORANGE;
	public static final int DISABLED = 0xFF666666;
	public static final int HITBOX_HIGHLIGHT = 0x80FF3030; // czerwony, tylko dla modułu ataku

	// Gradient nagłówka (jak w logo na screenie)
	public static final int HEADER_GRADIENT_START = 0xFFFFA733;
	public static final int HEADER_GRADIENT_END = 0xFFFFE066;

	public static int withAlpha(int color, int alpha) {
		return (color & 0x00FFFFFF) | (alpha << 24);
	}
}
