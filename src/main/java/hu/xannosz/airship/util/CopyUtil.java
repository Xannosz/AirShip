package hu.xannosz.airship.util;

import hu.xannosz.airship.registries.Coordinate;
import hu.xannosz.airship.registries.Dimension;
import hu.xannosz.airship.registries.ShipData;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;

@UtilityClass
public class CopyUtil {
	public static BlockPos copy(BlockPos pos) {
		return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
	}

	public static Coordinate copy(Coordinate coordinate) {
		return new Coordinate(coordinate.x(), coordinate.z(), coordinate.dimension());
	}

	public static Dimension copy(Dimension dimension) {
		return new Dimension(dimension.key(), dimension.name(), dimension.model(), dimension.referenceY());
	}

	public static ShipData copy(ShipData shipData) {
		return new ShipData(shipData.getRWCoreX(), shipData.getRWCoreZ(), shipData.getDimensionCode(),
				copy(shipData.getSWCore()), shipData.getRadius(), shipData.getName());
	}
}
