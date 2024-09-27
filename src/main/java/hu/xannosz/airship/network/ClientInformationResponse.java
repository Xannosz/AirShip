package hu.xannosz.airship.network;

import hu.xannosz.airship.client.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientInformationResponse {
	private final String dimensionName;
	private final float direction;

	public ClientInformationResponse(String dimensionName, float direction) {
		this.dimensionName = dimensionName;
		this.direction = direction;
	}

	public ClientInformationResponse(FriendlyByteBuf buf) {
		dimensionName = buf.readUtf();
		direction = buf.readFloat();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeUtf(dimensionName);
		buf.writeFloat(direction);
	}

	public void handler(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() ->
				// CLIENT SITE
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
						ClientPacketHandler.handleClientInformationResponse(dimensionName, direction))
		);
	}
}
