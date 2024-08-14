package dev.xkmc.glimmeringtales.content.block.misc;

import dev.xkmc.l2modularblock.mult.ShapeUpdateBlockMethod;
import dev.xkmc.l2modularblock.mult.SurviveBlockMethod;
import dev.xkmc.l2modularblock.one.EntityInsideBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClayCarpetImpl implements ShapeBlockMethod, ShapeUpdateBlockMethod, SurviveBlockMethod, EntityInsideBlockMethod {

	protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		var below = level.getBlockState(pos.below());
		return Block.isFaceFull(below.getCollisionShape(level, pos.below()), Direction.UP);
	}

	@Override
	public BlockState updateShape(Block block, BlockState current, BlockState state, Direction dire, BlockState nState, LevelAccessor level, BlockPos pos, BlockPos nPos) {
		return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : current;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		Vec3 vec3 = new Vec3(0.05, 1f, 0.05);
		e.makeStuckInBlock(state, vec3);
	}
}
