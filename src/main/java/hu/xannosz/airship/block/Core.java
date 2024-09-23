package hu.xannosz.airship.block;

import hu.xannosz.airship.blockentity.CoreBlockEntity;
import hu.xannosz.airship.blockentity.ModBlockEntities;
import hu.xannosz.airship.item.ModItems;
import hu.xannosz.airship.registries.AirShipRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static hu.xannosz.airship.item.ShipDetector.CORE_POSITION_TAG;
import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class Core extends BaseEntityBlock {
	public Core() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(-1.0F, 3600000.0F).noLootTable().noOcclusion().sound(SoundType.METAL));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new CoreBlockEntity(blockPos, blockState);
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
			if (player.getItemInHand(hand).getItem().equals(Items.COMPASS)) {
				ItemStack detector = new ItemStack(ModItems.SHIP_DETECTOR.get(), 1);
				detector.getOrCreateTag().put(CORE_POSITION_TAG, NbtUtils.writeBlockPos(pos));
				player.setItemInHand(hand, detector);
			}
			if (player.getItemInHand(hand).getItem().equals(Items.NAME_TAG) &&
					player.getItemInHand(hand).hasCustomHoverName() &&
					!player.getItemInHand(hand).getHoverName().getString().equals("")) {
				AirShipRegistry.INSTANCE.updateName(player.getItemInHand(hand).getHoverName().getString(), pos);
			}
		}

		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModBlockEntities.CORE_BLOCK_ENTITY.get(), CoreBlockEntity::tick);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
		return Shapes.block();
	}
}
