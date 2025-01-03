package hu.xannosz.airship.registries;

import hu.xannosz.airship.registries.ShipData;
import lombok.Data;

import java.util.List;

@Data
public class InnerRadarData {
	private long time;
	private List<ShipData> ships;
}
