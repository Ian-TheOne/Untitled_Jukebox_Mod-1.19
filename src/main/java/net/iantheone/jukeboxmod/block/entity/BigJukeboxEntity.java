package net.iantheone.jukeboxmod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BigJukeboxEntity extends BlockEntity{
    public BigJukeboxEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.BIG_JUKEBOX,pos, state);
    }
}
