package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.block.Compass;
import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ShipData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class CompassBlockEntity extends BlockEntity {
	public CompassBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.COMPASS_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, CompassBlockEntity blockEntity) {
		blockEntity.tick();
	}

	@SuppressWarnings("ConstantConditions")
	private void tick() {
		if (level.isClientSide() || !isInShipDimension(level)) {
			return;
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
		BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

		if (entity instanceof CoreBlockEntity coreBlockEntity) {
			level.setBlock(getBlockPos(), ModBlocks.COMPASS.get().defaultBlockState()
					.setValue(Compass.POSITION, coreBlockEntity.getDirection().getCode()), 2, 0);
		}
	}
}
