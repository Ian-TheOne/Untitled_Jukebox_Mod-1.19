package net.iantheone.jukeboxmod.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.iantheone.jukeboxmod.JukeboxMod;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Blocks {
    public static final Block BIG_JUKEBOX = registerBlock("big_jukebox",
            new BigJukeboxBlock(FabricBlockSettings.of(Material.WOOD).strength(6f).nonOpaque()), ItemGroup.REDSTONE);

    private static Block registerBlock(String name, Block block, ItemGroup group){
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(JukeboxMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(JukeboxMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void registerBlocks(){
        JukeboxMod.LOGGER.info("Registering Blocks for:" + JukeboxMod.MOD_ID);
    }
}
