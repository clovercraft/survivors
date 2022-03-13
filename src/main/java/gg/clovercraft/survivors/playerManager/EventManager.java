package gg.clovercraft.survivors.playerManager;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;

public class EventManager {

    private final PlayerManager PLAYERS;

    private boolean sessionStarted = false;
    private final int sessionLength = 180;
    private final int rolePickTime = 5;
    private final int sessionWarnTime = 165;
    private int sessionStartTick;

    public EventManager( PlayerManager players ) {
        PLAYERS = players;
    }

    public void registerEvents() {
        registerPlayerJoinEvent();
        registerAttackEvent();
        registerDeathEvent();
        registerTickEvent();
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

    private void registerDeathEvent() {
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            PLAYERS.handlePlayerDeath( oldPlayer );
        });
    }

    private void registerTickEvent() {
        ServerTickEvents.END_SERVER_TICK.register(( server ) -> {
            // start the timer if it should be running
            ScoreboardPlayerScore running = server.getScoreboard().getPlayerScore( "$running", server.getScoreboard().getObjective( "survivors_insession" ) );
            if( running.getScore() == 1 && sessionStarted == false ) {
                sessionStarted = true;
                sessionStartTick = server.getTicks();
            }

            if( sessionStarted ) {
                int now = ticksToMinutes( server.getTicks() );
                switch( now ) {
                    case rolePickTime:
                        pickRolesEvent( server );
                        break;
                    case sessionWarnTime:
                        sessionWarnEvent( server );
                        break;
                    case sessionLength:
                        endSessionEvent( server );
                        break;
                }
            }
        });
    }

    private int ticksToMinutes( int ticks ) {
        int diff = ticks - sessionStartTick;
        return diff / ( 20 * 60 );
    }

    private void pickRolesEvent( MinecraftServer server ) {
        // do role picking
    }

    private void sessionWarnEvent( MinecraftServer server ) {
        // do warning announcement
    }

    private void endSessionEvent( MinecraftServer server ) {
        sessionStarted = false;
        ScoreboardPlayerScore running = server.getScoreboard().getPlayerScore( "$running", server.getScoreboard().getObjective( "survivors_insession" ) );
        running.setScore( 0 );

        // handle end of session role details
        // do announcements
    }
}
