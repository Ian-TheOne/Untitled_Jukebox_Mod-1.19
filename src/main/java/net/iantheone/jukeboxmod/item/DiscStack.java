package net.iantheone.jukeboxmod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

//Like a bundle but specifically for music discs
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

    //when right-clicked while in hand
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        //drop all discs into player inventory
        if(dropAllDiscs(itemStack, user)){
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(itemStack, world.isClient());
        }
        return TypedActionResult.fail(itemStack);
    }



    //this adds a disc to an nbt list.
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

    //this removes all the music discs currently in the stack, and puts them into the player's inventory if it's not full
    private static boolean dropAllDiscs(ItemStack stack, PlayerEntity player) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        Boolean invFull = (player.getInventory().getEmptySlot() == -1 );

        if (!nbtCompound.contains(DISCS_KEY)) return false;

        if (player instanceof ServerPlayerEntity) {
            NbtList nbtList = nbtCompound.getList(DISCS_KEY, NbtElement.COMPOUND_TYPE);

            for (int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound2 = nbtList.getCompound(i);
                ItemStack itemStack = ItemStack.fromNbt(nbtCompound2);
                
                if(!invFull){
                    player.getInventory().insertStack(itemStack);
                }else{
                    player.dropItem(itemStack, true);
                }
            }
        }
        stack.removeSubNbt(DISCS_KEY);
        return true;
    }

}
