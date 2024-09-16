package hu.xannosz.airship.block;

import hu.xannosz.airship.blockentity.ModBlockEntities;
import hu.xannosz.airship.blockentity.OneBlockJumperBlockEntity;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class OneBlockJumper extends BaseEntityBlock {
	public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 3);
	public static final BooleanProperty CLICKED = BooleanProperty.create("clicked");

	public OneBlockJumper() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(4.3F).noOcclusion().sound(SoundType.WOOD));
		this.registerDefaultState(this.stateDefinition.any().setValue(POSITION, 0).setValue(CLICKED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(POSITION).add(CLICKED);
	}

	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos pos,
										  @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
		if (!level.isClientSide() || !isInShipDimension(level)) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof OneBlockJumperBlockEntity oneBlockJumperBlockEntity) {
				oneBlockJumperBlockEntity.turn();
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
			if (entity instanceof OneBlockJumperBlockEntity oneBlockJumperBlockEntity) {
				oneBlockJumperBlockEntity.jump();
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new OneBlockJumperBlockEntity(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModBlockEntities.ONE_BLOCK_JUMPER_BLOCK_ENTITY.get(), OneBlockJumperBlockEntity::tick);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
		if(blockState.getValue(CLICKED)) {
			return Shapes.or(Block.box(1, 0, 1, 15, 1, 15),
					Block.box(6, 1, 6, 10, 2.5, 10));
		}else {
			return Shapes.or(Block.box(1, 0, 1, 15, 1, 15),
					Block.box(6, 1, 6, 10, 4, 10));
		}
	}
}
