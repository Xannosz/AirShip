package hu.xannosz.airship.computer;

import lombok.Data;

@Data
public class ShipRadarData {
	private int worldCoordinateX;
	private int worldCoordinateZ;
	private int shipSize;
	private String shipName;
}
