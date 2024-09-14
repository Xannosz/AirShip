package hu.xannosz.airship.client;

import hu.xannosz.airship.blockentity.NavigationTableBlockEntity;
import hu.xannosz.airship.blockentity.SmallRuneBlockEntity;
import hu.xannosz.airship.network.MapData;
import hu.xannosz.airship.network.PlaySoundPacket;
import hu.xannosz.airship.network.SmallRuneData;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientPacketHandler {
	public static void handleMapData(MapData mapData, Supplier<NetworkEvent.Context> ctx) {
		BlockEntity entity = Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(mapData.getPosition());
		if (entity instanceof NavigationTableBlockEntity navigationTableBlockEntity) {
			navigationTableBlockEntity.setMapData(mapData);
		}
	}

	public static void handleSmallRuneData(SmallRuneData smallRuneData, Supplier<NetworkEvent.Context> ctx) {
		BlockEntity entity = Objects.requireNonNull(Minecraft.getInstance().level).getBlockEntity(smallRuneData.getPosition());
		if (entity instanceof SmallRuneBlockEntity smallRuneBlockEntity) {
			smallRuneBlockEntity.setRuneData(smallRuneData);
		}
	}

	public static void handlePlaySoundPacket(PlaySoundPacket playSoundPacket, Supplier<NetworkEvent.Context> ctx) {
		if (playSoundPacket.isInTheShip()) {
			Objects.requireNonNull(Minecraft.getInstance().level).playSound(Minecraft.getInstance().player,
					playSoundPacket.getPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 0.5f, 2f);
		}
		Objects.requireNonNull(Minecraft.getInstance().level).playLocalSound(
				(double) playSoundPacket.getPosition().getX() + 0.5D,
				(double) playSoundPacket.getPosition().getY() + 0.5D,
				(double) playSoundPacket.getPosition().getZ() + 0.5D,
				SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 2F, 2F, false);
	}
}
