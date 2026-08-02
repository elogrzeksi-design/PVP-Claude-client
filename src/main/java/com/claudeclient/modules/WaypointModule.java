package com.claudeclient.modules;

import com.claudeclient.util.Theme;
import com.claudeclient.util.Waypoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Zarządza listą waypointów gracza.
 * WAŻNE: moduł celowo NIE zawiera żadnej metody teleportacji - jedyna funkcja
 * to zapisywanie pozycji oraz wyliczanie kierunku/dystansu do wyświetlenia
 * jako strzałka/etykieta na HUD. Poruszanie się do celu wymaga gry ręcznej.
 */
public class WaypointModule extends Module {

	private final List<Waypoint> waypoints = new ArrayList<>();

	public WaypointModule() {
		super("Waypoints", "Zapisuje punkty nawigacyjne (bez auto-teleportacji)", true);
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public Waypoint addWaypointAtPlayer(String name) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null) {
			return null;
		}
		BlockPos pos = client.player.getBlockPos();
		Waypoint wp = new Waypoint(name, pos, Theme.ORANGE);
		waypoints.add(wp);
		return wp;
	}

	public void removeWaypoint(Waypoint wp) {
		waypoints.remove(wp);
	}

	/** Dystans w blokach od gracza do waypointa - tylko do wyświetlenia, nic więcej. */
	public double distanceTo(Waypoint wp) {
		MinecraftClient client = MinecraftClient.getInstance();
		PlayerEntity player = client.player;
		if (player == null) {
			return -1;
		}
		BlockPos pos = wp.getPosition();
		return new net.minecraft.util.math.Vec3d(player.getX(), player.getY(), player.getZ())
				.distanceTo(net.minecraft.util.math.Vec3d.ofCenter(pos));
	}

	/** Azymut (w stopniach) od gracza do waypointa - do rysowania strzałki kierunkowej na HUD. */
	public double directionTo(Waypoint wp) {
		MinecraftClient client = MinecraftClient.getInstance();
		PlayerEntity player = client.player;
		if (player == null) {
			return 0;
		}
		BlockPos pos = wp.getPosition();
		double dx = pos.getX() - player.getX();
		double dz = pos.getZ() - player.getZ();
		return Math.toDegrees(Math.atan2(dz, dx)) - 90;
	}
}
