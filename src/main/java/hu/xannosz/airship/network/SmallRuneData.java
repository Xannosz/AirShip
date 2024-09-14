package hu.xannosz.airship.network;

import hu.xannosz.airship.client.ClientPacketHandler;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Getter
@Setter
public class SmallRuneData {
	private final BlockPos position;
	private boolean enabled = true;
	private boolean landEnabled = false;
	private String id = "";
	private String rune1 = "";
	private String rune2 = "";
	private String rune3 = "";
	private String rune4 = "";
	private String rune5 = "";
	private String rune6 = "";
	private String rune7 = "";
	private String rune8 = "";
	private String rune9 = "";

	public SmallRuneData(BlockPos position) {
		this.position = position;
	}

	public SmallRuneData(FriendlyByteBuf buf) {
		position = buf.readBlockPos();
		enabled = buf.readBoolean();
		landEnabled = buf.readBoolean();
		id = buf.readUtf();
		rune1 = buf.readUtf();
		rune2 = buf.readUtf();
		rune3 = buf.readUtf();
		rune4 = buf.readUtf();
		rune5 = buf.readUtf();
		rune6 = buf.readUtf();
		rune7 = buf.readUtf();
		rune8 = buf.readUtf();
		rune9 = buf.readUtf();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(position);
		buf.writeBoolean(enabled);
		buf.writeBoolean(landEnabled);
		buf.writeUtf(id);
		buf.writeUtf(rune1);
		buf.writeUtf(rune2);
		buf.writeUtf(rune3);
		buf.writeUtf(rune4);
		buf.writeUtf(rune5);
		buf.writeUtf(rune6);
		buf.writeUtf(rune7);
		buf.writeUtf(rune8);
		buf.writeUtf(rune9);
	}

	public void handler(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() ->
				// CLIENT SITE
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
						ClientPacketHandler.handleSmallRuneData(this, supplier))
		);
	}
}
