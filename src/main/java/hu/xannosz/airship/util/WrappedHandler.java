package hu.xannosz.airship.util;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.blockentity.EnderCrystalHolderBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class WrappedHandler implements IItemHandlerModifiable {
	private final EnderCrystalHolderBlockEntity enderCrystalHolderBlockEntity;

	public WrappedHandler(EnderCrystalHolderBlockEntity enderCrystalHolderBlockEntity) {
		this.enderCrystalHolderBlockEntity = enderCrystalHolderBlockEntity;
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		if (slot == 0 && stack.getItem().equals(ModBlocks.ENDER_CRYSTAL.get().asItem())) {
			if (enderCrystalHolderBlockEntity.addCrystal()) {
				stack.shrink(1);
			}
		}
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (slot == 0 && stack.getItem().equals(ModBlocks.ENDER_CRYSTAL.get().asItem())) {
			if (enderCrystalHolderBlockEntity.addCrystal()) {
				stack.shrink(1);
			}
		}
		return stack;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return enderCrystalHolderBlockEntity.getCrystal() > 0 ? 0 : 1;
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return slot == 0 && stack.getItem().equals(ModBlocks.ENDER_CRYSTAL.get().asItem());
	}
}
