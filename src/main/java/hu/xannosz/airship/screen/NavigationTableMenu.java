package hu.xannosz.airship.screen;

import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.blockentity.NavigationTableBlockEntity;
import hu.xannosz.airship.network.MapData;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class NavigationTableMenu extends AbstractContainerMenu {

	private static final int PLAYER_INVENTORY_HEIGHT = 137;

	@Getter
	private final NavigationTableBlockEntity blockEntity;
	private final Level level;

	@SuppressWarnings("resource")
	public NavigationTableMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
		this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
	}

	public NavigationTableMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
		super(ModMenus.NAVIGATION_TABLE_MENU.get(), containerId);

		checkContainerSize(inv, 3);
		this.blockEntity = ((NavigationTableBlockEntity) blockEntity);
		level = inv.player.level();

		addPlayerInventory(inv);
		addPlayerHotBar(inv);

		ItemStackHandler handler = this.blockEntity.getItemHandler();
		SlotItemHandler mapSlot = new SlotItemHandler(handler, 1, 8, 113);
		SlotItemHandler paperSlot = new SlotItemHandler(handler, 2, 56, 113);
		SlotItemHandler coordinateSlot = new SlotItemHandler(handler, 0, 92, 113);
		this.addSlot(mapSlot);
		this.addSlot(paperSlot);
		this.addSlot(coordinateSlot);
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
				player, ModBlocks.NAVIGATION_TABLE.get());
	}

	// CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
	// must assign a slot number to each of the slots used by the GUI.
	// For this container, we can see both the tile inventory's slots and the player inventory slots and the hotBar.
	// Each time we add a Slot to the container, it automatically increases the slotIndex, which means
	//  0 - 8 = hotBar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
	//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
	//  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
	private static final int HOT_BAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOT_BAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

	// THIS YOU HAVE TO DEFINE!
	private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
		Slot sourceSlot = slots.get(index);
		if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
		ItemStack sourceStack = sourceSlot.getItem();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
					+ TE_INVENTORY_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;  // EMPTY_ITEM
			}
		} else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
			// This is a TE slot so merge the stack into the players inventory
			if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			return ItemStack.EMPTY;
		}
		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) {
			sourceSlot.set(ItemStack.EMPTY);
		} else {
			sourceSlot.setChanged();
		}
		sourceSlot.onTake(playerIn, sourceStack);
		return copyOfSourceStack;
	}

	private void addPlayerInventory(Inventory playerInventory) {
		for (int i = 0; i < 3; ++i) {
			for (int l = 0; l < 9; ++l) {
				this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, PLAYER_INVENTORY_HEIGHT + i * 18));
			}
		}
	}

	private void addPlayerHotBar(Inventory playerInventory) {
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, PLAYER_INVENTORY_HEIGHT + 58));
		}
	}

	public MapData getMapData() {
		return blockEntity.getMapData();
	}
}
