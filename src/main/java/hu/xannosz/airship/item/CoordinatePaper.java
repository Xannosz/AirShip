package hu.xannosz.airship.item;

import net.minecraft.world.item.Item;

public class CoordinatePaper extends Item {
	public static final String DIM_TAG = "real_dim";
	public static final String X_TAG = "real_x";
	public static final String Z_TAG = "real_z";

	public CoordinatePaper() {
		super(new Item.Properties().stacksTo(1));
	}
}
