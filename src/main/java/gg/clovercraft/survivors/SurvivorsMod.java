package gg.clovercraft.survivors;

import gg.clovercraft.survivors.item.LifeTotem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurvivorsMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String NAMESPACE = "ccsurvivors";

	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	public static final LifeTotem LIFE_TOTEM = new LifeTotem( new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	public static final ItemGroup SURVIVOR_ITEMS = FabricItemGroupBuilder.create(
			new Identifier( NAMESPACE, "items" ))
			.icon(() -> new ItemStack(Items.TOTEM_OF_UNDYING))
			.appendItems(stacks -> {
				stacks.add(new ItemStack( LIFE_TOTEM ) );
			})
			.build();

	public static final StatusEffect HUNTER_EFFECT = null;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Initializing Survivors Mod...");
		Registry.register(Registry.ITEM, new Identifier( NAMESPACE, "life_totem"), LIFE_TOTEM );
	}
}
