package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.block.EnderEngineButton;
import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ShipData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class EnderEngineButtonBlockEntity extends BlockEntity {
	int clock = 0;

	public EnderEngineButtonBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.ENDER_ENGINE_BUTTON_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, EnderEngineButtonBlockEntity blockEntity) {
		blockEntity.tick(state);
	}

	@SuppressWarnings("ConstantConditions")
	private void tick(BlockState state) {
		if (level.isClientSide() || !isInShipDimension(level)) {
			return;
		}

		if (clock == 0) {
			clock = 10;

			boolean isOn = state.getValue(EnderEngineButton.STATE);
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

			if (entity instanceof CoreBlockEntity coreBlockEntity) {
				if (coreBlockEntity.isEnderEngineOn() != isOn) {
					level.setBlock(getBlockPos(), ModBlocks.ENDER_ENGINE_BUTTON.get().defaultBlockState()
							.setValue(EnderEngineButton.STATE, coreBlockEntity.isEnderEngineOn()), 2, 0);
				}
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}
		clock--;
	}
}
