package com.claudeclient.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/**
 * Odłącza kamerę od ciała gracza, pozwalając swobodnie się rozglądać/latać
 * bez poruszania faktyczną postacią. Aktywowane pod F4 (patrz KeybindHandler).
 * Ciało gracza pozostaje nieruchome na serwerze - to tylko lokalna kamera kliencka.
 *
 * Ruch obsługiwany jest tu w onTick(): WSAD przesuwa pozycję kamery w kierunku
 * patrzenia (yaw/pitch), Spacja/Shift - pionowo. Rotacja (mysz) jest wstrzykiwana
 * z zewnątrz przez addMouseDelta(), wywoływane z mixina wejścia myszy.
 */
public class FreecamModule extends Module {

	private static final double MOVE_SPEED = 0.6; // bloki/tick
	private static final double MOUSE_SENSITIVITY = 0.15;

	private Vec3d freecamPos;
	private float freecamYaw;
	private float freecamPitch;

	private double lastCursorX;
	private double lastCursorY;
	private boolean cursorInitialized;

	public FreecamModule() {
		super("Freecam", "Odłącza kamerę od gracza (F4)", false);
	}

	/**
	 * Przyjmuje surową pozycję kursora (piksele okna) i przelicza ją na deltę
	 * względem poprzedniego wywołania, po czym obraca kamerę freecam.
	 * Pierwsze wywołanie po wejściu w freecam tylko zapisuje bazową pozycję,
	 * żeby uniknąć skoku kamery.
	 */
	public void updateFromRawCursor(double x, double y) {
		if (!cursorInitialized) {
			lastCursorX = x;
			lastCursorY = y;
			cursorInitialized = true;
			return;
		}
		double deltaX = x - lastCursorX;
		double deltaY = y - lastCursorY;
		lastCursorX = x;
		lastCursorY = y;
		addMouseDelta(deltaX, deltaY);
	}

	@Override
	protected void onEnable() {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null) {
			freecamPos = client.player.getEyePos();
			freecamYaw = client.player.getYaw();
			freecamPitch = client.player.getPitch();
		}
	}

	@Override
	protected void onDisable() {
		freecamPos = null;
		cursorInitialized = false;
	}

	@Override
	public void onTick() {
		if (freecamPos == null) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		// Nie poruszaj kamerą, gdy otwarty jest jakikolwiek ekran (np. panel ustawień) -
		// wtedy klawisze mają służyć nawigacji GUI, nie lotowi.
		if (client.currentScreen != null) {
			return;
		}

		GameOptions options = client.options;
		double dx = 0, dy = 0, dz = 0;

		double yawRad = Math.toRadians(freecamYaw);
		double forwardX = -Math.sin(yawRad);
		double forwardZ = Math.cos(yawRad);
		double rightX = Math.cos(yawRad);
		double rightZ = Math.sin(yawRad);

		if (options.forwardKey.isPressed()) {
			dx += forwardX;
			dz += forwardZ;
		}
		if (options.backKey.isPressed()) {
			dx -= forwardX;
			dz -= forwardZ;
		}
		if (options.leftKey.isPressed()) {
			dx -= rightX;
			dz -= rightZ;
		}
		if (options.rightKey.isPressed()) {
			dx += rightX;
			dz += rightZ;
		}
		if (options.jumpKey.isPressed()) {
			dy += 1;
		}
		if (options.sneakKey.isPressed()) {
			dy -= 1;
		}

		double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
		if (len > 0.0001) {
			double speed = MOVE_SPEED;
			// Sprint (Ctrl) przyspiesza lot kamery, tak jak zwykły sprint w grze.
			if (client.options.sprintKey.isPressed()) {
				speed *= 2.5;
			}
			freecamPos = freecamPos.add(dx / len * speed, dy / len * speed, dz / len * speed);
		}
	}

	/** Wywoływane z mixina myszy, gdy freecam jest aktywny, żeby obracać kamerę. */
	public void addMouseDelta(double deltaX, double deltaY) {
		if (freecamPos == null) {
			return;
		}
		freecamYaw = (float) (freecamYaw + deltaX * MOUSE_SENSITIVITY);
		freecamPitch = (float) Math.max(-90, Math.min(90, freecamPitch + deltaY * MOUSE_SENSITIVITY));
	}

	public Vec3d getFreecamPos() {
		return freecamPos;
	}

	public void setFreecamPos(Vec3d pos) {
		this.freecamPos = pos;
	}

	public float getFreecamYaw() {
		return freecamYaw;
	}

	public void setFreecamYaw(float yaw) {
		this.freecamYaw = yaw;
	}

	public float getFreecamPitch() {
		return freecamPitch;
	}

	public void setFreecamPitch(float pitch) {
		this.freecamPitch = pitch;
	}
}
