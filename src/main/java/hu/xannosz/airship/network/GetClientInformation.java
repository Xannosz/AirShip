package hu.xannosz.airship.network;

import hu.xannosz.airship.blockentity.CoreBlockEntity;
import hu.xannosz.airship.registries.AirShipRegistry;
import hu.xannosz.airship.registries.ExternalRegistry;
import hu.xannosz.airship.registries.ShipData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static hu.xannosz.airship.util.ShipUtils.isInShipDimension;

public class GetClientInformation {

	public GetClientInformation() {
	}

	public GetClientInformation(FriendlyByteBuf buf) {
	}

	public void toBytes(FriendlyByteBuf buf) {
	}

	@SuppressWarnings("resource")
	public void handler(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			// SERVER SITE
			ServerPlayer player = context.getSender();
			if (player != null && isInShipDimension(player.level())) {
				final ShipData data = AirShipRegistry.INSTANCE.isInShip(player.getOnPos(), 0);
				if (player.level().getBlockEntity(data.getSWCore()) instanceof CoreBlockEntity coreBlockEntity) {
					ModMessages.sendToPlayer(new ClientInformationResponse(ExternalRegistry.INSTANCE.getDimension(data.getDimensionCode()).name(), coreBlockEntity.getDirection().getAngel()), player);
				}
			}
		});
	}
}
