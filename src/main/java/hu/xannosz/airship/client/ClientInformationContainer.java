package hu.xannosz.airship.client;

import hu.xannosz.airship.network.GetClientInformation;
import hu.xannosz.airship.network.ModMessages;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Date;

@Slf4j
@OnlyIn(Dist.CLIENT)
public class ClientInformationContainer {
	private static String dimensionName = "minecraft:overworld";
	@Getter
	private static float direction = 0F;
	private static long clock = 0;

	public static String getDimensionName() {
		long time = new Date().getTime();
		if (clock + 500 < time) {
			clock = time;
			ModMessages.sendToServer(new GetClientInformation());
		}
		return dimensionName;
	}

	public static void setData(String dimensionName, float direction) {
		ClientInformationContainer.dimensionName = dimensionName;
		ClientInformationContainer.direction = direction;
	}
}
