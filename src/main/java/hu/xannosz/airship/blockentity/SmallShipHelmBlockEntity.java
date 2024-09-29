package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.block.SmallShipHelm;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ShipData;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

@Slf4j
public class SmallShipHelmBlockEntity extends BlockEntity {
	private int clock = 0;

	public SmallShipHelmBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.SMALL_SHIP_HELM_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, SmallShipHelmBlockEntity blockEntity) {
		blockEntity.tick(state);
	}

	@SuppressWarnings("ConstantConditions")
	public void left() {
		BlockState state = level.getBlockState(getBlockPos());
		int pos = state.getValue(SmallShipHelm.POSITION);
		if (pos - 6 > -5) {
			level.setBlock(getBlockPos(), ModBlocks.SMALL_SHIP_HELM.get().defaultBlockState()
					.setValue(SmallShipHelm.POSITION, pos - 2), 2, 0);
		}
	}

	@SuppressWarnings("ConstantConditions")
	public void right() {
		BlockState state = level.getBlockState(getBlockPos());
		int pos = state.getValue(SmallShipHelm.POSITION);
		if (pos - 6 < 5) {
			level.setBlock(getBlockPos(), ModBlocks.SMALL_SHIP_HELM.get().defaultBlockState()
					.setValue(SmallShipHelm.POSITION, pos + 2), 2, 0);
		}
	}

	@SuppressWarnings("ConstantConditions")
	private void tick(BlockState state) {
		if (level.isClientSide() || !isInShipDimension(level)) {
			return;
		}

		if (clock == 0) {
			clock = 20;

			int pos = state.getValue(SmallShipHelm.POSITION);
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

			if (entity instanceof CoreBlockEntity coreBlockEntity) {
				if (pos - 6 < 0) {
					if (pos % 2 == 1) {
						coreBlockEntity.left();
					}
					level.setBlock(getBlockPos(), ModBlocks.SMALL_SHIP_HELM.get().defaultBlockState()
							.setValue(SmallShipHelm.POSITION, pos + 1), 2, 0);
				} else if (pos - 6 > 0) {
					if (pos % 2 == 1) {
						coreBlockEntity.right();
					}
					level.setBlock(getBlockPos(), ModBlocks.SMALL_SHIP_HELM.get().defaultBlockState()
							.setValue(SmallShipHelm.POSITION, pos - 1), 2, 0);
				}
			}
		}
		clock--;
	}
}
