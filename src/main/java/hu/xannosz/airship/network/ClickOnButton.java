package hu.xannosz.airship.network;

import hu.xannosz.airship.util.ButtonId;
import hu.xannosz.airship.util.ButtonUser;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClickOnButton {
	private final ButtonId buttonId;
	private final BlockPos position;

	public ClickOnButton(ButtonId buttonId, BlockPos position) {
		this.buttonId = buttonId;
		this.position = position;
	}

	public ClickOnButton(FriendlyByteBuf buf) {
		buttonId = buf.readEnum(ButtonId.class);
		position = buf.readBlockPos();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeEnum(buttonId);
		buf.writeBlockPos(position);
	}

	@SuppressWarnings("resource")
	public void handler(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			// SERVER SITE
			BlockEntity entity = Objects.requireNonNull(context.getSender()).level().getBlockEntity(position);
			if (entity instanceof ButtonUser buttonUser) {
				buttonUser.executeButtonClick(buttonId);
			}
		});
	}
}
