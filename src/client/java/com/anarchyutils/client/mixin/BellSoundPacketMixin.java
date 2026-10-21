package com.anarchyutils.client.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class BellSoundPacketMixin {
    @Inject(method = "handleSoundEvent", at = @At("HEAD"), cancellable = true)
    private void anarchyutils$suppressVanillaBellSound(ClientboundSoundPacket packet, CallbackInfo ci) {
        String soundId = packet.getSound().value().location().getPath();
        if ("block.bell.use".equals(soundId)) ci.cancel();
    }
}
