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

public class OverworldShipModel extends ShipModel {
	public OverworldShipModel() {
		blockStates.put('P', Blocks.SPRUCE_LOG.defaultBlockState()
				.setValue(BlockStateProperties.AXIS, Direction.Axis.Z));
		blockStates.put('p', Blocks.SPRUCE_LOG.defaultBlockState()
				.setValue(BlockStateProperties.AXIS, Direction.Axis.Y));
		blockStates.put('S', Blocks.BIRCH_STAIRS.defaultBlockState());
		blockStates.put('B', Blocks.BIRCH_PLANKS.defaultBlockState());
		blockStates.put('O', Blocks.ACACIA_LOG.defaultBlockState());
		blockStates.put('E', Blocks.ACACIA_PLANKS.defaultBlockState());
		blockStates.put('G', Blocks.GLASS.defaultBlockState());
		blockStates.put('t', Blocks.BIRCH_SLAB.defaultBlockState());
		blockStates.put('T', Blocks.BIRCH_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
		blockStates.put('3', Blocks.DARK_OAK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));
		blockStates.put('4', Blocks.DARK_OAK_STAIRS.defaultBlockState());
		blockStates.put('R', Blocks.BIRCH_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.WEST)
				.setValue(StairBlock.HALF, Half.TOP)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('L', Blocks.BIRCH_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.EAST)
				.setValue(StairBlock.HALF, Half.TOP)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('r', Blocks.BIRCH_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.WEST)
				.setValue(StairBlock.HALF, Half.BOTTOM)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('l', Blocks.BIRCH_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.EAST)
				.setValue(StairBlock.HALF, Half.BOTTOM)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('s', Blocks.BIRCH_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.SOUTH)
				.setValue(StairBlock.HALF, Half.BOTTOM)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('1', Blocks.BIRCH_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.NORTH)
				.setValue(StairBlock.HALF, Half.TOP)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('2', Blocks.BIRCH_STAIRS.defaultBlockState()
				.setValue(StairBlock.FACING, Direction.SOUTH)
				.setValue(StairBlock.HALF, Half.TOP)
				.setValue(StairBlock.SHAPE, StairsShape.STRAIGHT));
		blockStates.put('X', Blocks.WHITE_WOOL.defaultBlockState());
		blockStates.put('I', Blocks.OAK_FENCE.defaultBlockState());

		blockStates.put('C', ModBlocks.COMPASS.get().defaultBlockState());
		blockStates.put('K', ModBlocks.SMALL_SHIP_HELM.get().defaultBlockState());
		blockStates.put('k', ModBlocks.THROTTLE.get().defaultBlockState());

		pivot = new BlockPos(-7, -2, -15);

		writeBlock(Arrays.asList(
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"      BBB      ",
								"     LBBBR     ",
								"      BBB      ",
								"      BBB      ",
								"      BBB      ",
								"     LBBBR     ",
								"      BBB      ",
								"      BBB      ",
								"      BBB      ",
								"     LBBBR     ",
								"      BBB      ",
								"      BPB      ",
								"      BPB      ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       P       ",
								"       P       ",
								"      BPB      ",
								"      BPB      ",
								"     BBPBB     ",
								"     BBPBB     ",
								"     BOPOB     ",
								"    LBOPOBR    ",
								"     BO OB     ",
								"     BOPOB     ",
								"     BOPOB     ",
								"    LBOPOBR    ",
								"     BOPOB     ",
								"     BOPOB     ",
								"     BOPOB     ",
								"    LBOPOBR    ",
								"     BOPOB     ",
								"     BOPOB     ",
								"     BOPOB     ",
								"    LBBPBBR    ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       P       ",
								"       P       ",
								"      BBB      ",
								"     BBBBB     ",
								"     B   B     ",
								"    B     B    ",
								"    B     B    ",
								"    B     B    ",
								"   LB     BR   ",
								"    B  p  B    ",
								"    B    sB    ",
								"    B    BB    ",
								"   LB    BBR   ",
								"    B    BB    ",
								"    B    BB    ",
								"    B    BB    ",
								"   LB    BBR   ",
								"    B    BB    ",
								"    B    BB    ",
								"    B    BB    ",
								"   LBBBBBBBR   ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       B       ",
								"      BBB      ",
								"     B   B     ",
								"     B   B     ",
								"    B     B    ",
								"    B     B    ",
								"   B       B   ",
								"   B       B   ",
								"   B       B   ",
								"   B   p   B   ",
								"   B       B   ",
								"   B       B   ",
								"   B       B   ",
								"   B       B   ",
								"   B     ssB   ",
								"   B     BBB   ",
								"   B     BBB   ",
								"   B     BBB   ",
								"   B     BBB   ",
								"   B     BBB   ",
								"   BBBBBBBBB   ",
								"   1   1   1   ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       P       ",
								"       P       ",
								"      BBB      ",
								"     B   B     ",
								"     B   B     ",
								"    B     B    ",
								"    B     B    ",
								"   B       B   ",
								"   B       B   ",
								"  LB       BR  ",
								"   B   p   B   ",
								"   B       B   ",
								"  LB       BR  ",
								"   B       B   ",
								"   B       B   ",
								"   B       B   ",
								"  LBBBBBBssBR  ",
								"  LBBBBBBBBBR  ",
								"  lBBBBBBBBBr  ",
								"  LBBBBBBBBBR  ",
								"  LBBBBBBBBBR  ",
								"  lBBBBBBBBBr  ",
								"  LBBBBBBBBBR  ",
								"  T1S1S1S1S1T  ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"       P       ",
								"       P       ",
								"       B       ",
								"      B B      ",
								"     B   B     ",
								"     B   B     ",
								"    B     B    ",
								"    B     B    ",
								"   B       B   ",
								"   B       B   ",
								"               ",
								"   B   p   B   ",
								"   B       B   ",
								"               ",
								"   B       B   ",
								"   B       B   ",
								"   BBBBBB  B   ",
								"   BBSSBB  B   ",
								"   pB  BB  p   ",
								"   E       E   ",
								"   E       E   ",
								"   E       E   ",
								"   E   p   E   ",
								"   pEEEpEEEp   ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"       P       ",
								"       P       ",
								"       P       ",
								"       P       ",
								"      BBB      ",
								"     BBBBB     ",
								"     BBBBB     ",
								"    BBBBBBB    ",
								"    BBBBBBB    ",
								"   BBBBBBBBB   ",
								"   BBBBBBBBB   ",
								"   BBBBPBBBB   ",
								"   BBBBGBBBB   ",
								"   BBBBPBBBB   ",
								"   BBBBBBBBB   ",
								"   BBBBBBBBB   ",
								"   BBBBBBBBB   ",
								"   BBBBBBBBB   ",
								"   BBSSBB  B   ",
								"   BB  BB  B   ",
								"   pB  BB  p   ",
								"   E       E   ",
								"               ",
								"               ",
								"   E   p   E   ",
								"   pE EpE Ep   ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"       4       ",
								"               ",
								"               ",
								"       B       ",
								"      B B      ",
								"     B   B     ",
								"     l   r     ",
								"    Bl   rB    ",
								"    l     r    ",
								"   Bl  P  rB   ",
								"   l   P   r   ",
								"   l   4   r   ",
								"   B       B   ",
								"   l   p   r   ",
								"   l       r   ",
								"   B       B   ",
								"   l       r   ",
								"   l       r   ",
								"   B     ssB   ",
								"   B     BBB   ",
								"   pB  BBBBp   ",
								"   E       E   ",
								"   E       E   ",
								"   E       E   ",
								"   E       E   ",
								"   pEEEpEEEp   ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"    I  P  I    ",
								"       P       ",
								"       4       ",
								"               ",
								"               ",
								"   I       I   ",
								"       p       ",
								"               ",
								"               ",
								"               ",
								"               ",
								"   s       s   ",
								"  BBB  BBssBB  ",
								"  BBBBBBBBBBB  ",
								"  BBBBBBBBBBB  ",
								"  BBBBBBBBBBB  ",
								"  BBBBBBBBBBB  ",
								"  BBBBBBBBBBB  ",
								"  BBBBBBBBBBB  ",
								"  11111111111  ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       P       ",
								"       P       ",
								"    I  4  I    ",
								"               ",
								"               ",
								"               ",
								"               ",
								"   I       I   ",
								"       p       ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       2       ",
								"  BBB22KB  BB  ",
								"  l         r  ",
								"  l   ttt   r  ",
								"  l   ttt   r  ",
								"  l         r  ",
								"  l         r  ",
								"  l         r  ",
								"  BSSBSSSBSSB  ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       P       ",
								"       P       ",
								"       4       ",
								"               ",
								"    I     I    ",
								"               ",
								"               ",
								"               ",
								"               ",
								"   I       I   ",
								"       p       ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"   I  C k  I   ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"     I   I     ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"       3       ",
								"       P       ",
								"       P       ",
								"       4       ",
								"               ",
								"               ",
								"               ",
								"    I XXX I    ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"   I XXXXX I   ",
								"     XXpXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"      XXX      ",
								"   I       I   ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"     I   I     ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"       I       ",
								"               ",
								"               ",
								"       X       ",
								"      XXX      ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXpXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"     XXXXX     ",
								"       X       ",
								"     I   I     ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"       I       ",
								"       X       ",
								"     XXXXX     ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXpXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"     XXXXX     ",
								"       X       ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"      XXX      ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXpXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"      XXX      ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"      XXX      ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXpXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"      XXX      ",
								"               "
						),
						Arrays.asList(
								"       X       ",
								"     XXXXX     ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXpXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"     XXXXX     ",
								"       X       "
						),
						Arrays.asList(
								"      XXX      ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXpXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"      XXX      "
						),
						Arrays.asList(
								"     XXXXX     ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXpXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"     XXXXX     "
						),
						Arrays.asList(
								"      XXX      ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXpXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"      XXX      "
						),
						Arrays.asList(
								"       X       ",
								"     XXXXX     ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXpXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								"XXXXXXXXXXXXXXX",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"     XXXXX     ",
								"       X       "
						),
						Arrays.asList(
								"               ",
								"      XXX      ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXpXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"      XXX      ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"      XXX      ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXpXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								" XXXXXXXXXXXXX ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"      XXX      ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"       X       ",
								"     XXXXX     ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXpXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"  XXXXXXXXXXX  ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"     XXXXX     ",
								"       X       ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       X       ",
								"      XXX      ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXpXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"   XXXXXXXXX   ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"    XXXXXXX    ",
								"     XXXXX     ",
								"       X       ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"      XXX      ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXpXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"     XXXXX     ",
								"      XXX      ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						),
						Arrays.asList(
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"       p       ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               ",
								"               "
						)

				)
		);
	}
}
