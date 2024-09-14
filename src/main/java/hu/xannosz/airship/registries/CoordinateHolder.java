package hu.xannosz.airship.registries;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public interface CoordinateHolder {
	boolean isValidItem(ItemStack stack);

	Coordinate getCoordinate(ItemStack stack, ServerLevel level);
}
