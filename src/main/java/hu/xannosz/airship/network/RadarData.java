package hu.xannosz.airship.network;

import hu.xannosz.airship.client.ClientPacketHandler;
import hu.xannosz.airship.util.ShipDirection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@Setter
public class RadarData {
	private final BlockPos position;
	private final Map<Integer, Map<Integer, String>> ships;
	private String name;
	private ShipDirection direction;
	private int realX;
	private int realZ;
	private int speed;

	public RadarData(BlockPos position) {
		this.position = position;
		ships = new HashMap<>();
		name = "";
		direction = ShipDirection.N;
	}

	public RadarData(FriendlyByteBuf buf) {
		position = buf.readBlockPos();
		ships = buf.readMap(FriendlyByteBuf::readInt, fbBuf -> fbBuf.readMap(FriendlyByteBuf::readInt, FriendlyByteBuf::readUtf));
		name = buf.readUtf();
		direction = buf.readEnum(ShipDirection.class);
		realX = buf.readInt();
		realZ = buf.readInt();
		speed = buf.readInt();
	}

	public void setShip(int x, int z, String name) {
		ships.computeIfAbsent(x, k -> new HashMap<>());
		ships.get(x).put(z, name);
	}

	public String getShip(int x, int z) {
		return ships.getOrDefault(x, new HashMap<>()).getOrDefault(z, "");
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(position);
		buf.writeMap(ships, FriendlyByteBuf::writeInt, (fbBuf, iiMap) -> fbBuf.writeMap(iiMap, FriendlyByteBuf::writeInt, FriendlyByteBuf::writeUtf));
		buf.writeUtf(name);
		buf.writeEnum(direction);
		buf.writeInt(realX);
		buf.writeInt(realZ);
		buf.writeInt(speed);
	}

	public void handler(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() ->
				// CLIENT SITE
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
						ClientPacketHandler.handleRadarData(this, supplier))
		);
	}
}
