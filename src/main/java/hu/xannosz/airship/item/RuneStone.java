package hu.xannosz.airship.item;

import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.RuneRegistry;
import hu.xannosz.airship.registries.ShipData;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.xannosz.airship.util.Constants.REAL_RADIUS;
import static hu.xannosz.airship.util.RuneUtil.filterRunes;
import static hu.xannosz.airship.util.RuneUtil.useRune;
import static hu.xannosz.airship.util.ShipUtils.toDimensionCode;
import static hu.xannosz.airship.util.ShipUtils.toLevel;

@Slf4j
public class RuneStone extends Item {
	public RuneStone() {
		super(new Item.Properties().stacksTo(1).durability(8));
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		if (level.isClientSide()) {
			return super.use(level, player, hand);
		}

		final List<ShipData> ships = AirShipRegistry.INSTANCE.getShipsInRadius(player.getOnPos(), toDimensionCode(level),
				REAL_RADIUS / 3);

		final Map<BlockPos, String> runes = new HashMap<>();
		for (ShipData data : ships) {
			runes.putAll(RuneRegistry.INSTANCE.searchForRunes(
					0, data.getSWCore(), data.getRadius(), 1000));
		}

		final Map<BlockPos, String> filteredRunes = filterRunes(runes, toLevel(0, level));
		for (BlockPos pos : filteredRunes.keySet()) {
			ItemStack stack = player.getItemInHand(hand);
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
			useRune((ServerLevel) level, player.getOnPos(), toLevel(0, level), pos);
			return InteractionResultHolder.pass(stack);
		}

		return super.use(level, player, hand);
	}
}
