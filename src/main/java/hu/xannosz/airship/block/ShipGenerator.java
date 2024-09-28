package hu.xannosz.airship.block;

import hu.xannosz.airship.blockentity.ModBlockEntities;
import hu.xannosz.airship.blockentity.ShipGeneratorBlockEntity;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class ShipGenerator extends BaseEntityBlock {

	public static final BooleanProperty STATE = BooleanProperty.create("state");

	public ShipGenerator() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(-1.0F, 3600000.0F).noLootTable().noOcclusion().sound(SoundType.METAL));
		this.registerDefaultState(this.stateDefinition.any().setValue(STATE, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(STATE);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new ShipGeneratorBlockEntity(blockPos, blockState);
	}

	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
										  @NotNull Player player, @NotNull InteractionHand hand,
										  @NotNull BlockHitResult hit) {
		if (!level.isClientSide() && state.getValue(STATE) && !isInShipDimension(level)) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof ShipGeneratorBlockEntity shipGeneratorBlockEntity) {
				shipGeneratorBlockEntity.dropItem();
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}

		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModBlockEntities.SHIP_GENERATOR_BLOCK_ENTITY.get(), ShipGeneratorBlockEntity::tick);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
		return Block.box(4, 0, 4, 12, 8, 12);
	}
}
