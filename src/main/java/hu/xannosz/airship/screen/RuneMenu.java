package hu.xannosz.airship.screen;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.blockentity.SmallRuneBlockEntity;
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

public class RuneMenu extends AbstractContainerMenu {
	@Getter
	private final SmallRuneBlockEntity blockEntity;
	private final Level level;

	@SuppressWarnings("resource")
	public RuneMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
		this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
	}

	public RuneMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
		super(ModMenus.RUNE_MENU.get(), containerId);

		checkContainerSize(inv, 0);
		this.blockEntity = ((SmallRuneBlockEntity) blockEntity);
		level = inv.player.level();
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
				player, ModBlocks.SMALL_RUNE.get());
	}
}
