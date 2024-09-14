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
public class MapData {
	private final BlockPos position;
	private final Map<Integer, Map<Integer, Integer>> colors;
	private boolean showAirShips;
	private String name;
	private ShipDirection direction;
	private Integer realX;
	private int realZ;
	private int speed;
	private int targetX;
	private int targetZ;
	private boolean hasTarget;

	public MapData(BlockPos position) {
		this.position = position;
		colors = new HashMap<>();
		name = "";
		direction = ShipDirection.N;
	}

	public MapData(FriendlyByteBuf buf) {
		position = buf.readBlockPos();
		colors = buf.readMap(FriendlyByteBuf::readInt, fbBuf -> fbBuf.readMap(FriendlyByteBuf::readInt, FriendlyByteBuf::readInt));
		showAirShips = buf.readBoolean();
		name = buf.readUtf();
		direction = buf.readEnum(ShipDirection.class);
		realX = buf.readInt();
		realZ = buf.readInt();
		speed = buf.readInt();
		targetX = buf.readInt();
		targetZ = buf.readInt();
		hasTarget = buf.readBoolean();
	}

	public void setColor(int x, int y, int color) {
		colors.computeIfAbsent(x, k -> new HashMap<>());
		colors.get(x).put(y, color);
	}

	public int getColor(int x, int y) {
		return colors.getOrDefault(x, new HashMap<>()).getOrDefault(y, 0);
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(position);
		buf.writeMap(colors, FriendlyByteBuf::writeInt, (fbBuf, iiMap) -> fbBuf.writeMap(iiMap, FriendlyByteBuf::writeInt, FriendlyByteBuf::writeInt));
		buf.writeBoolean(showAirShips);
		buf.writeUtf(name);
		buf.writeEnum(direction);
		buf.writeInt(realX);
		buf.writeInt(realZ);
		buf.writeInt(speed);
		buf.writeInt(targetX);
		buf.writeInt(targetZ);
		buf.writeBoolean(hasTarget);
	}

	public void handler(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() ->
				// CLIENT SITE
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
						ClientPacketHandler.handleMapData(this, supplier))
		);
	}
}
