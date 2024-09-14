package hu.xannosz.airship.dimension;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.Collections;
import java.util.Optional;
import java.util.OptionalLong;

import static hu.xannosz.airship.AirShip.MOD_ID;

public class ModDimensions {
	public static final ResourceKey<LevelStem> SHIP_DIMENSION_KEY = ResourceKey.create(Registries.LEVEL_STEM,
			new ResourceLocation(MOD_ID, "ship_dimension"));
	public static final ResourceKey<Level> SHIP_DIMENSION_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
			new ResourceLocation(MOD_ID, "ship_dimension"));
	public static final ResourceKey<DimensionType> SHIP_DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
			new ResourceLocation(MOD_ID, "ship_dimension_type"));

	public static void bootstrapType(BootstapContext<DimensionType> context) {
		context.register(SHIP_DIMENSION_TYPE, new DimensionType(
				OptionalLong.of(12000), // fixedTime
				false, // hasSkylight
				false, // hasCeiling
				false, // ultraWarm
				false, // natural
				1.0, // coordinateScale
				true, // bedWorks
				false, // respawnAnchorWorks
				-64, // minY
				480, // height
				480, // logicalHeight
				BlockTags.INFINIBURN_OVERWORLD, // infiniburn
				new ResourceLocation(MOD_ID, "ship_dimension"), // effectsLocation
				1.0f, // ambientLight
				new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
	}

	public static void bootstrapStem(BootstapContext<LevelStem> context) {
		HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
		HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);

		FlatLevelSource wrappedChunkGenerator =
				new FlatLevelSource(new FlatLevelGeneratorSettings(
						Optional.empty(),
						biomeRegistry.getOrThrow(Biomes.THE_VOID),
						Collections.emptyList()
				));

		LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.SHIP_DIMENSION_TYPE), wrappedChunkGenerator);

		context.register(SHIP_DIMENSION_KEY, stem);
	}
}
