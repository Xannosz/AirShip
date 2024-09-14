package hu.xannosz.airship.block;

import hu.xannosz.airship.blockentity.ModBlockEntities;
import hu.xannosz.airship.blockentity.NavigationTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NavigationTable extends BaseEntityBlock {
	public NavigationTable() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(4.3F).noOcclusion().sound(SoundType.WOOD));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new NavigationTableBlockEntity(blockPos, blockState);
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
		if (!level.isClientSide()) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof NavigationTableBlockEntity navigationTableBlockEntity) {
				NetworkHooks.openScreen((ServerPlayer) player, navigationTableBlockEntity, pos);
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}

		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModBlockEntities.NAVIGATION_TABLE_BLOCK_ENTITY.get(), NavigationTableBlockEntity::tick);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState nextBlockState, boolean p_48717_) {
		BlockEntity blockentity = level.getBlockEntity(blockPos);
		if (blockentity instanceof NavigationTableBlockEntity navigationTableBlockEntity) {
			navigationTableBlockEntity.drops();
		}
		super.onRemove(blockState, level, blockPos, nextBlockState, p_48717_);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
		return Shapes.block();
	}
}
