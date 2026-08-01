package com.claudeclient.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

/**
 * Podświetla hitbox gracza na czerwono TYLKO gdy:
 *  1) gracz znajduje się w linii wzroku (normalny raycast klienta, ten sam co przy ataku),
 *  2) dystans mieści się w konfigurowalnym zasięgu ataku (domyślnie 3 bloki),
 *  3) nic nie blokuje trafienia (raycast naturalnie zatrzymuje się na blokach - brak ESP przez ściany).
 *
 * To NIE jest ESP - moduł nie widzi graczy poza polem widzenia ani przez przeszkody,
 * działa identycznie jak wskaźnik celu przy zwykłym ataku myszką.
 */
public class HitboxHighlightModule extends Module {

	private double attackRange = 3.0;
	private PlayerEntity currentTarget;

	public HitboxHighlightModule() {
		super("Hitbox Highlight", "Podświetla hitbox gracza w zasięgu ataku (raycast, nie przez ściany)", true);
	}

	public double getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(double range) {
		this.attackRange = Math.max(1.0, Math.min(6.0, range));
	}

	public PlayerEntity getCurrentTarget() {
		return currentTarget;
	}

	@Override
	public void onTick() {
		currentTarget = null;
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null || client.world == null) {
			return;
		}

		// crosshairTarget to wynik natywnego raycasta silnika gry:
		// respektuje bloki (nie przenika ścian) i standardowy zasięg interakcji.
		HitResult hit = client.crosshairTarget;
		if (!(hit instanceof EntityHitResult entityHit)) {
			return;
		}

		if (!(entityHit.getEntity() instanceof PlayerEntity target)) {
			return;
		}

		if (target == client.player) {
			return;
		}

		double distance = client.player.distanceTo(target);
		if (distance <= attackRange) {
			currentTarget = target;
		}
	}
}
