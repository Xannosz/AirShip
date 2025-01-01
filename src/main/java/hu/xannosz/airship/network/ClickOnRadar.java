package hu.xannosz.airship.network;

import hu.xannosz.airship.blockentity.RadarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClickOnRadar {
	private final BlockPos position;
	private final String name;

	public ClickOnRadar(BlockPos position, String name) {
		this.position = position;
		this.name = name;
	}

	public ClickOnRadar(FriendlyByteBuf buf) {
		position = buf.readBlockPos();
		name = buf.readUtf();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(position);
		buf.writeUtf(name);
	}

	@SuppressWarnings("resource")
	public void handler(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			// SERVER SITE
			BlockEntity entity = Objects.requireNonNull(context.getSender()).level().getBlockEntity(position);
			if (entity instanceof RadarBlockEntity radarBlockEntity) {
				radarBlockEntity.clickOnRadar(name);
			}
		});
	}
}
