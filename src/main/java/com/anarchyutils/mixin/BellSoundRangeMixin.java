package com.anarchyutils.mixin;

import com.anarchyutils.BellSoundSettings;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEvent.class)
public class BellSoundRangeMixin {
    @Inject(method = "getRange(F)F", at = @At("RETURN"), cancellable = true, remap = false)
    private void anarchyutils$extendBellBroadcastRange(float volume, CallbackInfoReturnable<Float> cir) {
        SoundEvent sound = (SoundEvent) (Object) this;
        if (BellSoundSettings.isBellSound(sound.location().getPath())) {
            cir.setReturnValue(Math.max(cir.getReturnValue(), (float) BellSoundSettings.rangeBlocks));
        }
    }
}
