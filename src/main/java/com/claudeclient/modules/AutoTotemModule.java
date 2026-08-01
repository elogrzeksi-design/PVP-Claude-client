package com.claudeclient.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

/**
 * Automatycznie przesuwa Totem Nieśmiertelności z ekwipunku do ręki (off-hand),
 * gdy aktualnie trzymany totem zostanie zużyty. Działa wyłącznie przez standardowe
 * pakiety kliknięcia slotu - nie modyfikuje danych po stronie serwera bezpośrednio,
 * tylko symuluje to, co gracz mógłby zrobić ręcznie, szybciej.
 */
public class AutoTotemModule extends Module {

	private static final int OFFHAND_SLOT = 45; // slot offhand w ScreenHandler gracza

	public AutoTotemModule() {
		super("Auto Totem", "Automatycznie uzupełnia totem w offhandzie", true);
	}

	@Override
	public void onTick() {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null || client.interactionManager == null) {
			return;
		}

		ItemStack offhand = client.player.getOffHandStack();
		if (!offhand.isEmpty() && offhand.isOf(Items.TOTEM_OF_UNDYING)) {
			return; // totem już jest w ręce, nic nie rób
		}

		PlayerInventory inventory = client.player.getInventory();
		int totemSlot = findTotemSlot(inventory);
		if (totemSlot == -1) {
			return; // brak totemów w ekwipunku
		}

		int networkSlot = inventoryToNetworkSlot(totemSlot);
		client.interactionManager.clickSlot(
				client.player.currentScreenHandler.syncId,
				networkSlot,
				40, // off-hand swap button (hotkey 'F' odpowiednik)
				SlotActionType.SWAP,
				client.player
		);
	}

	private int findTotemSlot(PlayerInventory inventory) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
				return i;
			}
		}
		return -1;
	}

	private int inventoryToNetworkSlot(int invSlot) {
		// Sloty 0-8 to hotbar, w ScreenHandler gracza odpowiadają slotom 36-44.
		if (invSlot < 9) {
			return invSlot + 36;
		}
		return invSlot;
	}
}
