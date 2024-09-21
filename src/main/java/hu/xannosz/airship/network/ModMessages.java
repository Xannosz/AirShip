package hu.xannosz.airship.network;

import hu.xannosz.airship.AirShip;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
	private static SimpleChannel INSTANCE;

	private static int pocketId = 0;

	private static int id() {
		return pocketId++;
	}

	public static void register() {
		INSTANCE = NetworkRegistry.ChannelBuilder
				.named(new ResourceLocation(AirShip.MOD_ID, "messages"))
				.networkProtocolVersion(() -> "1.0")
				.clientAcceptedVersions(s -> true)
				.serverAcceptedVersions(s -> true)
				.simpleChannel();

		INSTANCE.messageBuilder(ClickOnButton.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(ClickOnButton::new)
				.encoder(ClickOnButton::toBytes)
				.consumerMainThread(ClickOnButton::handler)
				.add();
		INSTANCE.messageBuilder(ClickOnMap.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(ClickOnMap::new)
				.encoder(ClickOnMap::toBytes)
				.consumerMainThread(ClickOnMap::handler)
				.add();
		INSTANCE.messageBuilder(GetMapData.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(GetMapData::new)
				.encoder(GetMapData::toBytes)
				.consumerMainThread(GetMapData::handler)
				.add();
		INSTANCE.messageBuilder(MapData.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(MapData::new)
				.encoder(MapData::toBytes)
				.consumerMainThread(MapData::handler)
				.add();
		INSTANCE.messageBuilder(GetSmallRuneData.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(GetSmallRuneData::new)
				.encoder(GetSmallRuneData::toBytes)
				.consumerMainThread(GetSmallRuneData::handler)
				.add();
		INSTANCE.messageBuilder(SmallRuneData.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(SmallRuneData::new)
				.encoder(SmallRuneData::toBytes)
				.consumerMainThread(SmallRuneData::handler)
				.add();
		INSTANCE.messageBuilder(PlaySoundPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
				.decoder(PlaySoundPacket::new)
				.encoder(PlaySoundPacket::toBytes)
				.consumerMainThread(PlaySoundPacket::handler)
				.add();
		INSTANCE.messageBuilder(RuneButtonUsage.class, id(), NetworkDirection.PLAY_TO_SERVER)
				.decoder(RuneButtonUsage::new)
				.encoder(RuneButtonUsage::toBytes)
				.consumerMainThread(RuneButtonUsage::handler)
				.add();
	}

	public static <MSG> void sendToServer(MSG message) {
		INSTANCE.sendToServer(message);
	}

	public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}
}
