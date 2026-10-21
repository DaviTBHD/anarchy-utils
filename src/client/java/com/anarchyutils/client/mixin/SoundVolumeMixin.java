
package com.anarchyutils.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.anarchyutils.client.SoundManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;

@Mixin(SoundEngine.class)
public class SoundVolumeMixin {

    @Redirect(
        method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/sounds/Sound;getAttenuationDistance()I"
        ),
        remap = false
    )
    private int anarchyutils$modifyBellRange(Sound resolvedSound, SoundInstance sound) {
        if (SoundManager.isBellSound(sound.getIdentifier().getPath())) {
            // SoundEngine multiplies this distance by max(instance volume, 1).
            float volumeScale = Math.max(sound.getVolume(), 1.0f);
            return Math.max(1, Math.round(com.anarchyutils.BellSoundSettings.rangeBlocks / volumeScale));
        }
        return resolvedSound.getAttenuationDistance();
    }

    @Redirect(
        method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SoundInstance;getX()D"),
        remap = false
    )
    private double anarchyutils$playBellAtListenerX(SoundInstance sound) {
        if (SoundManager.isBellSound(sound.getIdentifier().getPath())) {
            var player = Minecraft.getInstance().player;
            if (player != null) return player.getX();
        }
        return sound.getX();
    }

    @Redirect(
        method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SoundInstance;getY()D"),
        remap = false
    )
    private double anarchyutils$playBellAtListenerY(SoundInstance sound) {
        if (SoundManager.isBellSound(sound.getIdentifier().getPath())) {
            var player = Minecraft.getInstance().player;
            if (player != null) return player.getY();
        }
        return sound.getY();
    }

    @Redirect(
        method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SoundInstance;getZ()D"),
        remap = false
    )
    private double anarchyutils$playBellAtListenerZ(SoundInstance sound) {
        if (SoundManager.isBellSound(sound.getIdentifier().getPath())) {
            var player = Minecraft.getInstance().player;
            if (player != null) return player.getZ();
        }
        return sound.getZ();
    }

    @Shadow(remap = false)
    private float calculateVolume(float volume, SoundSource source) {
        throw new AssertionError();
    }

    // Starting a sound bypasses calculateVolume(SoundInstance).
    @Redirect(
        method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sounds/SoundEngine;calculateVolume(FLnet/minecraft/sounds/SoundSource;)F"
        ),
        remap = false
    )
    private float anarchyutils$modifyInitialVolume(
            SoundEngine engine, float volume, SoundSource source, SoundInstance sound
    ) {
        return SoundManager.applyVolume(
            sound.getIdentifier().getPath(), calculateVolume(volume, source)
        );
    }

    @Inject(
        method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F",
        at = @At("RETURN"),
        cancellable = true,
        remap = false
    )
    private void anarchyutils$modifyVolume(
            SoundInstance sound,
            CallbackInfoReturnable<Float> cir 
    ) {
        String soundId = sound.getIdentifier().getPath();

        float originalVolume = cir.getReturnValue();

        float newVolume = SoundManager.applyVolume(
            soundId,
            originalVolume
        );

        cir.setReturnValue(newVolume);
    }
}
