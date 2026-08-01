package com.claudeclient.modules;

/**
 * Bazowa klasa dla każdego modułu Claude Clienta.
 * Każdy moduł ma nazwę, opis, stan włączenia oraz opcjonalny keybind.
 */
public abstract class Module {

	private final String name;
	private final String description;
	protected boolean enabled;

	public Module(String name, String description, boolean defaultEnabled) {
		this.name = name;
		this.description = description;
		this.enabled = defaultEnabled;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			onEnable();
		} else {
			onDisable();
		}
	}

	public void toggle() {
		setEnabled(!enabled);
	}

	/** Wywoływane raz na klatkę, jeśli moduł jest włączony (rejestrowane przez ModuleManager). */
	public void onTick() {
	}

	/** Wywoływane, gdy moduł zostaje włączony. */
	protected void onEnable() {
	}

	/** Wywoływane, gdy moduł zostaje wyłączony. */
	protected void onDisable() {
	}
}
