package net.iantheone.jukeboxmod;

import net.fabricmc.api.ModInitializer;
import net.iantheone.jukeboxmod.block.entity.BlockEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.iantheone.jukeboxmod.block.Blocks;
import net.iantheone.jukeboxmod.item.Items;

public class JukeboxMod implements ModInitializer {
	public static final String MOD_ID = "jukeboxmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Blocks.registerBlocks();
		BlockEntities.registerAllBlockEntities();

		Items.registerItems();

	}
}
