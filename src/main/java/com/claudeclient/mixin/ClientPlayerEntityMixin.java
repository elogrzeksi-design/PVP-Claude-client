package com.claudeclient.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Punkt rozszerzeń dla logiki związanej z lokalnym graczem
 * (np. blokada ruchu ciała podczas Freecam, jeśli zostanie dodana).
 */
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
}
