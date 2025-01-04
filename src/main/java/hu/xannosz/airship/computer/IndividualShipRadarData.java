package hu.xannosz.airship.computer;

import hu.xannosz.airship.util.ShipDirection;
import lombok.Data;

@Data
public class IndividualShipRadarData {
	private int worldCoordinateX;
	private int worldCoordinateZ;
	private int shipSize;
	private String shipName;
	private ShipDirection direction;
	private int speed;
}
