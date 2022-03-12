package gg.clovercraft.survivors;

import gg.clovercraft.survivors.playerManager.PlayerManager;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;

public class EventManager {

    private final PlayerManager PLAYERS;

    public EventManager( PlayerManager players ) {
        PLAYERS = players;
    }

    public void registerEvents() {
        registerPlayerJoinEvent();
        registerAttackEvent();
    }

    private void registerPlayerJoinEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server ) -> {
            PlayerEntity player = handler.getPlayer();
            PLAYERS.registerPlayer( player );
        });
    }

    private void registerAttackEvent() {
        AttackEntityCallback.EVENT.register((player, world, hand, pos, direction) -> {
            LivingEntity target = player.getAttacking();
            if( target == null ) return ActionResult.PASS;

            if( PLAYERS.attackAllowed( player, target ) )
                return ActionResult.PASS;
            else
                return ActionResult.FAIL;
        });
    }
}
