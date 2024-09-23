package hu.xannosz.airship.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import hu.xannosz.airship.block.ModBlocks;
import hu.xannosz.airship.item.ModItems;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ShipData;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static hu.xannosz.airship.item.ShipDetector.CORE_POSITION_TAG;
import static hu.xannosz.airship.registries.AirShipRegistry.SHIP_CORE_Y_POSITION;
import static hu.xannosz.airship.util.Config.DEFAULT_SHIP_RADIUS;
import static hu.xannosz.airship.util.RuneUtil.searchForSafeLandPosition;
import static hu.xannosz.airship.util.RuneUtil.useRune;
import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;
import static hu.xannosz.airship.util.ShipUtils.toLevel;

@Slf4j
public class AirshipCommand {
	public AirshipCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("airship")
				.then(Commands.literal("list")
						.then(Commands.argument("name", StringArgumentType.string()).executes(
								(context -> list(context.getSource(), context.getArgument("name", String.class)))
						)).executes(context -> list(context.getSource(), "")))
				.then(Commands.literal("get")
						.then(Commands.argument("name", StringArgumentType.string()).executes(
								(context -> get(context.getSource(), context.getArgument("name", String.class)))
						)))
				.then(Commands.literal("warp")
						.then(Commands.argument("dim", IntegerArgumentType.integer())
								.then(Commands.argument("x", IntegerArgumentType.integer())
										.then(Commands.argument("z", IntegerArgumentType.integer())
												.executes(
														(context -> warp(context.getSource(),
																context.getArgument("dim", Integer.class),
																context.getArgument("x", Integer.class),
																context.getArgument("z", Integer.class)
														))
												)))))
				.then(Commands.literal("compass")
						.then(Commands.argument("name", StringArgumentType.string()).executes(
								(context -> createCompass(context.getSource(), context.getArgument("name", String.class), 0, 0))
						))
						.then(Commands.argument("x", IntegerArgumentType.integer())
								.then(Commands.argument("z", IntegerArgumentType.integer()).executes(
												(context -> createCompass(context.getSource(), null,
														context.getArgument("x", Integer.class),
														context.getArgument("z", Integer.class)
												))
										)
								)))
				.then(Commands.literal("tp")
						.then(Commands.argument("name", StringArgumentType.string()).executes(
								(context -> tp(context.getSource(), context.getArgument("name", String.class), 0, 0))
						))
						.then(Commands.argument("x", IntegerArgumentType.integer())
								.then(Commands.argument("z", IntegerArgumentType.integer()).executes(
												(context -> tp(context.getSource(), null,
														context.getArgument("x", Integer.class),
														context.getArgument("z", Integer.class)
												))
										)
								))
				)
				.then(Commands.literal("create")
						.then(Commands.argument("radius", IntegerArgumentType.integer()).executes(
								(context -> createShip(context.getSource(), context.getArgument("radius", Integer.class)))
						)).executes(context -> createShip(context.getSource(), DEFAULT_SHIP_RADIUS)))
		);
	}

	private int list(CommandSourceStack source, String name) {
		AirShipRegistry.INSTANCE.getShipsByName(name).forEach(
				shipData -> source.sendSystemMessage(Component.literal(shipData.getName()))
		);

		return 0;
	}

	private int get(CommandSourceStack source, String name) {
		AirShipRegistry.INSTANCE.getShipsByName(name).forEach(
				(shipData) -> {
					if (shipData.getName().equals(name)) {
						source.sendSystemMessage(Component.literal("NAME: " + shipData.getName()));
						source.sendSystemMessage(Component.literal("X: " + shipData.getRWCoreX()));
						source.sendSystemMessage(Component.literal("Z: " + shipData.getRWCoreZ()));
						source.sendSystemMessage(Component.literal("DIM: " + shipData.getDimensionCode()));
						source.sendSystemMessage(Component.literal("CORE: " + shipData.getSWCore().toShortString()));
						source.sendSystemMessage(Component.literal("RADIUS: " + shipData.getRadius()));
					}
				}
		);

		return 0;
	}

	private int warp(CommandSourceStack source, int dim, int x, int z) {
		if (isInShipDimension(source.getLevel())) {
			AirShipRegistry.INSTANCE.updateDimension(x, z, dim,
					new BlockPos((int) source.getPosition().x(),
							(int) source.getPosition().y(),
							(int) source.getPosition().z()));
		}
		return 0;
	}

	private int createCompass(CommandSourceStack source, String name, int x, int z) {
		ShipData data = getShipData(source, name, x, z);
		if (data == null) return 1;

		ItemStack detector = new ItemStack(ModItems.SHIP_DETECTOR.get(), 1);
		detector.getOrCreateTag().put(CORE_POSITION_TAG, NbtUtils.writeBlockPos(data.getSWCore()));
		if (source.getPlayer() != null) {
			source.getPlayer().addItem(detector);
		}

		return 0;
	}

	private int tp(CommandSourceStack source, String name, int x, int z) {
		ShipData data = getShipData(source, name, x, z);
		if (data == null) return 1;

		BlockPos pos = searchForSafeLandPosition(data.getSWCore(), toLevel(0, source.getLevel()), 8, 4);

		if (pos == null) {
			pos = searchForSafeLandPosition(data.getSWCore(), toLevel(0, source.getLevel()), 80, 40);
		}
		if (pos == null) {
			source.sendSystemMessage(Component.literal("No safe landing position")
					.withStyle(ChatFormatting.RED));
		}

		useRune(source.getLevel(), new BlockPos((int) source.getPosition().x(),
				(int) source.getPosition().y(),
				(int) source.getPosition().z()), toLevel(0, source.getLevel()), pos);

		return 0;
	}

	@Nullable
	private static ShipData getShipData(CommandSourceStack source, String name, int x, int z) {
		if (name != null) {
			List<ShipData> shipDataList = AirShipRegistry.INSTANCE.getShipsByName(name);
			if (shipDataList.size() == 0) {
				source.sendSystemMessage(Component.literal("Ship does not exist with name: " + name)
						.withStyle(ChatFormatting.RED));
				return null;
			}
			if (shipDataList.size() > 1) {
				source.sendSystemMessage(Component.literal("Too many ships exist with name: " + name)
						.withStyle(ChatFormatting.RED));
				return null;
			}
			return shipDataList.get(0);
		} else {
			ShipData shipData = AirShipRegistry.INSTANCE.isInShip(new BlockPos(x, SHIP_CORE_Y_POSITION, z), 0);
			if (shipData == null || shipData.getSWCore().getX() != x || shipData.getSWCore().getZ() != z) {
				source.sendSystemMessage(Component.literal("Ship does not exist on coordinate: " + x + " " + z)
						.withStyle(ChatFormatting.RED));
				return null;
			}
			return shipData;
		}
	}

	private int createShip(CommandSourceStack source, int radius) {
		BlockPos core = AirShipRegistry.INSTANCE.getNewCorePosition(new BlockPos((int) source.getPosition().x(),
						(int) source.getPosition().y(),
						(int) source.getPosition().z()),
				isInShipDimension(source.getLevel()) ? toLevel(1, source.getLevel()) : source.getLevel(), radius);

		ServerLevel shipWorld = toLevel(0, source.getLevel());
		shipWorld.setBlock(new BlockPos(core.getX(), core.getY(), core.getZ()),
				ModBlocks.CORE.get().defaultBlockState(), 2, 0);
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				shipWorld.setBlock(new BlockPos(core.getX() + x, core.getY() - 1, core.getZ() + z),
						Blocks.STONE.defaultBlockState(), 2, 0);
			}
		}

		return createCompass(source, null, core.getX(), core.getZ());
	}
}
