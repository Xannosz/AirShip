package hu.xannosz.airship.shipmodels;

import hu.xannosz.airship.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.util.Arrays;

public class EnderShipModel extends ShipModel {
	public EnderShipModel() {
		blockStates.put('P', Blocks.PURPUR_PILLAR.defaultBlockState()
				.setValue(BlockStateProperties.AXIS, Direction.Axis.Z));
		blockStates.put('p', Blocks.PURPUR_PILLAR.defaultBlockState()
				.setValue(BlockStateProperties.AXIS, Direction.Axis.Y));
		blockStates.put('S', Blocks.PURPUR_STAIRS.defaultBlockState());
		blockStates.put('B', Blocks.PURPUR_BLOCK.defaultBlockState());
		blockStates.put('O', Blocks.OBSIDIAN.defaultBlockState());
		blockStates.put('E', Blocks.END_STONE_BRICKS.defaultBlockState());
		blockStates.put('G', Blocks.PURPLE_STAINED_GLASS.defaultBlockState());
		blockStates.put('t', Blocks.PURPUR_SLAB.defaultBlockState());
		blockStates.put('T', Blocks.PURPUR_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
		blockStates.put('R', Blocks.PURPUR_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.WEST)
				.setValue(StairBlock.HALF, Half.TOP)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('L', Blocks.PURPUR_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.EAST)
				.setValue(StairBlock.HALF, Half.TOP)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('r', Blocks.PURPUR_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.WEST)
				.setValue(StairBlock.HALF, Half.BOTTOM)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('l', Blocks.PURPUR_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.EAST)
				.setValue(StairBlock.HALF, Half.BOTTOM)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('s', Blocks.PURPUR_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.SOUTH)
				.setValue(StairBlock.HALF, Half.BOTTOM)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('1', Blocks.PURPUR_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.NORTH)
				.setValue(StairBlock.HALF, Half.TOP)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('2', Blocks.PURPUR_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.SOUTH)
				.setValue(StairBlock.HALF, Half.TOP)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));

		blockStates.put('C', ModBlocks.COMPASS.get().defaultBlockState());
		blockStates.put('K', ModBlocks.SMALL_SHIP_HELM.get().defaultBlockState());
		blockStates.put('k', ModBlocks.THROTTLE.get().defaultBlockState());

		pivot = new BlockPos(-5, -2, -13);

		writeBlock(Arrays.asList(
						Arrays.asList(
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"           ",
								"           ",
								"           ",
								"           "
						),
						Arrays.asList(
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"    BBB    ",
								"   LBBBR   ",
								"    BBB    ",
								"    BBB    ",
								"    BBB    ",
								"   LBBBR   ",
								"    BBB    ",
								"    BBB    ",
								"    BBB    ",
								"   LBBBR   ",
								"    BBB    ",
								"    BPB    ",
								"    BPB    ",
								"           ",
								"           ",
								"           "
						),
						Arrays.asList(
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"     P     ",
								"     P     ",
								"    BPB    ",
								"    BPB    ",
								"   BBPBB   ",
								"   BBPBB   ",
								"   BOPOB   ",
								"  LBOPOBR  ",
								"   BO OB   ",
								"   BOPOB   ",
								"   BOPOB   ",
								"  LBOPOBR  ",
								"   BOPOB   ",
								"   BOPOB   ",
								"   BOPOB   ",
								"  LBOPOBR  ",
								"   BOPOB   ",
								"   BOPOB   ",
								"   BOPOB   ",
								"  LBBPBBR  ",
								"           ",
								"           "
						),
						Arrays.asList(
								"           ",
								"           ",
								"           ",
								"           ",
								"     P     ",
								"     P     ",
								"    BBB    ",
								"   BBBBB   ",
								"   B   B   ",
								"  B     B  ",
								"  B     B  ",
								"  B     B  ",
								" LB     BR ",
								"  B  p  B  ",
								"  B    sB  ",
								"  B    BB  ",
								" LB    BBR ",
								"  B    BB  ",
								"  B    BB  ",
								"  B    BB  ",
								" LB    BBR ",
								"  B    BB  ",
								"  B    BB  ",
								"  B    BB  ",
								" LBBBBBBBR ",
								"           ",
								"           "
						),
						Arrays.asList(
								"           ",
								"           ",
								"           ",
								"           ",
								"     B     ",
								"    BBB    ",
								"   B   B   ",
								"   B   B   ",
								"  B     B  ",
								"  B     B  ",
								" B       B ",
								" B       B ",
								" B       B ",
								" B   p   B ",
								" B       B ",
								" B       B ",
								" B       B ",
								" B       B ",
								" B     ssB ",
								" B     BBB ",
								" B     BBB ",
								" B     BBB ",
								" B     BBB ",
								" B     BBB ",
								" BBBBBBBBB ",
								" 1   1   1 ",
								"           "
						),
						Arrays.asList(
								"           ",
								"           ",
								"           ",
								"     P     ",
								"     P     ",
								"    BBB    ",
								"   B   B   ",
								"   B   B   ",
								"  B     B  ",
								"  B     B  ",
								" B       B ",
								" B       B ",
								"LB       BR",
								" B   p   B ",
								" B       B ",
								"LB       BR",
								" B       B ",
								" B       B ",
								" B       B ",
								"LBBBBBBssBR",
								"LBBBBBBBBBR",
								"lBBBBBBBBBr",
								"LBBBBBBBBBR",
								"LBBBBBBBBBR",
								"lBBBBBBBBBr",
								"LBBBBBBBBBR",
								"T1S1S1S1S1T"
						),
						Arrays.asList(
								"           ",
								"           ",
								"     P     ",
								"     P     ",
								"     B     ",
								"    B B    ",
								"   B   B   ",
								"   B   B   ",
								"  B     B  ",
								"  B     B  ",
								" B       B ",
								" B       B ",
								"           ",
								" B   p   B ",
								" B       B ",
								"           ",
								" B       B ",
								" B       B ",
								" BBBBBB  B ",
								" BBSSBB  B ",
								" pB  BB  p ",
								" E       E ",
								" E       E ",
								" E       E ",
								" E   p   E ",
								" pEEEpEEEp ",
								"           "
						),
						Arrays.asList(
								"     P     ",
								"     P     ",
								"     P     ",
								"     P     ",
								"    BBB    ",
								"   BBBBB   ",
								"   BBBBB   ",
								"  BBBBBBB  ",
								"  BBBBBBB  ",
								" BBBBBBBBB ",
								" BBBBBBBBB ",
								" BBBBPBBBB ",
								" BBBBGBBBB ",
								" BBBBPBBBB ",
								" BBBBBBBBB ",
								" BBBBBBBBB ",
								" BBBBBBBBB ",
								" BBBBBBBBB ",
								" BBSSBB  B ",
								" BB  BB  B ",
								" pB  BB  p ",
								" E       E ",
								"           ",
								"           ",
								" E   p   E ",
								" pE EpE Ep ",
								"           "
						),
						Arrays.asList(
								"     S     ",
								"           ",
								"           ",
								"     B     ",
								"    B B    ",
								"   B   B   ",
								"   l   r   ",
								"  Bl   rB  ",
								"  l     r  ",
								" Bl  P  rB ",
								" l   P   r ",
								" l   S   r ",
								" B       B ",
								" l   p   r ",
								" l       r ",
								" B       B ",
								" l       r ",
								" l       r ",
								" B     ssB ",
								" B     BBB ",
								" pB  BBBBp ",
								" E       E ",
								" E       E ",
								" E       E ",
								" E       E ",
								" pEEEpEEEp ",
								"           "
						),
						Arrays.asList(
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"     P     ",
								"     P     ",
								"     S     ",
								"           ",
								"           ",
								"           ",
								"     p     ",
								"           ",
								"           ",
								"           ",
								"           ",
								" s       s ",
								"BBB  BBssBB",
								"BBBBBBBBBBB",
								"BBBBBBBBBBB",
								"BBBBBBBBBBB",
								"BBBBBBBBBBB",
								"BBBBBBBBBBB",
								"BBBBBBBBBBB",
								"11111111111"
						),
						Arrays.asList(
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"     P     ",
								"     P     ",
								"     S     ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"     p     ",
								"           ",
								"           ",
								"           ",
								"           ",
								"     2     ",
								"BsB22KB  BB",
								"l         r",
								"l   ttt   r",
								"l   ttt   r",
								"l         r",
								"l         r",
								"l         r",
								"BSSBSSSBSSB"
						),
						Arrays.asList(
								"           ",
								"           ",
								"           ",
								"     P     ",
								"     P     ",
								"     S     ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"     p     ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"    C k    ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           "
						),
						Arrays.asList(
								"     T     ",
								"     P     ",
								"     P     ",
								"     S     ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"     p     ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           ",
								"           "
						)
				)
		);
	}
}
