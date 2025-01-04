package hu.xannosz.airship.computer;

import hu.xannosz.airship.blockentity.CoreBlockEntity;
import hu.xannosz.airship.config.AirshipConfig;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.DynamicRegistry;
import hu.xannosz.airship.registries.ExternalRegistry;
import hu.xannosz.airship.registries.ShipData;
import hu.xannosz.airship.util.ShipDirection;
import hu.xannosz.airship.util.ShipUtils;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

import static hu.xannosz.airship.util.ShipUtils.*;

@UtilityClass
public class ComputerModule {
	public static List<ShipRadarData> getShipRadarData(ServerLevel level, BlockPos pos) {
		List<ShipRadarData> shipRadarData = new ArrayList<>();
		List<ShipData> ships;
		if (isInShipDimension(level)) {
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
			ships = DynamicRegistry.INSTANCE.getRadarData(shipData.getSWCore()).getShips();
		} else {
			ships = AirShipRegistry.INSTANCE.getShipsInRadius(pos, ShipUtils.toDimensionCode(level), AirshipConfig.RADAR_SCAN_RADIUS.get());
		}
		ships.forEach(s -> {
			ShipRadarData shipData = new ShipRadarData();
			shipData.setWorldCoordinateX(pos.getX() - (int) s.getRWCoreX());
			shipData.setWorldCoordinateZ(pos.getZ() - (int) s.getRWCoreZ());
			shipData.setShipSize(s.getRadius());
			shipData.setShipName(s.getName());
			shipRadarData.add(shipData);
		});
		return shipRadarData;
	}

	public static IndividualShipRadarData getIndividualShipRadarData(ServerLevel level, BlockPos pos, String name) {
		IndividualShipRadarData individualShipRadarData = new IndividualShipRadarData();
		individualShipRadarData.setShipName("");
		if (name.isEmpty()) {
			return individualShipRadarData;
		}

		List<ShipData> ships;
		if (isInShipDimension(level)) {
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
			ships = DynamicRegistry.INSTANCE.getRadarData(shipData.getSWCore()).getShips();
		} else {
			ships = AirShipRegistry.INSTANCE.getShipsInRadius(pos, ShipUtils.toDimensionCode(level), AirshipConfig.RADAR_SCAN_RADIUS.get());
		}
		ships.forEach(s -> {
			if (s.getName().equals(name)) {
				individualShipRadarData.setWorldCoordinateX(pos.getX() - (int) s.getRWCoreX());
				individualShipRadarData.setWorldCoordinateZ(pos.getZ() - (int) s.getRWCoreZ());
				individualShipRadarData.setShipSize(s.getRadius());
				individualShipRadarData.setShipName(s.getName());
				if (toLevel(0, level).getBlockEntity(s.getSWCore()) instanceof CoreBlockEntity coreBlockEntity) {
					individualShipRadarData.setDirection(coreBlockEntity.getDirection());
					individualShipRadarData.setSpeed(coreBlockEntity.getSpeed());
				}
			}
		});

		return individualShipRadarData;
	}

	public static GroundRadarData getGroundRadarData(ServerLevel level, BlockPos pos, int scale) {
		GroundRadarData groundRadarData = new GroundRadarData();
		if (isInShipDimension(level)) {
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
			groundRadarData.setColors(
					DynamicRegistry.INSTANCE.getMapData(pos, scale, toLevel(shipData.getDimensionCode(), level)).getColors());
		} else {
			ShipUtils.fillMapData(level, groundRadarData, scale,
					(pos.getX() / scale) * scale, (pos.getZ() / scale) * scale);
		}
		return groundRadarData;
	}

	public static ComputerShipData getShipData(ServerLevel level, BlockPos pos) {
		ComputerShipData computerShipData = new ComputerShipData();
		if (!isInShipDimension(level)) {
			return computerShipData;
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
		BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

		if (entity instanceof CoreBlockEntity coreBlockEntity) {
			computerShipData.setSpeed(coreBlockEntity.getSpeed());
			computerShipData.setDirection(coreBlockEntity.getDirection());
			computerShipData.setExistingEnderEnergy(coreBlockEntity.getEnderEnergy());
			computerShipData.setNecessaryEnderEnergy(coreBlockEntity.getNecessaryEnderEnergy());
			computerShipData.setEnderEngineOn(coreBlockEntity.isEnderEngineOn());

			computerShipData.setWorldCoordinateX(shipData.getRWCoreX());
			computerShipData.setWorldCoordinateZ(shipData.getRWCoreZ());
			computerShipData.setDimension(ExternalRegistry.INSTANCE.getDimensionName(shipData.getDimensionCode()));
			computerShipData.setShipCorePosition(shipData.getSWCore());
			computerShipData.setShipSize(shipData.getRadius());
			computerShipData.setShipName(shipData.getName());
		} else {
			handleMissingShipCore(level, shipData.getSWCore());
		}

		return computerShipData;
	}

	public static void left(ServerLevel level, BlockPos pos) {
		if (!isInShipDimension(level)) {
			return;
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
		BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

		if (entity instanceof CoreBlockEntity coreBlockEntity) {
			coreBlockEntity.left();
		} else {
			handleMissingShipCore(level, shipData.getSWCore());
		}
	}

	public static void right(ServerLevel level, BlockPos pos) {
		if (!isInShipDimension(level)) {
			return;
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
		BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

		if (entity instanceof CoreBlockEntity coreBlockEntity) {
			coreBlockEntity.right();
		} else {
			handleMissingShipCore(level, shipData.getSWCore());
		}
	}

	public static void slower(ServerLevel level, BlockPos pos) {
		if (!isInShipDimension(level)) {
			return;
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
		BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

		if (entity instanceof CoreBlockEntity coreBlockEntity) {
			coreBlockEntity.slower();
		} else {
			handleMissingShipCore(level, shipData.getSWCore());
		}
	}

	public static void faster(ServerLevel level, BlockPos pos) {
		if (!isInShipDimension(level)) {
			return;
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
		BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

		if (entity instanceof CoreBlockEntity coreBlockEntity) {
			coreBlockEntity.faster();
		} else {
			handleMissingShipCore(level, shipData.getSWCore());
		}
	}

	public static void toggleEnderEngine(ServerLevel level, BlockPos pos) {
		if (!isInShipDimension(level)) {
			return;
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
		BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

		if (entity instanceof CoreBlockEntity coreBlockEntity) {
			coreBlockEntity.toggleEnderEngine();
		} else {
			handleMissingShipCore(level, shipData.getSWCore());
		}
	}

	public static void jumpOneBlock(ServerLevel level, BlockPos pos, ShipDirection direction) {
		if (!isInShipDimension(level)) {
			return;
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
		BlockEntity entity = level.getBlockEntity(shipData.getSWCore());

		if (entity instanceof CoreBlockEntity coreBlockEntity) {
			coreBlockEntity.jumpOneBlock(direction);
		} else {
			handleMissingShipCore(level, shipData.getSWCore());
		}
	}
}
