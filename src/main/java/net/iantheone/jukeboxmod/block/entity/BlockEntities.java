package net.iantheone.jukeboxmod.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.iantheone.jukeboxmod.JukeboxMod;
import net.iantheone.jukeboxmod.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntities {
    public static BlockEntityType<BigJukeboxEntity> BIG_JUKEBOX;

    public static void registerAllBlockEntities(){
        BIG_JUKEBOX = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(JukeboxMod.MOD_ID, "big_jukebox"),
                FabricBlockEntityTypeBuilder.create(BigJukeboxEntity::new,
                        Blocks.BIG_JUKEBOX).build(null));
    }
}
