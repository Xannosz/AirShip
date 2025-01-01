package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.config.AirshipConfig;
import hu.xannosz.airship.network.GetRadarData;
import hu.xannosz.airship.network.ModMessages;
import hu.xannosz.airship.network.RadarData;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.DynamicRegistry;
import hu.xannosz.airship.registries.ShipData;
import hu.xannosz.airship.screen.RadarMenu;
import hu.xannosz.airship.util.ShipUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class RadarBlockEntity extends BlockEntity implements MenuProvider {

	//client side too!!!
	@Getter
	@Setter
	private RadarData radarData = new RadarData(getBlockPos());
	@Setter
	private boolean isOpened = false;
	private int clock = 0;

	//Server only
	private String selectedShipName = "";

	public RadarBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.RADAR_BLOCK_ENTITY.get(), blockPos, blockState);
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.literal("Radar");
	}

	@Override
	protected void saveAdditional(@NotNull CompoundTag tag) {
		tag.putString("radar.selectedShipName", selectedShipName);
		super.saveAdditional(tag);
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		selectedShipName = nbt.getString("radar.selectedShipName");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
		return new RadarMenu(containerId, inventory, this);
	}

	public void clickOnRadar(String shipName) {
		selectedShipName = shipName;
	}

	@SuppressWarnings("unused")
	public static void tick(Level level, BlockPos pos, BlockState state, RadarBlockEntity blockEntity) {
		blockEntity.tick();
	}

	@SuppressWarnings("ConstantConditions")
	private void tick() {
		if (level.isClientSide()) {
			if (isOpened) {
				isOpened = false;
				if (clock == 0) {
					clock = 5;
					ModMessages.sendToServer(new GetRadarData(getBlockPos()));
				}
				clock--;
			}
		} else {
			if (clock == 0) {
				clock = 5;
				List<ShipData> ships = new ArrayList<>();
				if (isOpened) {
					if (isInShipDimension(level)) {
						ShipData shipData = AirShipRegistry.INSTANCE.isInShip(getBlockPos(), 0);
						ships = DynamicRegistry.INSTANCE.getRadarData(shipData.getSWCore()).getShips();
					} else {
						ships = AirShipRegistry.INSTANCE.getShipsInRadius(getBlockPos(), ShipUtils.toDimensionCode(level), AirshipConfig.RADAR_SCAN_RADIUS.get());
					}
				}
				radarData.setName("");
				radarData.setDirection(null);
				radarData.setRealX(0);
				radarData.setRealZ(0);
				radarData.setSpeed(0);
				for (ShipData ship : ships) {
					radarData.setShip(getBlockPos().getX() - (int) ship.getRWCoreX(), getBlockPos().getZ() - (int) ship.getRWCoreZ(), ship.getName());
					if (ship.getName().equals(selectedShipName)) {
						radarData.setName(ship.getName());
						radarData.setRealX((int) ship.getRWCoreX());
						radarData.setRealZ((int) ship.getRWCoreZ());
						if (level.getBlockEntity(ship.getSWCore()) instanceof CoreBlockEntity coreBlockEntity) {
							radarData.setDirection(coreBlockEntity.getDirection());
							radarData.setSpeed(coreBlockEntity.getSpeed());
						}
					}
				}
			}
			clock--;
		}
	}

	public void responseToServer(ServerPlayer player) {
		isOpened = true;
		ModMessages.sendToPlayer(radarData, player);
	}
}
