package hu.xannosz.airship.screen;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.blockentity.LocalRuneBlockEntity;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class LocalRuneMenu extends AbstractContainerMenu {
	@Getter
	private final LocalRuneBlockEntity blockEntity;
	private final Level level;

	@SuppressWarnings("resource")
	public LocalRuneMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
		this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
	}

	public LocalRuneMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
		super(ModMenus.LOCAL_RUNE_MENU.get(), containerId);

		checkContainerSize(inv, 0);
		this.blockEntity = ((LocalRuneBlockEntity) blockEntity);
		level = inv.player.level();
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
				player, ModBlocks.LOCAL_RUNE.get());
	}
}
