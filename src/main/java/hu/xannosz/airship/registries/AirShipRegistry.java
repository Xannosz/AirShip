package hu.xannosz.airship.registries;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static hu.xannosz.airship.util.Config.DEFAULT_SHIP_RADIUS;
import static hu.xannosz.airship.util.Config.GENERATION_PERTURBATION;
import static hu.xannosz.airship.util.Constants.*;
import static hu.xannosz.airship.util.ShipUtils.toDimensionCode;

@Slf4j
public class AirShipRegistry extends SavedData {

	public static AirShipRegistry INSTANCE = null;

	private static final int BORDER_SIZE = 1000;
	private static final int SHIP_CORE_Y_POSITION = 150;

	private final List<ShipData> ships = new ArrayList<>();
	private double spiralEnd = 0;

	public BlockPos getNewCorePosition(BlockPos generationPosition, Level dimension, int radius) {
		BlockPos corePosition = getFreePosition(radius);

		ships.add(new ShipData(
				generationPosition.getX() + new Random().nextInt(-GENERATION_PERTURBATION, GENERATION_PERTURBATION),
				generationPosition.getZ() + new Random().nextInt(-GENERATION_PERTURBATION, GENERATION_PERTURBATION),
				toDimensionCode(dimension), corePosition, radius));
		setDirty();
		return corePosition;
	}

	public ShipData isInShip(BlockPos block, int dim) {
		if (dim != 0) {
			Dimension dimension = ExternalRegistry.INSTANCE.getDimension(dim);
			if (block.getY() > dimension.referenceY() + REAL_RADIUS ||
					block.getY() < dimension.referenceY() - REAL_RADIUS) {
				return null;
			}
		}
		for (ShipData ship : ships) {
			if (isInShip(block, dim, ship)) {
				return ship;
			}
		}
		return null;
	}

	public List<ShipData> getShipsInRadius(BlockPos block, int dim, int radius) {
		List<ShipData> result = new ArrayList<>();
		for (ShipData data : ships) {
			if (data.getDimensionCode() == dim && inRadius(block, (int) data.getRWCoreX(), (int) data.getRWCoreZ(), radius)) {
				result.add(data);
			}
		}
		return result;
	}

	public void updatePosition(double x, double z, BlockPos block) {
		ShipData shipData = isInShip(block, 0);
		shipData.updateCoordinates(x, z);
		setDirty();
	}

	public void updateDimension(double x, double z, int dim, BlockPos block) {
		ShipData shipData = isInShip(block, 0);
		shipData.updateDimension(x, z, dim);
		setDirty();
	}

	public ShipData isInShipBorder(BlockPos block) {
		for (ShipData ship : ships) {
			if (isInShipBorder(block, ship)) {
				return ship;
			}
		}
		return null;
	}

	@Override
	public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
		ListTag shipList = new ListTag();
		for (ShipData shipData : ships) {
			CompoundTag itemTag = new CompoundTag();
			itemTag.putDouble("rWCoreX", shipData.getRWCoreX());
			itemTag.putDouble("rWCoreZ", shipData.getRWCoreZ());
			itemTag.putInt("dimensionCode", shipData.getDimensionCode());
			itemTag.putInt("sWCore.x", shipData.getSWCore().getX());
			itemTag.putInt("sWCore.y", shipData.getSWCore().getY());
			itemTag.putInt("sWCore.z", shipData.getSWCore().getZ());
			itemTag.putInt("radius", shipData.getRadius());
			shipList.add(itemTag);
		}
		tag.put("ships", shipList);
		tag.putDouble("spiralEnd", spiralEnd);
		return tag;
	}

	public static AirShipRegistry create() {
		if (INSTANCE == null) {
			INSTANCE = new AirShipRegistry();
		}
		return INSTANCE;
	}

	@SuppressWarnings("ConstantConditions")
	public static AirShipRegistry load(CompoundTag tag) {
		AirShipRegistry registry = create();
		ListTag shipList = (ListTag) tag.get("ships");
		for (int i = 0; i < shipList.size(); i++) {
			CompoundTag sTag = shipList.getCompound(i);
			ShipData shipData = new ShipData();
			shipData.setRWCoreX(sTag.getDouble("rWCoreX"));
			shipData.setRWCoreZ(sTag.getDouble("rWCoreZ"));
			shipData.setDimensionCode(sTag.getInt("dimensionCode"));
			shipData.setSWCore(new BlockPos(sTag.getInt("sWCore.x"),
					sTag.getInt("sWCore.y"),
					sTag.getInt("sWCore.z")));
			shipData.setRadius(sTag.getInt("radius"));
			registry.ships.add(shipData);
		}
		registry.spiralEnd = tag.getDouble("spiralEnd");
		return registry;
	}

	private boolean isInShip(BlockPos block, int dim, ShipData shipData) {
		if (dim == 0) {
			if (block.getY() > SHIP_Y_MAX || block.getY() < SHIP_Y_MIN) {
				return false;
			}
			return inRadius(block, shipData.getSWCore().getX(), shipData.getSWCore().getZ(), DEFAULT_SHIP_RADIUS);
		}
		if (dim != shipData.getDimensionCode()) {
			return false;
		}
		return inRadius(block, (int) shipData.getRWCoreX(), (int) shipData.getRWCoreZ(), REAL_RADIUS);
	}

	private boolean isInShipBorder(BlockPos block, ShipData shipData) {
		if (inRadius(block, shipData.getSWCore().getX(), shipData.getSWCore().getZ(), DEFAULT_SHIP_RADIUS)) {
			return block.getY() > SHIP_Y_MAX || block.getY() < SHIP_Y_MIN;
		}
		return inRadius(block, shipData.getSWCore().getX(), shipData.getSWCore().getZ(),
				DEFAULT_SHIP_RADIUS + BORDER_SIZE);
	}

	private BlockPos getFreePosition(int radius) {
		out:
		while (true) {
			BlockPos possiblePosition =
					new BlockPos((int) Math.round(spiralEnd * Math.cos(spiralEnd)),
							SHIP_CORE_Y_POSITION,
							(int) Math.round(spiralEnd * Math.sin(spiralEnd)));
			spiralEnd += 0.01;

			for (ShipData shipData : ships) {
				if (inRadius(possiblePosition, shipData.getSWCore().getX(), shipData.getSWCore().getZ(), shipData.getRadius() + radius + BORDER_SIZE * 2)) {
					continue out;
				}
			}

			setDirty();
			return possiblePosition;
		}
	}

	private static boolean inRadius(BlockPos block, int x, int z, int radius) {
		if (block.getX() > x - radius && block.getX() < x + radius) {
			return block.getZ() > z - radius && block.getZ() < z + radius;
		}
		return false;
	}
}
