package hu.xannosz.airship.block;

import hu.xannosz.airship.blockentity.ModBlockEntities;
import hu.xannosz.airship.blockentity.ThrottleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class Throttle extends BaseEntityBlock {

	public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 4);

	public Throttle() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.3F).noOcclusion().sound(SoundType.METAL));
		this.registerDefaultState(this.stateDefinition.any().setValue(POSITION, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(POSITION);
	}

	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos pos,
										  @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
		if (!level.isClientSide() && isInShipDimension(level)) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof ThrottleBlockEntity throttleBlockEntity) {
				throttleBlockEntity.slower();
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}

		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Override
	@SuppressWarnings("deprecation")
	public void attack(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player) {
		if (!level.isClientSide() && isInShipDimension(level)) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof ThrottleBlockEntity throttleBlockEntity) {
				throttleBlockEntity.faster();
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new ThrottleBlockEntity(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModBlockEntities.THROTTLE_BLOCK_ENTITY.get(), ThrottleBlockEntity::tick);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
		switch (blockState.getValue(POSITION)) {
			case 0 -> {
				return Shapes.or(Block.box(0, 0, 0, 16, 1, 16),
						Block.box(6, 1, 13, 15, 7, 15));
			}
			case 1 -> {
				return Shapes.or(Block.box(0, 0, 0, 16, 1, 16),
						Block.box(6, 1, 10, 15, 7, 12));
			}
			case 2 -> {
				return Shapes.or(Block.box(0, 0, 0, 16, 1, 16),
						Block.box(6, 1, 7, 15, 7, 9));
			}
			case 3 -> {
				return Shapes.or(Block.box(0, 0, 0, 16, 1, 16),
						Block.box(6, 1, 4, 15, 7, 6));
			}
			case 4 -> {
				return Shapes.or(Block.box(0, 0, 0, 16, 1, 16),
						Block.box(6, 1, 1, 15, 7, 3));
			}
		}
		return Block.box(0, 0, 0, 16, 1, 16);
	}
}
