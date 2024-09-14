package hu.xannosz.airship.client;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static hu.xannosz.airship.item.ShipDetector.*;
import static hu.xannosz.airship.util.Constants.REAL_RADIUS;
import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;
import static hu.xannosz.airship.util.ShipUtils.levelToString;

@Slf4j
@OnlyIn(Dist.CLIENT)
public class ShipDetectorFunction implements ClampedItemPropertyFunction {
	@Override
	@SuppressWarnings("ConstantConditions")
	public float unclampedCall(@NotNull ItemStack stack, @Nullable ClientLevel level,
							   @Nullable LivingEntity entity, int seed) {
		if (isInShipDimension((Level) level)) {
			return 0.1f;
		}

		final String dimension = stack.getOrCreateTag().getString(DIM_TAG);

		if (!levelToString(level).equals(dimension) || entity == null) {
			return 0.2f;
		}

		final double realX = stack.getOrCreateTag().getDouble(X_TAG);
		final double realZ = stack.getOrCreateTag().getDouble(Z_TAG);
		final double distant = getDistantFromEntityToPos(entity, realX, realZ);

		if (distant < REAL_RADIUS / 3f) {
			return 0.5f;
		}

		float angle = Mth.positiveModulo((float) getAngleFromEntityToPos(entity, realX, realZ), 1.0f);
		angle += 0.5D - (getWrappedVisualRotationY(entity) - 0.25D);

		if (angle > 1) {
			angle -= 1;
		}

		if (distant > REAL_RADIUS) {
			return 0.3f + angle / 10;
		} else {
			return 0.4f + angle / 10;
		}
	}

	private double getDistantFromEntityToPos(Entity entity, double x, double z) {
		return Math.sqrt(Math.pow(z - entity.getZ(), 2) + Math.pow(x - entity.getX(), 2));
	}

	private double getAngleFromEntityToPos(Entity entity, double x, double z) {
		return Math.atan2(z - entity.getZ(), x - entity.getX()) / (double) ((float) Math.PI * 2F);
	}

	private double getWrappedVisualRotationY(Entity entity) {
		return Mth.positiveModulo(entity.getVisualRotationYInDegrees() / 360.0F, 1.0D);
	}
}
