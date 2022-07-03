package net.iantheone.jukeboxmod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

//make that when right clicked above a music disc, embeds the disc into it's nbt data and removes the disc from the player's inventory.
public class DiscStack extends Item{
    private static final int MAX_STORAGE = 15;
    private static final String DISCS_KEY = "Music Discs";

    public DiscStack(Settings settings) {
        super(settings);
    }
    //when right-clicked above a disk
    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        ItemStack otherStack = slot.getStack();
        if (clickType != ClickType.RIGHT) {
            return false;
        }

        if (otherStack.getItem() instanceof MusicDiscItem) {
            //check if the nbt list isn't longer than the max storage
            NbtList nbtList = stack.getOrCreateNbt().getList(DISCS_KEY, NbtElement.COMPOUND_TYPE);
            if (nbtList.size() < MAX_STORAGE) {
                //add the disc to the list
                int i = addToStack(stack, otherStack);
                otherStack.decrement(i);
            }
        }
        return true;
    }

    //when right-clicked with a disk
    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType != ClickType.RIGHT || !slot.canTakePartial(player)) {
            return false;
        }
        if (otherStack.getItem() instanceof MusicDiscItem) {
            //check if the nbt list isn't longer than the max storage
            NbtList nbtList = stack.getOrCreateNbt().getList(DISCS_KEY, NbtElement.COMPOUND_TYPE);
            if (nbtList.size() < MAX_STORAGE) {
                //add the disc to the list
                int i = addToStack(stack, otherStack);
                otherStack.decrement(i);
            }
        }
        return true;
    }

    //this code adds a disc to an nbt list that the disc stack holds.
    private static int addToStack(ItemStack stack, ItemStack disc){
        if (disc.isEmpty()){
            return 0;
        }
        NbtCompound nbtCompound = stack.getOrCreateNbt();

        if (!nbtCompound.contains(DISCS_KEY)) {
            nbtCompound.put(DISCS_KEY, new NbtList());
        }

        NbtList nbtList = nbtCompound.getList(DISCS_KEY, NbtElement.COMPOUND_TYPE);
        

        ItemStack itemStack2 = disc.copy();
        NbtCompound nbtCompound3 = new NbtCompound();

        itemStack2.writeNbt(nbtCompound3);
        nbtList.add(0, nbtCompound3);

        return 1;
    }

}
