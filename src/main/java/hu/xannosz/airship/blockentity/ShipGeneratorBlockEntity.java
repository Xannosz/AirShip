package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.block.ShipGenerator;
import hu.xannosz.airship.item.ModItems;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.shipmodels.ShipModel;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static hu.xannosz.airship.item.ShipDetector.CORE_POSITION_TAG;
import static hu.xannosz.airship.util.Config.DEFAULT_SHIP_RADIUS;
import static hu.xannosz.airship.util.ShipUtils.*;

@Slf4j
public class ShipGeneratorBlockEntity extends BlockEntity {
	int clock = 0;
	int progress = 0;
	BlockPos corePosition = new BlockPos(0, 0, 0);
	ShipModel model;

	public ShipGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.SHIP_GENERATOR_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		tag.putInt("generator.progress", progress);
		tag.putInt("generator.core.x", corePosition.getX());
		tag.putInt("generator.core.y", corePosition.getY());
		tag.putInt("generator.core.z", corePosition.getZ());
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		progress = nbt.getInt("generator.progress");
		corePosition = new BlockPos(nbt.getInt("generator.core.x"),
				nbt.getInt("generator.core.y"), nbt.getInt("generator.core.z"));
	}

	public void dropItem() {
		SimpleContainer inventory = new SimpleContainer(1);

		ItemStack detector = new ItemStack(ModItems.SHIP_DETECTOR.get(), 1);
		detector.getOrCreateTag().put(CORE_POSITION_TAG, NbtUtils.writeBlockPos(corePosition));

		inventory.addItem(detector);

		if (level != null) {
			Containers.dropContents(level, worldPosition, inventory);
			level.setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), 2, 0);
		}
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, ShipGeneratorBlockEntity blockEntity) {
		blockEntity.tick();
	}

	@SuppressWarnings("ConstantConditions")
	private void tick() {
		if (level.isClientSide() || isInShipDimension(level)) { //TODO break it!!!!
			return;
		}

		if (model == null) {
			model = getModelForDimension(toDimensionCode(level));
		}

		if (clock == 0) {
			clock = 50;

			final ServerLevel shipWorld = toLevel(getShipDimName(), (ServerLevel) level);

			if (corePosition.getY() == 0) {
				corePosition = AirShipRegistry.INSTANCE.getNewCorePosition(getBlockPos(), level, DEFAULT_SHIP_RADIUS);
				shipWorld.setBlock(corePosition, ModBlocks.CORE.get().defaultBlockState(), 2, 0);
			}

			for (int i = progress * 100; i < (progress + 1) * 100 && i < model.getBlocks().size(); i++) {
				shipWorld.setBlock(new BlockPos(
						model.getBlocks().get(i).getFirst().getX() + corePosition.getX(),
						model.getBlocks().get(i).getFirst().getY() + corePosition.getY(),
						model.getBlocks().get(i).getFirst().getZ() + corePosition.getZ()
				), model.getBlocks().get(i).getSecond(), 2, 0);
			}

			progress++;
			setChanged();
		}

		if (progress > model.getBlocks().size() / 100) {
			level.setBlock(getBlockPos(), ModBlocks.SHIP_GENERATOR.get().defaultBlockState()
					.setValue(ShipGenerator.STATE, true), 2, 0);
		}
		clock--;
	}
}
