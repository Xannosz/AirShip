package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.item.CoordinatePaper;
import hu.xannosz.airship.item.ModItems;
import hu.xannosz.airship.network.GetMapData;
import hu.xannosz.airship.network.MapData;
import hu.xannosz.airship.network.ModMessages;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.Coordinate;
import hu.xannosz.airship.registries.ExternalRegistry;
import hu.xannosz.airship.registries.ShipData;
import hu.xannosz.airship.screen.NavigationTableMenu;
import hu.xannosz.airship.util.ButtonId;
import hu.xannosz.airship.util.ButtonUser;
import hu.xannosz.airship.util.ShipUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static hu.xannosz.airship.util.ShipUtils.*;

@Slf4j
public class NavigationTableBlockEntity extends BlockEntity implements MenuProvider, ButtonUser {

	//client side too!!!
	@Getter
	@Setter
	private MapData mapData = new MapData(getBlockPos());
	//CLIENT ONLY
	@Setter
	private boolean isOpened = false;
	private int clock = 0;
	private int selectedX = -1;
	private int selectedY = -1;

	private boolean showAirShips = true;
	private int scale = 1;

	@Getter
	private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return switch (slot) {
				case 0 -> false;
				case 1 -> ExternalRegistry.INSTANCE.isCoordinateHolder(stack);
				case 2 -> stack.getItem() == Items.PAPER;
				default -> super.isItemValid(slot, stack);
			};
		}
	};
	@Getter
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

	public NavigationTableBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.NAVIGATION_TABLE_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		tag.put("navigation.inventory", itemHandler.serializeNBT());
		tag.putBoolean("navigation.showAirShips", showAirShips);
		tag.putInt("navigation.scale", scale);
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("navigation.inventory"));
		showAirShips = nbt.getBoolean("navigation.showAirShips");
		scale = nbt.getInt("navigation.scale");
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.literal("Navigation table");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
		return new NavigationTableMenu(containerId, inventory, this);
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void executeButtonClick(ButtonId buttonId) {
		if (buttonId.equals(ButtonId.TOGGLE_SHIPS_DISPLAY)) {
			showAirShips = !showAirShips;
			setChanged();
		}
		if (buttonId.equals(ButtonId.DOWN_SCALE)) {
			scale++;
			if (scale > 5) {
				scale = 5;
			}
		}
		if (buttonId.equals(ButtonId.UP_SCALE)) {
			scale--;
			if (scale < 1) {
				scale = 1;
			}
		}
		if (buttonId.equals(ButtonId.WRITE_COORDINATES)) {
			if (itemHandler.getStackInSlot(2).getItem().equals(Items.PAPER) &&
					itemHandler.getStackInSlot(2).getCount() > 0 &&
					itemHandler.getStackInSlot(0).isEmpty()) {

				itemHandler.getStackInSlot(2).shrink(1);
				ItemStack coordinatePaper = new ItemStack(ModItems.COORDINATE_PAPER.get());

				if (itemHandler.getStackInSlot(1).isEmpty()) {
					if (isInShipDimension(level)) {
						ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
						coordinatePaper.getOrCreateTag().putString(CoordinatePaper.DIM_TAG,
								ExternalRegistry.INSTANCE.getDimensionName(shipData.getDimensionCode()));
						coordinatePaper.getOrCreateTag().putInt(CoordinatePaper.X_TAG, (int) Math.round(shipData.getRWCoreX()));
						coordinatePaper.getOrCreateTag().putInt(CoordinatePaper.Z_TAG, (int) Math.round(shipData.getRWCoreZ()));
					} else {
						coordinatePaper.getOrCreateTag().putString(CoordinatePaper.DIM_TAG, levelToString(level));
						coordinatePaper.getOrCreateTag().putInt(CoordinatePaper.X_TAG, getBlockPos().getX());
						coordinatePaper.getOrCreateTag().putInt(CoordinatePaper.Z_TAG, getBlockPos().getY());
					}
				} else {
					if (ExternalRegistry.INSTANCE.isCoordinateHolder(itemHandler.getStackInSlot(1))) {
						ServerLevel serverWorld;
						if (isInShipDimension(level)) {
							final ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
							serverWorld = toLevel(shipData.getDimensionCode(), level);
						} else {
							serverWorld = (ServerLevel) level;
						}

						Coordinate coordinate = ExternalRegistry.INSTANCE.getCoordinate(itemHandler.getStackInSlot(1), serverWorld);
						if (coordinate == null) {
							return;
						}

						coordinatePaper.getOrCreateTag().putString(CoordinatePaper.DIM_TAG, coordinate.dimension());
						coordinatePaper.getOrCreateTag().putInt(CoordinatePaper.X_TAG, coordinate.x());
						coordinatePaper.getOrCreateTag().putInt(CoordinatePaper.Z_TAG, coordinate.z());
					}
				}

				itemHandler.setStackInSlot(0, coordinatePaper);
				setChanged();
			}
		}
	}

	public void drops() {
		SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}
		if (level != null) {
			Containers.dropContents(level, worldPosition, inventory);
		}
	}

	public void clickOnMap(int x, int y) {
		selectedX = x;
		selectedY = y;
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, NavigationTableBlockEntity blockEntity) {
		blockEntity.tick();
	}

	@SuppressWarnings("ConstantConditions")
	private void tick() {
		if (level.isClientSide()) {
			if (isOpened) {
				isOpened = false;
				if (clock == 0) {
					clock = 5;
					ModMessages.sendToServer(new GetMapData(getBlockPos()));
				}
				clock--;
			}
		} else {
			if (clock == 0) {
				clock = 5;
				//log.info("SCX " + selectedX);
				//log.info("SCY " + selectedY);

				int x;
				int z;
				ServerLevel serverLevel;
				if (isInShipDimension(level)) {
					ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);

					mapData.setRealX((int) Math.round(shipData.getRWCoreX()));
					mapData.setRealZ((int) Math.round(shipData.getRWCoreZ()));
					mapData.setName(shipData.getName());

					x = (((int) Math.round(shipData.getRWCoreX())) / scale) * scale;
					z = (((int) Math.round(shipData.getRWCoreZ())) / scale) * scale;

					serverLevel = toLevel(shipData.getDimensionCode(), level);

					BlockEntity entity = level.getBlockEntity(shipData.getSWCore());
					if (entity instanceof CoreBlockEntity coreBlockEntity) {
						mapData.setDirection(coreBlockEntity.getDirection());
						mapData.setSpeed(coreBlockEntity.getSpeed());
					}
				} else {

					mapData.setRealX(getBlockPos().getX());
					mapData.setRealZ(getBlockPos().getZ());

					x = (getBlockPos().getX() / scale) * scale;
					z = (getBlockPos().getZ() / scale) * scale;
					serverLevel = (ServerLevel) level;
				}

				mapData.setHasTarget(false);
				if (ExternalRegistry.INSTANCE.isCoordinateHolder(itemHandler.getStackInSlot(1))) {
					final Coordinate coordinate = ExternalRegistry.INSTANCE.getCoordinate(itemHandler.getStackInSlot(1), serverLevel);
					if (coordinate != null && coordinate.dimension().equals(levelToString(serverLevel))) {
						mapData.setTargetX(coordinate.x());
						mapData.setTargetZ(coordinate.z());
						mapData.setHasTarget(true);
					}
				}

				mapData.setShowAirShips(showAirShips);
				ShipUtils.fillMapData(serverLevel, mapData, scale, x, z);

				//List<ShipData> ships = AirShipRegistry.INSTANCE.getShipsInRadius(getBlockPos(), toDimensionCode(level), 60 * scale);

				//int j2 = (x / scale + runX - 50) * scale;
				//int k2 = (z / scale + runZ - 50) * scale;
			}
			clock--;
		}
	}

	private void drawMap(int x, int z, ServerLevel serverLevel) {
		int t = scale * 50;
		for (int runX = 0; runX < 100; runX++) {
			for (int runZ = 0; runZ < 100; runZ++) {
				mapData.setColor(runX, 100 - runZ,
						getColorFor(x + runX * scale - t, z + runZ * scale - t, serverLevel));
			}
		}
	}

	private int getColorFor(int x, int z, ServerLevel serverLevel) {
		for (int y = 400; y > -100; y--) {
			BlockPos pos = new BlockPos(x, y, z);
			BlockState underTest = serverLevel.getBlockState(pos);
			if (!underTest.getBlock().equals(Blocks.AIR) && !underTest.getBlock().equals(Blocks.VOID_AIR)) {
				serverLevel.getBrightness(LightLayer.SKY, pos);
				return underTest.getMapColor(serverLevel, pos).col;
			}
		}
		return Blocks.AIR.defaultMapColor().col;
	}

	public void responseToServer(ServerPlayer player) {
		ModMessages.sendToPlayer(mapData, player);
	}
}
