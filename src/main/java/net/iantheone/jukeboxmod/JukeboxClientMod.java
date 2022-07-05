package net.iantheone.jukeboxmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.iantheone.jukeboxmod.block.Blocks;
import net.minecraft.client.render.RenderLayer;

public class JukeboxClientMod implements ClientModInitializer {
    private static Renderer renderer = RendererAccess.INSTANCE.getRenderer();
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), Blocks.BIG_JUKEBOX);
    }
}
