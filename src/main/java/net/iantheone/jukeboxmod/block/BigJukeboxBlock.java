package net.iantheone.jukeboxmod.block;

import net.minecraft.block.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;


public class BigJukeboxBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    protected static final VoxelShape TOP_FORWARD = Block.createCuboidShape(0, 0, 2, 16, 8, 14);
    protected static final VoxelShape BOTTOM_FORWARD = Block.createCuboidShape(0, 0, 2, 16, 16, 14);

    protected static final VoxelShape TOP_SIDEWAYS = Block.createCuboidShape(2, 0, 0, 14, 8, 16);
    protected static final VoxelShape BOTTOM_SIDEWAYS = Block.createCuboidShape(2, 0, 0, 14, 16, 16);


    //constructor stuff
    public BigJukeboxBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(HALF, DoubleBlockHalf.LOWER));
    }

    //collision/outline shape
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        var facing = state.get(FACING);
        var half = state.get(HALF);
        
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            return (half == DoubleBlockHalf.LOWER) ? BOTTOM_FORWARD : TOP_FORWARD;
        } else {
            return (half == DoubleBlockHalf.LOWER) ? BOTTOM_SIDEWAYS : TOP_SIDEWAYS;
        }
    }

    //place top part
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        Block bigJukebox = net.iantheone.jukeboxmod.block.Blocks.BIG_JUKEBOX;

        if (!world.getBlockState(pos.up()).getMaterial().blocksMovement()){
            world.breakBlock(pos.up(), true);
            world.setBlockState(pos.up(), bigJukebox.getDefaultState().with(HALF, DoubleBlockHalf.UPPER));
            world.setBlockState(pos.up(), world.getBlockState(pos.up()).with(FACING, world.getBlockState(pos).get(FACING)));
        }
    }

    //break if appropriate half is not above/below this block
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        var doubleBlockHalf = state.get(HALF);
        var bigJukebox = net.iantheone.jukeboxmod.block.Blocks.BIG_JUKEBOX;

        var dir = (doubleBlockHalf == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN);
        var oppositeHalf = (doubleBlockHalf == DoubleBlockHalf.LOWER ? DoubleBlockHalf.UPPER: DoubleBlockHalf.LOWER);

        if (direction == dir && (!neighborState.isOf(bigJukebox)) || neighborState.get(HALF) != oppositeHalf) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx){
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    //blockstate stuff
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
        builder.add(HALF, FACING);
    }
}

