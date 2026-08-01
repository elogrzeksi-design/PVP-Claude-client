package com.claudeclient.util;

import net.minecraft.util.math.BlockPos;

/**
 * Pojedynczy punkt nawigacyjny. Wyłącznie dane do wyświetlenia na HUD -
 * brak jakiejkolwiek logiki teleportacji.
 */
public class Waypoint {

	private String name;
	private BlockPos position;
	private int color;
	private boolean visible = true;

	public Waypoint(String name, BlockPos position, int color) {
		this.name = name;
		this.position = position;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BlockPos getPosition() {
		return position;
	}

	public void setPosition(BlockPos position) {
		this.position = position;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
