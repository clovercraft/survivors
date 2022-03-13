package gg.clovercraft.survivors.playerManager;

import gg.clovercraft.survivors.util.Data;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class PlayerManager {

    private final Data DB;
    private final Logger LOGGER;

    private HashMap<String, SurvivorPlayer> survivors;

    public PlayerManager(Logger logger, Data data ) {
        LOGGER = logger;
        DB = data;
    }

    public void registerPlayer(PlayerEntity player ) {
        survivors.put( player.getUuidAsString(), loadSurvivor( player ) );
    }

    public SurvivorPlayer getRegisteredSurvivor( PlayerEntity player ) {
        return survivors.get( player.getUuidAsString() );
    }

    public void saveRegisteredSurvivor( SurvivorPlayer survivor ) {
        survivors.replace( survivor.uuid, survivor );
    }

    public boolean attackAllowed( PlayerEntity player, LivingEntity entity ) {

        // is the target not a player?
        if( ! entity.isPlayer() ) return true;

        SurvivorPlayer survivor = getRegisteredSurvivor( player );
        SurvivorPlayer target = getRegisteredSurvivor( (PlayerEntity) entity );

        boolean allowed;

        // can the attacking player initiate hostility?
        if( survivor.isHostile() ) {
            allowed = true;
            target.setDefending();
            saveRegisteredSurvivor( target );
        } else {
            allowed = survivor.isDefending();
        }

        return allowed;
    }

    public void handlePlayerDeath( ServerPlayerEntity player ) {
        LivingEntity attacker = player.getAttacker();
        if( attacker.isPlayer() ) {
            SurvivorPlayer survivor = getRegisteredSurvivor( (PlayerEntity) attacker );
            // if they're the hunter, we can cure them
            if( survivor.hunter ) {
                survivor.hunter = false;
                player.sendMessage( Text.of( "You have been cured!" ), false );
                saveRegisteredSurvivor( survivor );
            }

            // if they're the vampire, give them an additional life
            if( survivor.vampire ) {
                survivor.lives++;
                player.sendMessage( Text.of( "You have claimed an additional life." ), false );
                saveRegisteredSurvivor( survivor );
            }
        }

        SurvivorPlayer survivor = getRegisteredSurvivor( (PlayerEntity) player );
        survivor.lives--;
        if ( survivor.lives == 0 ) {
            player.changeGameMode( GameMode.SPECTATOR );
        }
        saveRegisteredSurvivor( survivor );
    }

    private SurvivorPlayer loadSurvivor( PlayerEntity player ) {
        String selectSql = "SELECT * FROM survivors WHERE uuid = '" + player.getUuidAsString() + "';";
        DB.query( selectSql );

        if( ! DB.single() ) {
            // if we didn't get one, we need to create a new DB entry.
            int lives = (int) ((Math.random() * ( 6 - 2 ) ) + 2 );
            String insertSql = "INSERT INTO survivors (uuid,name,lives) VALUES (" + player.getUuidAsString() + "," + player.getEntityName() + "," + lives + ");";
            DB.query( insertSql );
            DB.query( selectSql );
            DB.single();
        }

        SurvivorPlayer survivor = new SurvivorPlayer( player );
        try {
            survivor.id = DB.RESULT.getInt( "id" );
            survivor.uuid = DB.RESULT.getString( "uuid" );
            survivor.name = DB.RESULT.getString( "name" );
            survivor.lives = DB.RESULT.getInt( "lives" );
            survivor.hunter = DB.RESULT.getBoolean( "isHunter" );
            survivor.vampire = DB.RESULT.getBoolean( "isVampire" );
            survivor.spartan = DB.RESULT.getBoolean( "isSpartan" );
        } catch( SQLException e ) {
            LOGGER.warn( e.getMessage() );
        }
        return survivor;
    }
}
