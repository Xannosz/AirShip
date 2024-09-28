package hu.xannosz.airship.block;

import hu.xannosz.airship.blockentity.EnderCrystalHolderBlockEntity;
import hu.xannosz.airship.blockentity.ModBlockEntities;
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

public class EnderCrystalHolder extends BaseEntityBlock {

	public static final int MAX_SIZE = 16;
	public static final IntegerProperty SIZE = IntegerProperty.create("crystal", 0, MAX_SIZE);

	public EnderCrystalHolder() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.3F).noOcclusion().sound(SoundType.METAL));
		this.registerDefaultState(this.stateDefinition.any().setValue(SIZE, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(SIZE);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new EnderCrystalHolderBlockEntity(blockPos, blockState);
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
		if (!level.isClientSide() && isInShipDimension(level)) {
			if (player.getItemInHand(hand).getItem().equals(ModBlocks.ENDER_CRYSTAL.get().asItem())) {
				BlockEntity entity = level.getBlockEntity(pos);
				if (entity instanceof EnderCrystalHolderBlockEntity enderCrystalHolderBlockEntity) {
					if (enderCrystalHolderBlockEntity.addCrystal()) {
						player.getItemInHand(hand).shrink(1);
					}
				} else {
					throw new IllegalStateException("Our Container provider is missing!");
				}
			}
		}

		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModBlockEntities.ENDER_CRYSTAL_HOLDER_BLOCK_ENTITY.get(), EnderCrystalHolderBlockEntity::tick);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
		return Shapes.or(Block.box(0, 0, 0, 16, 1, 16),
				Block.box(6, 1, 6, 10, 3, 10));
	}
}
