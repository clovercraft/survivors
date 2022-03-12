package gg.clovercraft.survivors.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class VampireStatusEffect extends StatusEffect {
    public VampireStatusEffect() {
        super(
                StatusEffectCategory.NEUTRAL,
                0x98D972
        );
    }

    @Override
    public boolean canApplyUpdateEffect( int duration, int amplifier ) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier ) {
        if ( entity instanceof PlayerEntity ) {          // only do any of this if the entity is a player
            PlayerEntity player = (PlayerEntity) entity; // get the player
            if( isExposed( player ) ) {
                player.addStatusEffect( new StatusEffectInstance( StatusEffects.SLOWNESS ) );
            }
        }
    }

    private boolean isExposed( PlayerEntity player ) {
        World playerWorld = player.getWorld();
        return playerWorld.isDay() && playerWorld.isSkyVisible( player.getBlockPos() ) && ! player.isSneaking();
    }
}
