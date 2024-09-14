package hu.xannosz.airship.network;

import hu.xannosz.airship.blockentity.SmallRuneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class GetSmallRuneData {
	private final BlockPos position;

	public GetSmallRuneData(BlockPos position) {
		this.position = position;
	}

	public GetSmallRuneData(FriendlyByteBuf buf) {
		position = buf.readBlockPos();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(position);
	}

	@SuppressWarnings("resource")
	public void handler(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			// SERVER SITE
			BlockEntity entity = Objects.requireNonNull(context.getSender()).level().getBlockEntity(position);
			if (entity instanceof SmallRuneBlockEntity smallRuneBlockEntity) {
				smallRuneBlockEntity.responseToServer(context.getSender());
			}
		});
	}
}
