package gg.clovercraft.survivors;

import gg.clovercraft.survivors.playerManager.PlayerManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurvivorServerMod implements DedicatedServerModInitializer {

    public static final String NAMESPACE = "ccsurvivors";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private Data DB;
    private PlayerManager players;
    private EventManager events;

    @Override
    public void onInitializeServer() {
        LOGGER.info( "Loading CC Survivors Mod" );

        DB = new Data( LOGGER );
        players = new PlayerManager( LOGGER, DB );
        events = new EventManager( LOGGER, players );
        events.registerEvents();
    }
}
