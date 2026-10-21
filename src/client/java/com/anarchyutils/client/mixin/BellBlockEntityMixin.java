package com.anarchyutils.client.mixin;

import com.anarchyutils.client.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BellBlockEntity.class)
public abstract class BellBlockEntityMixin {
    @Inject(method = "triggerEvent", at = @At("HEAD"))
    private void anarchyutils$playRemoteBell(int type, int data, CallbackInfoReturnable<Boolean> cir) {
        if (type != 1) return;

        BellBlockEntity bell = (BellBlockEntity) (Object) this;
        Level level = bell.getLevel();
        if (level == null || !level.isClientSide() || Minecraft.getInstance().player == null) return;

        // The server's block-event packet is delivered to clients tracking this chunk,
        // even when its separate sound packet is outside the normal audio radius.
        level.playLocalSound(
            bell.getBlockPos(),
            SoundEvents.BELL_BLOCK,
            SoundSource.BLOCKS,
            1.0f,
            1.0f,
            false
        );
    }
}
