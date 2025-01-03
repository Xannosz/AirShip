package hu.xannosz.airship.registries;

import com.mojang.datafixers.util.Pair;
import hu.xannosz.airship.config.AirshipConfig;
import hu.xannosz.airship.network.MapData;
import hu.xannosz.airship.util.ShipUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class DynamicRegistry {

	public static DynamicRegistry INSTANCE = new DynamicRegistry();

	private final Map<BlockPos, InnerRadarData> radarData = new HashMap<>();
	private final Map<Pair<BlockPos, Integer>, InnerMapData> mapData = new HashMap<>();

	public InnerRadarData getRadarData(BlockPos pos) {
		if (!radarData.containsKey(pos) ||
				radarData.get(pos).getTime() < System.currentTimeMillis() - 250) {
			updateRadarData(pos);
		}
		return radarData.get(pos);
	}

	public InnerMapData getMapData(BlockPos pos, int scale, Level level) {
		if (!mapData.containsKey(new Pair<>(pos, scale)) ||
				mapData.get(new Pair<>(pos, scale)).getTime() < System.currentTimeMillis() - 250) {
			updateMapData(pos, scale, level);
		}
		return mapData.get(new Pair<>(pos, scale));
	}

	private void updateRadarData(BlockPos pos) {
		InnerRadarData innerRadarData = new InnerRadarData();
		innerRadarData.setTime(System.currentTimeMillis());
		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);

		innerRadarData.setShips(AirShipRegistry.INSTANCE.getShipsInRadius(new BlockPos((int) shipData.getRWCoreX(),
						100, (int) shipData.getRWCoreZ()), shipData.getDimensionCode(),
				AirshipConfig.RADAR_SCAN_RADIUS.get()));
		radarData.put(pos, innerRadarData);
	}

	private void updateMapData(BlockPos pos, int scale, Level level) {
		InnerMapData innerMapData = new InnerMapData();
		innerMapData.setTime(System.currentTimeMillis());
		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(pos, 0);
		MapData mapD = new MapData(pos);
		ShipUtils.fillMapData(level, mapD, scale, (int) shipData.getRWCoreX(), (int) shipData.getRWCoreZ());
		innerMapData.setColors(mapD.getColors());
		mapData.put(new Pair<>(pos, scale), innerMapData);
	}
}
