package hu.xannosz.airship.computer;

import hu.xannosz.airship.util.ShipDirection;
import lombok.Data;
import net.minecraft.core.BlockPos;

@Data
public class ComputerShipData {
	private int speed;
	private ShipDirection direction;
	private int existingEnderEnergy;
	private int necessaryEnderEnergy;
	private boolean isEnderEngineOn;

	private double worldCoordinateX;
	private double worldCoordinateZ;
	private String dimension;
	private BlockPos shipCorePosition;
	private int shipSize;
	private String shipName;
}
