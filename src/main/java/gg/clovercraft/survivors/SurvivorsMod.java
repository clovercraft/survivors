package gg.clovercraft.survivors;

import gg.clovercraft.survivors.effects.HunterStatusEffect;
import gg.clovercraft.survivors.effects.SpartanStatusEffect;
import gg.clovercraft.survivors.effects.VampireStatusEffect;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurvivorsMod implements ModInitializer {
	public static final String NAMESPACE = "ccsurvivors";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final StatusEffect HUNTER_STATUS = new HunterStatusEffect();
	public static final StatusEffect VAMPIRE_STATUS = new VampireStatusEffect();
	public static final StatusEffect SPARTAN_STATUS = new SpartanStatusEffect();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing Survivors Mod...");
		Registry.register( Registry.STATUS_EFFECT, new Identifier( NAMESPACE, "hunter" ), HUNTER_STATUS );
		Registry.register( Registry.STATUS_EFFECT, new Identifier( NAMESPACE, "vampire" ), VAMPIRE_STATUS );
		Registry.register( Registry.STATUS_EFFECT, new Identifier( NAMESPACE, "spartan" ), SPARTAN_STATUS );
		LOGGER.info( "Registered status effects..." );

	}

}
