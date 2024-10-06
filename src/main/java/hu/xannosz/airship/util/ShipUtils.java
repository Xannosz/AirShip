package hu.xannosz.airship.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.mojang.datafixers.util.Pair;
import hu.xannosz.airship.AirShip;
import hu.xannosz.airship.network.MapData;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.Dimension;
import hu.xannosz.airship.registries.ExternalRegistry;
import hu.xannosz.airship.registries.ShipData;
import hu.xannosz.airship.shipmodels.EnderShipModel;
import hu.xannosz.airship.shipmodels.ShipModel;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static hu.xannosz.airship.util.Constants.REAL_RADIUS;
import static hu.xannosz.airship.util.Constants.SHIP_Y_MIN;


@Slf4j
@UtilityClass
public class ShipUtils {

	private static final Map<Character, Pair<Integer, Integer>> EGA = new HashMap<>();

	static {
		EGA.put('a', new Pair<>(0, 0));
		EGA.put('b', new Pair<>(1, 0));
		EGA.put('c', new Pair<>(2, 0));
		EGA.put('d', new Pair<>(3, 0));
		EGA.put('e', new Pair<>(4, 0));
		EGA.put('f', new Pair<>(5, 0));
		EGA.put('g', new Pair<>(6, 0));
		EGA.put('h', new Pair<>(7, 0));
		EGA.put('i', new Pair<>(8, 0));
		EGA.put('j', new Pair<>(9, 0));
		EGA.put('k', new Pair<>(10, 0));
		EGA.put('l', new Pair<>(11, 0));
		EGA.put('m', new Pair<>(12, 0));
		EGA.put('n', new Pair<>(13, 0));
		EGA.put('o', new Pair<>(14, 0));
		EGA.put('p', new Pair<>(15, 0));
		EGA.put(' ', new Pair<>(16, 0));
		EGA.put('q', new Pair<>(0, 1));
		EGA.put('r', new Pair<>(1, 1));
		EGA.put('s', new Pair<>(2, 1));
		EGA.put('t', new Pair<>(3, 1));
		EGA.put('u', new Pair<>(4, 1));
		EGA.put('v', new Pair<>(5, 1));
		EGA.put('w', new Pair<>(6, 1));
		EGA.put('x', new Pair<>(7, 1));
		EGA.put('y', new Pair<>(8, 1));
		EGA.put('z', new Pair<>(9, 1));
		EGA.put('"', new Pair<>(10, 1));
		EGA.put('-', new Pair<>(11, 1));
		EGA.put('\'', new Pair<>(12, 1));
		//	EGA.put('',new Pair<>(13,1));
		//	EGA.put('',new Pair<>(14,1));
		//	EGA.put('',new Pair<>(15,1));
		EGA.put('0', new Pair<>(0, 2));
		EGA.put('1', new Pair<>(1, 2));
		EGA.put('2', new Pair<>(2, 2));
		EGA.put('3', new Pair<>(3, 2));
		EGA.put('4', new Pair<>(4, 2));
		EGA.put('5', new Pair<>(5, 2));
		EGA.put('6', new Pair<>(6, 2));
		EGA.put('7', new Pair<>(7, 2));
		EGA.put('8', new Pair<>(8, 2));
		EGA.put('9', new Pair<>(9, 2));
		EGA.put('A', new Pair<>(10, 2));
		EGA.put('B', new Pair<>(11, 2));
		EGA.put('C', new Pair<>(12, 2));
		EGA.put('D', new Pair<>(13, 2));
		EGA.put('E', new Pair<>(14, 2));
		EGA.put('F', new Pair<>(15, 2));
		EGA.put('á', new Pair<>(0, 3));
		EGA.put('é', new Pair<>(1, 3));
		EGA.put('í', new Pair<>(2, 3));
		EGA.put('ó', new Pair<>(3, 3));
		EGA.put('ö', new Pair<>(4, 3));
		EGA.put('ő', new Pair<>(5, 3));
		EGA.put('ú', new Pair<>(6, 3));
		EGA.put('ü', new Pair<>(7, 3));
		EGA.put('ű', new Pair<>(8, 3));
	}

	public static boolean isInShipDimension(Level level) {
		return levelToString(level).equals(getShipDimName());
	}

	public static String getShipDimName() {
		return AirShip.MOD_ID + ":ship_dimension";
	}

	public static String levelToString(Level level) {
		return level.dimension().location().toString();
	}

	public static ServerLevel toLevel(String name, ServerLevel level) {
		final ResourceLocation location = ResourceLocation.tryParse(name);
		if (location == null) {
			log.error("Dimension can not found: {}", level);
			throw new CodeShouldNeverReach();
		}
		final ResourceKey<Level> registryKey = ResourceKey.create(Registries.DIMENSION, location);
		return level.getServer().getLevel(registryKey);
	}

	public static int toDimensionCode(Level level) {
		return ExternalRegistry.INSTANCE.getDimensionCode(levelToString(level));
	}

	public static ServerLevel toLevel(int code, Level level) {
		return toLevel(ExternalRegistry.INSTANCE.getDimensionName(code), (ServerLevel) level);
	}

	public static String generateOneLetter() {
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int index = new Random().nextInt(abc.length());
		return abc.substring(index, index + 1);
	}

	public static String generateOneDigit() {
		String digits = "0123456789";
		int index = new Random().nextInt(digits.length());
		return digits.substring(index, index + 1);
	}

	public static String generateOneAlphaNumerical() {
		String symbols = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEF";
		int index = new Random().nextInt(symbols.length());
		return symbols.substring(index, index + 1);
	}

	public static String generateLetters(int num) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < num; i++) {
			result.append(generateOneLetter());
		}
		return result.toString();
	}

	public static String generateDigits(int num) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < num; i++) {
			result.append(generateOneDigit());
		}
		return result.toString();
	}

	public static String generateAlphaNumerical(int num) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < num; i++) {
			result.append(generateOneAlphaNumerical());
		}
		return result.toString();
	}

	public static void writeEGACharacters(String text, int initX, int initY, GuiGraphics guiGraphics) {
		final ResourceLocation texture =
				new ResourceLocation(AirShip.MOD_ID, "textures/gui/ega.png");
		final char[] chars = text.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			guiGraphics.blit(texture, initX + i * 6, initY, 1 + EGA.get(chars[i]).getFirst() * 6,
					7 + EGA.get(chars[i]).getSecond() * 12, 5, 5);
		}
	}

	public static ShipModel getModelForDimension(int dimensionCode) {
		ShipModel model = ExternalRegistry.INSTANCE.getDimension(dimensionCode).model();
		if (model == null) {
			return new EnderShipModel();
		}
		return model;
	}

	public static Pair<String, BlockPos> shouldTeleport(BlockPos block, String dimension) {
		int dim = ExternalRegistry.INSTANCE.getDimensionCode(dimension);

		if (dim == 0) {
			ShipData shipData = AirShipRegistry.INSTANCE.isInShipBorder(block);
			if (shipData != null) {
				return Pair.of(ExternalRegistry.INSTANCE.getDimensionName(shipData.getDimensionCode()), fromShipCoordinates(shipData, shipData.getDimensionCode()));
			} else {
				shipData = AirShipRegistry.INSTANCE.isInShip(block, dim);
				if (shipData == null) {
					Dimension overworld = ExternalRegistry.INSTANCE.getDimension(1);
					return Pair.of(overworld.name(), new BlockPos(0, overworld.referenceY() - REAL_RADIUS - 20, 0));
				}

				return null;
			}
		}

		ShipData shipData = AirShipRegistry.INSTANCE.isInShip(block, dim);
		if (shipData != null) {
			return Pair.of(ExternalRegistry.INSTANCE.getDimensionName(0), toShipCoordinates(shipData));
		}

		return null;
	}

	private static BlockPos toShipCoordinates(ShipData data) {
		return new BlockPos(data.getSWCore().getX(), SHIP_Y_MIN + 60, data.getSWCore().getZ());
	}

	private static BlockPos fromShipCoordinates(ShipData data, int dimCode) {
		final int yPosition = ExternalRegistry.INSTANCE.getDimension(dimCode).referenceY() - REAL_RADIUS - 20;
		return new BlockPos((int) data.getRWCoreX(), yPosition, (int) data.getRWCoreZ());
	}

	public static void fillMapData(Level level, MapData mapData,
								   int scale, int centerX, int centerZ) {
		BlockPos.MutableBlockPos mutableBlockPos1 = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();

		for (int runX = -5; runX < 105; ++runX) {
			double d0 = 0.0;
			for (int runZ = -5; runZ < 105; ++runZ) {
				int j2 = (centerX / scale + runX - 50) * scale;
				int k2 = (centerZ / scale + runZ - 50) * scale;
				Multiset<MapColor> multiset = LinkedHashMultiset.create();
				LevelChunk levelchunk = level.getChunk(SectionPos.blockToSectionCoord(j2), SectionPos.blockToSectionCoord(k2));
				if (!levelchunk.isEmpty()) {
					int l2 = 0;
					double d1 = 0.0;
					int i4;
					if (level.dimensionType().hasCeiling()) {
						i4 = j2 + k2 * 231871;
						i4 = i4 * i4 * 31287121 + i4 * 11;
						if ((i4 >> 20 & 1) == 0) {
							multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(level, BlockPos.ZERO), 10);
						} else {
							multiset.add(Blocks.STONE.defaultBlockState().getMapColor(level, BlockPos.ZERO), 100);
						}

						d1 = 100.0;
					} else {
						for (i4 = 0; i4 < scale; ++i4) {
							for (int j3 = 0; j3 < scale; ++j3) {
								mutableBlockPos1.set(j2 + i4, 0, k2 + j3);
								int k3 = levelchunk.getHeight(Heightmap.Types.WORLD_SURFACE, mutableBlockPos1.getX(), mutableBlockPos1.getZ()) + 1;
								BlockState blockstate;
								if (k3 <= level.getMinBuildHeight() + 1) {
									blockstate = Blocks.BEDROCK.defaultBlockState();
								} else {
									do {
										--k3;
										mutableBlockPos1.setY(k3);
										blockstate = levelchunk.getBlockState(mutableBlockPos1);
									} while (blockstate.getMapColor(level, mutableBlockPos1) == MapColor.NONE && k3 > level.getMinBuildHeight());

									if (k3 > level.getMinBuildHeight() && !blockstate.getFluidState().isEmpty()) {
										int l3 = k3 - 1;
										mutableBlockPos2.set(mutableBlockPos1);

										BlockState blockstate1;
										do {
											mutableBlockPos2.setY(l3--);
											blockstate1 = levelchunk.getBlockState(mutableBlockPos2);
											++l2;
										} while (l3 > level.getMinBuildHeight() && !blockstate1.getFluidState().isEmpty());

										blockstate = getCorrectStateForFluidBlock(level, blockstate, mutableBlockPos1);
									}
								}

								d1 += (double) k3 / (double) (scale * scale);
								multiset.add(blockstate.getMapColor(level, mutableBlockPos1));
							}
						}
					}

					l2 /= scale * scale;
					@SuppressWarnings("UnstableApiUsage")
					MapColor mapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.NONE);
					MapColor.Brightness brightness;
					double d2;
					if (mapcolor == MapColor.WATER) {
						d2 = (double) l2 * 0.1 + (double) (runX + runZ & 1) * 0.2;
						if (d2 < 0.5) {
							brightness = MapColor.Brightness.HIGH;
						} else if (d2 > 0.9) {
							brightness = MapColor.Brightness.LOW;
						} else {
							brightness = MapColor.Brightness.NORMAL;
						}
					} else {
						d2 = (d1 - d0) * 4.0 / (double) (scale + 4) + ((double) (runX + runZ & 1) - 0.5) * 0.4;
						if (d2 > 0.6) {
							brightness = MapColor.Brightness.HIGH;
						} else if (d2 < -0.6) {
							brightness = MapColor.Brightness.LOW;
						} else {
							brightness = MapColor.Brightness.NORMAL;
						}
					}

					d0 = d1;
					mapData.setColor(runX, runZ, mapcolor.getPackedId(brightness));
				}
			}
		}
	}

	private static BlockState getCorrectStateForFluidBlock(Level level, BlockState blockState, BlockPos pos) {
		FluidState fluidstate = blockState.getFluidState();
		return !fluidstate.isEmpty() && !blockState.isFaceSturdy(level, pos, Direction.UP) ? fluidstate.createLegacyBlock() : blockState;
	}
}
