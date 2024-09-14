package hu.xannosz.airship.network;

import hu.xannosz.airship.blockentity.NavigationTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClickOnMap {
	private final int x;
	private final int y;
	private final BlockPos position;

	public ClickOnMap(int x, int y, BlockPos position) {
		this.x = x;
		this.y = y;
		this.position = position;
	}

	public ClickOnMap(FriendlyByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		position = buf.readBlockPos();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeBlockPos(position);
	}

	@SuppressWarnings("resource")
	public void handler(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			// SERVER SITE
			BlockEntity entity = Objects.requireNonNull(context.getSender()).level().getBlockEntity(position);
			if (entity instanceof NavigationTableBlockEntity navigationTableBlockEntity) {
				navigationTableBlockEntity.clickOnMap(x, y);
			}
		});
	}
}
