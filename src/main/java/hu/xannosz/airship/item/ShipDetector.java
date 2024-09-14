package hu.xannosz.airship.item;

import hu.xannosz.airship.blockentity.CoreBlockEntity;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ExternalRegistry;
import hu.xannosz.airship.registries.ShipData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static hu.xannosz.airship.util.ShipUtils.getShipDimName;
import static hu.xannosz.airship.util.ShipUtils.toLevel;

public class ShipDetector extends Item {
	public static final String CORE_POSITION_TAG = "core_position";
	public static final String NAME_TAG = "core_name";
	public static final String DIM_TAG = "real_dim";
	public static final String X_TAG = "real_x";
	public static final String Z_TAG = "real_z";

	public ShipDetector() {
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
		final String name = itemStack.getOrCreateTag().getString(ShipDetector.NAME_TAG);

		components.add(Component.translatable("text.xannosz_airship.ship_name").append(Component.literal(name)).withStyle(ChatFormatting.AQUA));

		super.appendHoverText(itemStack, level, components, tooltipFlag);
	}

	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
		if (!level.isClientSide()) {
			final Tag tag = stack.getOrCreateTag().get(ShipDetector.CORE_POSITION_TAG);
			if (tag == null) {
				stack.getOrCreateTag().putString(DIM_TAG, "");
				return;
			}

			final BlockPos position = NbtUtils.readBlockPos((CompoundTag) tag);
			final ShipData shipData = AirShipRegistry.INSTANCE.isInShip(position, 0);
			if (shipData == null) {
				stack.getOrCreateTag().putString(DIM_TAG, "");
				return;
			}
			final String dimension = ExternalRegistry.INSTANCE.getDimensionName(shipData.getDimensionCode());

			stack.getOrCreateTag().putString(DIM_TAG, dimension);
			stack.getOrCreateTag().putDouble(X_TAG, shipData.getRWCoreX());
			stack.getOrCreateTag().putDouble(Z_TAG, shipData.getRWCoreZ());

			final ServerLevel serverWorld = toLevel(getShipDimName(), (ServerLevel) entity.level());
			final BlockEntity blockEntity = serverWorld.getBlockEntity(position);
			if (blockEntity instanceof CoreBlockEntity coreBlockEntity) {
				stack.getOrCreateTag().putString(NAME_TAG, coreBlockEntity.getName());
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}
	}
}
