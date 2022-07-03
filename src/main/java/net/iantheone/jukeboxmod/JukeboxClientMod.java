package net.iantheone.jukeboxmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.iantheone.jukeboxmod.block.Blocks;
import net.minecraft.client.render.RenderLayer;

public class JukeboxClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BIG_JUKEBOX, RenderLayer.getCutout());
    }
}
