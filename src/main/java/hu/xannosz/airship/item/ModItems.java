package hu.xannosz.airship.item;

import hu.xannosz.airship.AirShip;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AirShip.MOD_ID);
	public static final RegistryObject<Item> SHIP_DETECTOR = ITEMS.register("ship_detector", ShipDetector::new);
	public static final RegistryObject<Item> COORDINATE_PAPER = ITEMS.register("coordinate_paper", CoordinatePaper::new);
	public static final RegistryObject<Item> RUNE_STONE = ITEMS.register("rune_stone", RuneStone::new);
	public static final Set<RegistryObject<BlockItem>> BLOCK_ITEMS = new HashSet<>();
}
