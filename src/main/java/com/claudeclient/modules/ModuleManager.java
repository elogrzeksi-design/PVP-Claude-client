package com.claudeclient.modules;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Centralny rejestr modułów. GUI ustawień odczytuje tę listę,
 * żeby zbudować przełączniki, a ClaudeClient#onClientTick ją iteruje.
 */
public final class ModuleManager {

	private static final Map<String, Module> MODULES = new LinkedHashMap<>();

	private ModuleManager() {
	}

	public static void register(Module module) {
		MODULES.put(module.getName(), module);
	}

	public static Module get(String name) {
		return MODULES.get(name);
	}

	public static Map<String, Module> getAll() {
		return MODULES;
	}

	public static void tickAll() {
		for (Module module : MODULES.values()) {
			if (module.isEnabled()) {
				module.onTick();
			}
		}
	}
}
