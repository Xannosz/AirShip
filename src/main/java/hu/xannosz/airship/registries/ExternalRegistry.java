package hu.xannosz.airship.registries;

import hu.xannosz.airship.AirShip;
import hu.xannosz.airship.item.CoordinatePaper;
import hu.xannosz.airship.item.ModItems;
import hu.xannosz.airship.shipmodels.OverworldShipModel;
import hu.xannosz.airship.util.CodeShouldNeverReach;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExternalRegistry {
	public static ExternalRegistry INSTANCE = create();

	private final List<Dimension> dimensions = new ArrayList<>();
	private final List<CoordinateHolder> coordinateHolders = new ArrayList<>();

	private static ExternalRegistry create() {
		ExternalRegistry reg = new ExternalRegistry();

		reg.addDimension(new Dimension(0, AirShip.MOD_ID + ":ship_dimension", null, 0));
		reg.addDimension(new Dimension(1, "minecraft:overworld", new OverworldShipModel(), 550));

		reg.addCoordinateHolder(new CoordinateHolder() {
			@Override
			public boolean isValidItem(ItemStack stack) {
				return stack.getItem().equals(ModItems.COORDINATE_PAPER.get());
			}

			@Override
			public Coordinate getCoordinate(ItemStack stack, ServerLevel level) {
				return new Coordinate(stack.getOrCreateTag().getInt(CoordinatePaper.X_TAG),
						stack.getOrCreateTag().getInt(CoordinatePaper.Z_TAG),
						stack.getOrCreateTag().getString(CoordinatePaper.DIM_TAG));
			}
		});
		reg.addCoordinateHolder(new CoordinateHolder() {
			@Override
			public boolean isValidItem(ItemStack stack) {
				return stack.getItem().equals(Items.FILLED_MAP);
			}

			@Override
			public Coordinate getCoordinate(ItemStack stack, ServerLevel level) {
				MapItemSavedData data = MapItem.getSavedData(stack, level);

				if (data == null) {
					return null;
				}
				return new Coordinate(data.centerX, data.centerZ, data.dimension.location().toString());
			}
		});

		return reg;
	}

	public void addDimension(Dimension dimension) {
		dimensions.add(dimension);
	}

	public void addCoordinateHolder(CoordinateHolder coordinateHolder) {
		coordinateHolders.add(coordinateHolder);
	}

	public int getDimensionCode(String dimension) {
		for (Dimension dim : dimensions) {
			if (dim.name().equals(dimension)) {
				return dim.key();
			}
		}
		throw new CodeShouldNeverReach();
	}

	public String getDimensionName(int dimension) {
		for (Dimension dim : dimensions) {
			if (dim.key() == dimension) {
				return dim.name();
			}
		}
		throw new CodeShouldNeverReach();
	}

	public Dimension getDimension(int dimensionCode) {
		for (Dimension dim : dimensions) {
			if (dim.key() == dimensionCode) {
				return dim;
			}
		}
		throw new CodeShouldNeverReach();
	}

	public boolean isCoordinateHolder(ItemStack stack) {
		for (CoordinateHolder coordinateHolder : coordinateHolders) {
			if (coordinateHolder.isValidItem(stack)) {
				return true;
			}
		}
		return false;
	}

	public Coordinate getCoordinate(ItemStack stack, ServerLevel level) {
		for (CoordinateHolder coordinateHolder : coordinateHolders) {
			if (coordinateHolder.isValidItem(stack)) {
				return coordinateHolder.getCoordinate(stack, level);
			}
		}
		return null;
	}
}
