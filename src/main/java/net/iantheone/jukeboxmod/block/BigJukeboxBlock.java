package net.iantheone.jukeboxmod.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.iantheone.jukeboxmod.block.entity.BigJukeboxEntity;

import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemPlacementContext;

import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;

import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;


public class BigJukeboxBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final EnumProperty<DoubleBlockHalf>   HALF = Properties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty                 HAS_DISCS = BooleanProperty.of("has_discs");
    public static final BooleanProperty                 POWERED = BooleanProperty.of("powered");
    public static final DirectionProperty               FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    protected static final VoxelShape TOP_FORWARD = Block.createCuboidShape(0, 0, 2, 16, 8, 14);
    protected static final VoxelShape BOTTOM_FORWARD = Block.createCuboidShape(0, 0, 2, 16, 16, 14);

    protected static final VoxelShape TOP_SIDEWAYS = Block.createCuboidShape(2, 0, 0, 14, 8, 16);
    protected static final VoxelShape BOTTOM_SIDEWAYS = Block.createCuboidShape(2, 0, 0, 14, 16, 16);


    //constructor stuff
    public BigJukeboxBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HALF, DoubleBlockHalf.LOWER)
                .with(HAS_DISCS, false)
                .with(POWERED, false));
    }

    //collisionshape
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        var facing = state.get(FACING);
        var half = state.get(HALF);
        
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            return (half == DoubleBlockHalf.LOWER) ? BOTTOM_FORWARD : TOP_FORWARD;
        }else{
            return (half == DoubleBlockHalf.LOWER) ? BOTTOM_SIDEWAYS : TOP_SIDEWAYS;
        }
    }

    //place top part
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        Block bigJukebox = net.iantheone.jukeboxmod.block.Blocks.BIG_JUKEBOX;

        if (!world.getBlockState(pos.up()).getMaterial().blocksMovement()){
            world.breakBlock(pos.up(), true);

            world.setBlockState(pos.up(), bigJukebox.getDefaultState()
                    .with(HALF, DoubleBlockHalf.UPPER));

            world.setBlockState(pos.up(), world.getBlockState(pos.up())
                    .with(FACING, world.getBlockState(pos).get(FACING)));
        }
    }

    //break if appropriate half is not above/below this block
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        var doubleBlockHalf = state.get(HALF);
        var bigJukebox = net.iantheone.jukeboxmod.block.Blocks.BIG_JUKEBOX;

        var dir = (doubleBlockHalf == DoubleBlockHalf.UPPER) ? Direction.DOWN : Direction.UP;
        var oppositeHalf = (doubleBlockHalf == DoubleBlockHalf.UPPER) ? DoubleBlockHalf.LOWER: DoubleBlockHalf.UPPER;

        if (neighborState.isOf(this) && neighborState.get(HALF) == oppositeHalf) {
            return state.with(POWERED, neighborState.get(POWERED))
                        .with(HAS_DISCS, neighborState.get(HAS_DISCS));
        }

        if (direction == dir && ((!neighborState.isOf(bigJukebox)) || neighborState.get(HALF) != oppositeHalf)) {
            return Blocks.AIR.getDefaultState();
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    //check for redstone signal
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        var dir = (state.get(HALF) == DoubleBlockHalf.UPPER) ? pos.down() : pos.up();
        boolean bl = true;
        boolean bl2 = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(dir)? true : (bl = false);

        if (!this.getDefaultState().isOf(sourceBlock) && bl != state.get(POWERED)) {
            world.setBlockState(pos, state.with(POWERED, bl), Block.NOTIFY_LISTENERS);
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx){
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite())
                                     .with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

    //blockstate stuff
    //#region
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation){
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING, HAS_DISCS, POWERED);
    }
    //#endregion

    //BLOCK ENTITY//

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BigJukeboxEntity(pos, state);
    }
}

