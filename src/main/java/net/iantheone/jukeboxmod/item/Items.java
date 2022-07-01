package net.iantheone.jukeboxmod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.iantheone.jukeboxmod.JukeboxMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {
    public static final Item DISC_STACK = registerItem("disc_stack",
            new Item(new FabricItemSettings().group(ItemGroup.MISC)));


    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(JukeboxMod.MOD_ID, name), item);
    }

    public static void registerItems(){
        JukeboxMod.LOGGER.info("Registering Items for:" + JukeboxMod.MOD_ID);
    }
}
