package hu.xannosz.airship.block;

import hu.xannosz.airship.AirShip;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static hu.xannosz.airship.item.ModItems.BLOCK_ITEMS;
import static hu.xannosz.airship.item.ModItems.ITEMS;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AirShip.MOD_ID);

	public static final RegistryObject<Block> CORE = registerBlock("core", Core::new);
	public static final RegistryObject<Block> SHIP_HELM = registerBlock("ship_helm", ShipHelm::new);
	public static final RegistryObject<Block> SMALL_SHIP_HELM = registerBlock("small_ship_helm", SmallShipHelm::new);
	public static final RegistryObject<Block> THROTTLE = registerBlock("throttle", Throttle::new);
	public static final RegistryObject<Block> COMPASS = registerBlock("compass", Compass::new);
	public static final RegistryObject<Block> SHIP_GENERATOR = registerBlock("ship_generator", ShipGenerator::new);
	public static final RegistryObject<Block> ENDER_CRYSTAL = registerBlock("ender_crystal", EnderCrystal::new);
	public static final RegistryObject<Block> ENDER_CRYSTAL_HOLDER = registerBlock("ender_crystal_holder", EnderCrystalHolder::new);
	public static final RegistryObject<Block> ENDER_ENGINE_BUTTON = registerBlock("ender_engine_button", EnderEngineButton::new);
	public static final RegistryObject<Block> ENDER_ENGINE_GAUGE = registerBlock("ender_engine_gauge", EnderEngineGauge::new);

	public static final RegistryObject<Block> YELLOW_LOOMER = registerBlock("yellow_loomer", Loomer::new);
	public static final RegistryObject<Block> NAVIGATION_TABLE = registerBlock("navigation_table", NavigationTable::new);
	public static final RegistryObject<Block> SMALL_RUNE = registerBlock("small_rune", SmallRune::new);
	public static final RegistryObject<Block> ONE_BLOCK_JUMPER = registerBlock("one_block_jumper", OneBlockJumper::new);

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blockCreator) {
		RegistryObject<T> block = BLOCKS.register(name, blockCreator);
		registerBlockItem(name, block);
		return block;
	}

	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
		BLOCK_ITEMS.add(ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties())));
	}
}
