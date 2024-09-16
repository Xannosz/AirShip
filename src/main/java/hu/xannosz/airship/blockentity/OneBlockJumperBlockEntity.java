package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.block.OneBlockJumper;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ShipData;
import hu.xannosz.airship.util.ShipDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class OneBlockJumperBlockEntity extends BlockEntity {
	private int clock = 0;
	private boolean isClicked = false;

	public OneBlockJumperBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.ONE_BLOCK_JUMPER_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, OneBlockJumperBlockEntity blockEntity) {
		blockEntity.tick(state);
	}

	@SuppressWarnings("ConstantConditions")
	public void turn() {
		final BlockState state = level.getBlockState(getBlockPos());
		int nextPos = state.getValue(OneBlockJumper.POSITION) + 1;
		if (nextPos > 3) {
			nextPos = 0;
		}
		level.setBlock(getBlockPos(), ModBlocks.ONE_BLOCK_JUMPER.get().defaultBlockState()
				.setValue(OneBlockJumper.CLICKED, state.getValue(OneBlockJumper.CLICKED))
				.setValue(OneBlockJumper.POSITION, nextPos), 2, 0);
	}

	@SuppressWarnings("ConstantConditions")
	public void jump() {
		final BlockState state = level.getBlockState(getBlockPos());
		if (!state.getValue(OneBlockJumper.CLICKED)) {
			int pos = state.getValue(OneBlockJumper.POSITION);
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
			BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

			if (entity instanceof CoreBlockEntity coreBlockEntity) {
				switch (pos) {
					case 0 -> coreBlockEntity.jumpOneBlock(ShipDirection.N);
					case 1 -> coreBlockEntity.jumpOneBlock(ShipDirection.E);
					case 2 -> coreBlockEntity.jumpOneBlock(ShipDirection.S);
					case 3 -> coreBlockEntity.jumpOneBlock(ShipDirection.W);
				}

				level.setBlock(getBlockPos(), ModBlocks.ONE_BLOCK_JUMPER.get().defaultBlockState()
						.setValue(OneBlockJumper.POSITION, pos)
						.setValue(OneBlockJumper.CLICKED, true), 2, 0);
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	private void tick(BlockState state) {
		if (level.isClientSide() || !isInShipDimension(level)) {
			return;
		}

		if (clock == 0) {
			clock = 10;
			if (isClicked) {
				isClicked = false;
				level.setBlock(getBlockPos(), ModBlocks.ONE_BLOCK_JUMPER.get().defaultBlockState()
						.setValue(OneBlockJumper.POSITION, state.getValue(OneBlockJumper.POSITION))
						.setValue(OneBlockJumper.CLICKED, false), 2, 0);
			} else {
				if (state.getValue(OneBlockJumper.CLICKED)) {
					isClicked = true;
				}
			}
		}
		clock--;
	}
}
