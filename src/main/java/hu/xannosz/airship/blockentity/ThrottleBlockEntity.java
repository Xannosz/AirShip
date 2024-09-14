package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.block.Throttle;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ShipData;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

@Slf4j
public class ThrottleBlockEntity extends BlockEntity {
	public ThrottleBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.THROTTLE_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, ThrottleBlockEntity blockEntity) {
		blockEntity.tick();
	}

	@SuppressWarnings("ConstantConditions")
	public void faster() {
		BlockState state = level.getBlockState(getBlockPos());
		int pos = state.getValue(Throttle.POSITION);
		if (pos < 4) {
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

			if (entity instanceof CoreBlockEntity coreBlockEntity) {
				coreBlockEntity.faster();
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	public void slower() {
		BlockState state = level.getBlockState(getBlockPos());
		int pos = state.getValue(Throttle.POSITION);
		if (pos > 0) {
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

			if (entity instanceof CoreBlockEntity coreBlockEntity) {
				coreBlockEntity.slower();
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	private void tick() {
		if (level.isClientSide() || !isInShipDimension(level)) {
			return;
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
		BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

		if (entity instanceof CoreBlockEntity coreBlockEntity) {
			switch (coreBlockEntity.getSpeed()) {
				case 0 -> level.setBlock(getBlockPos(), ModBlocks.THROTTLE.get().defaultBlockState()
						.setValue(Throttle.POSITION, 0), 2, 0);
				case 5 -> level.setBlock(getBlockPos(), ModBlocks.THROTTLE.get().defaultBlockState()
						.setValue(Throttle.POSITION, 1), 2, 0);
				case 10 -> level.setBlock(getBlockPos(), ModBlocks.THROTTLE.get().defaultBlockState()
						.setValue(Throttle.POSITION, 2), 2, 0);
				case 50 -> level.setBlock(getBlockPos(), ModBlocks.THROTTLE.get().defaultBlockState()
						.setValue(Throttle.POSITION, 3), 2, 0);
				case 100 -> level.setBlock(getBlockPos(), ModBlocks.THROTTLE.get().defaultBlockState()
						.setValue(Throttle.POSITION, 4), 2, 0);
			}
		}
	}
}
