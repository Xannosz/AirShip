package hu.xannosz.airship.util;

import com.mojang.datafixers.util.Pair;
import hu.xannosz.airship.dimension.DimensionTransporter;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;

import static hu.xannosz.airship.util.ShipUtils.*;

@Slf4j
public class TeleportHandler {

	@SuppressWarnings("resource")
	public static void onLivingTick(LivingEvent.LivingTickEvent e) {
		LivingEntity entity = e.getEntity();

		if (entity.level().isClientSide() || !(entity instanceof Player)) {
			return;
		}

		if (!entity.canChangeDimensions()) {
			return;
		}

		Pair<String, BlockPos> target = shouldTeleport(entity.blockPosition(),
				levelToString(e.getEntity().level()));

		if (target != null) {
			final ServerLevel serverWorld = toLevel(target.getFirst(), (ServerLevel) entity.level());
			if (serverWorld != null) {
				entity.changeDimension(serverWorld, new DimensionTransporter(target.getSecond()));
			}
		}
	}
}
