package hu.xannosz.airship.blockentity;

import hu.xannosz.airship.AirShip;
import hu.xannosz.airship.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("ConstantConditions")
public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AirShip.MOD_ID);

	public static final RegistryObject<BlockEntityType<CoreBlockEntity>> CORE_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("core_block_entity", () ->
					BlockEntityType.Builder.of(
							CoreBlockEntity::new,
							ModBlocks.CORE.get()).build(null));

	public static final RegistryObject<BlockEntityType<RudderBlockEntity>> RUDDER_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("rudder_block_entity", () ->
					BlockEntityType.Builder.of(
							RudderBlockEntity::new,
							ModBlocks.RUDDER.get()).build(null));

	public static final RegistryObject<BlockEntityType<SmallRudderBlockEntity>> SMALL_RUDDER_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("small_rudder_block_entity", () ->
					BlockEntityType.Builder.of(
							SmallRudderBlockEntity::new,
							ModBlocks.SMALL_RUDDER.get()).build(null));

	public static final RegistryObject<BlockEntityType<ThrottleBlockEntity>> THROTTLE_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("throttle_block_entity", () ->
					BlockEntityType.Builder.of(
							ThrottleBlockEntity::new,
							ModBlocks.THROTTLE.get()).build(null));

	public static final RegistryObject<BlockEntityType<CompassBlockEntity>> COMPASS_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("compass_block_entity", () ->
					BlockEntityType.Builder.of(
							CompassBlockEntity::new,
							ModBlocks.COMPASS.get()).build(null));

	public static final RegistryObject<BlockEntityType<ShipGeneratorBlockEntity>> SHIP_GENERATOR_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("ship_generator_block_entity", () ->
					BlockEntityType.Builder.of(
							ShipGeneratorBlockEntity::new,
							ModBlocks.SHIP_GENERATOR.get()).build(null));

	public static final RegistryObject<BlockEntityType<EnderCrystalHolderBlockEntity>> ENDER_CRYSTAL_HOLDER_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("ender_crystal_holder_block_entity", () ->
					BlockEntityType.Builder.of(
							EnderCrystalHolderBlockEntity::new,
							ModBlocks.ENDER_CRYSTAL_HOLDER.get()).build(null));

	public static final RegistryObject<BlockEntityType<EnderEngineButtonBlockEntity>> ENDER_ENGINE_BUTTON_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("ender_engine_button_block_entity", () ->
					BlockEntityType.Builder.of(
							EnderEngineButtonBlockEntity::new,
							ModBlocks.ENDER_ENGINE_BUTTON.get()).build(null));

	public static final RegistryObject<BlockEntityType<EnderEngineGaugeBlockEntity>> ENDER_ENGINE_GAUGE_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("ender_engine_gauge_block_entity", () ->
					BlockEntityType.Builder.of(
							EnderEngineGaugeBlockEntity::new,
							ModBlocks.ENDER_ENGINE_GAUGE.get()).build(null));

	public static final RegistryObject<BlockEntityType<NavigationTableBlockEntity>> NAVIGATION_TABLE_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("navigation_table_block_entity", () ->
					BlockEntityType.Builder.of(
							NavigationTableBlockEntity::new,
							ModBlocks.NAVIGATION_TABLE.get()).build(null));

	public static final RegistryObject<BlockEntityType<SmallRuneBlockEntity>> SMALL_RUNE_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("small_rune_block_entity", () ->
					BlockEntityType.Builder.of(
							SmallRuneBlockEntity::new,
							ModBlocks.SMALL_RUNE.get()).build(null));

	public static final RegistryObject<BlockEntityType<OneBlockJumperBlockEntity>> ONE_BLOCK_JUMPER_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("one_block_jumper_block_entity", () ->
					BlockEntityType.Builder.of(
							OneBlockJumperBlockEntity::new,
							ModBlocks.ONE_BLOCK_JUMPER.get()).build(null));
}
