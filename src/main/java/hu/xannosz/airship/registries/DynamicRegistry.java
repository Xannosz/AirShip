package hu.xannosz.airship.registries;

import hu.xannosz.airship.config.AirshipConfig;
import hu.xannosz.airship.network.InnerRadarData;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class DynamicRegistry {

	public static DynamicRegistry INSTANCE = new DynamicRegistry();

	private final Map<BlockPos, InnerRadarData> radarData = new HashMap<>();

	public InnerRadarData getRadarData(BlockPos pos) {
		if (!radarData.containsKey(pos) ||
				radarData.get(pos).getTime() < System.currentTimeMillis() - 250) {
			updateRadarData(pos);
		}
		return radarData.get(pos);
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
}
