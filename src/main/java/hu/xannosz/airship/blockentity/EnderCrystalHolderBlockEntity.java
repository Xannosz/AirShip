package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.block.EnderCrystalHolder;
import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ShipData;
import hu.xannosz.airship.util.WrappedHandler;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static hu.xannosz.airship.util.Config.ENERGY_HOLDER_SEND_ENERGY;
import static hu.xannosz.airship.util.Config.ENERGY_PER_CRYSTAL;
import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class EnderCrystalHolderBlockEntity extends BlockEntity {

	@Getter
	int crystal = 0;
	int clock = 0;

	public EnderCrystalHolderBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.ENDER_CRYSTAL_HOLDER_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		tag.putInt("holder.crystal", crystal);
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		crystal = nbt.getInt("holder.crystal");
	}

	public boolean addCrystal() {
		if (crystal > 0) {
			return false;
		}
		crystal = ENERGY_PER_CRYSTAL;
		return true;
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
	}

	@Nonnull
	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			if (level == null) {
				return super.getCapability(cap, side);
			}

			return LazyOptional.of(() -> new WrappedHandler(this)).cast();
		}

		return super.getCapability(cap, side);
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, EnderCrystalHolderBlockEntity blockEntity) {
		blockEntity.tick(state);
	}

	@SuppressWarnings("ConstantConditions")
	private void tick(BlockState state) {
		if (level.isClientSide() || !isInShipDimension(level)) {
			return;
		}

		if (clock == 0) {
			clock = 10;

			if (crystal > 0) {
				ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
				BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

				if (entity instanceof CoreBlockEntity coreBlockEntity) {
					crystal -= coreBlockEntity.increaseCrystalEnergy(Math.min(ENERGY_HOLDER_SEND_ENERGY, crystal));
				}
				setChanged();
			}

			int size = state.getValue(EnderCrystalHolder.SIZE);
			int crystalSize = (int) (((crystal + 0.0f) / ENERGY_PER_CRYSTAL) * EnderCrystalHolder.MAX_SIZE);
			if (crystalSize != size) {
				level.setBlock(getBlockPos(), ModBlocks.ENDER_CRYSTAL_HOLDER.get().defaultBlockState()
						.setValue(EnderCrystalHolder.SIZE, crystalSize), 2, 0);
			}
		}
		clock--;
	}
}
