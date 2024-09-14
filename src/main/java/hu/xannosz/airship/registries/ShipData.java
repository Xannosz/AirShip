package hu.xannosz.airship.registries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipData {
	private double rWCoreX;
	private double rWCoreZ;
	private int dimensionCode;
	private BlockPos sWCore;
	private int radius;

	public void updateCoordinates(double x, double z) {
		rWCoreX += x;
		rWCoreZ += z;
	}

	public void updateDimension(double x, double z, int dimensionCode) {
		rWCoreX = x;
		rWCoreZ = z;
		this.dimensionCode = dimensionCode;
	}
}
