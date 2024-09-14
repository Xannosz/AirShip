package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.block.EnderEngineGauge;
import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ShipData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class EnderEngineGaugeBlockEntity extends BlockEntity {
	int clock = 0;

	public EnderEngineGaugeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.ENDER_ENGINE_GAUGE_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, EnderEngineGaugeBlockEntity blockEntity) {
		blockEntity.tick(state);
	}

	@SuppressWarnings("ConstantConditions")
	private void tick(BlockState state) {
		if (level.isClientSide() || !isInShipDimension(level)) {
			return;
		}

		if (clock == 0) {
			clock = 10;

			int energy = state.getValue(EnderEngineGauge.ENERGY);
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

			if (entity instanceof CoreBlockEntity coreBlockEntity) {
				int actualEnergy = (int) (((coreBlockEntity.getEnderEnergy() + 0.0f) / coreBlockEntity.getNecessaryEnderEnergy()) * EnderEngineGauge.MAX_ENERGY);
				if (actualEnergy != energy) {
					level.setBlock(getBlockPos(), ModBlocks.ENDER_ENGINE_GAUGE.get().defaultBlockState()
							.setValue(EnderEngineGauge.ENERGY, actualEnergy), 2, 0);
				}
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}
		clock--;
	}
}
