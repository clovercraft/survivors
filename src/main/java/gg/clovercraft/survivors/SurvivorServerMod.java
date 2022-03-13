package gg.clovercraft.survivors;


import gg.clovercraft.survivors.commands.SetupCommand;
import gg.clovercraft.survivors.playerManager.EventManager;
import gg.clovercraft.survivors.playerManager.PlayerManager;
import gg.clovercraft.survivors.util.Data;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class SurvivorServerMod implements DedicatedServerModInitializer {

    public static final String NAMESPACE = "ccsurvivors";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    public static final int OP = 4;

    @Override
    public void onInitializeServer() {
        LOGGER.info( "Loading CC Survivors Mod" );
        Data DB = new Data( LOGGER );
        PlayerManager players = new PlayerManager( LOGGER, DB );
        EventManager events = new EventManager( players );
        events.registerEvents();

        CommandRegistrationCallback.EVENT.register( ( dispatcher, dedicated ) -> {
            dispatcher.register( literal("survivors")
                    .then( literal("setup").executes( new SetupCommand( players ) ))
            );
        });
    }
}
