package hu.xannosz.airship.network;

import hu.xannosz.airship.blockentity.RadarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class GetRadarData {
	private final BlockPos position;

	public GetRadarData(BlockPos position) {
		this.position = position;
	}

	public GetRadarData(FriendlyByteBuf buf) {
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
			if (entity instanceof RadarBlockEntity radarBlockEntity) {
				radarBlockEntity.responseToServer(context.getSender());
			}
		});
	}
}
