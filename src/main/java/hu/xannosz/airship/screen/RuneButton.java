package hu.xannosz.airship.screen;

import hu.xannosz.airship.network.ModMessages;
import hu.xannosz.airship.network.RuneButtonUsage;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

public class RuneButton extends GraphicalButton {
	@Setter
	private String runeId = "";

	public RuneButton(ButtonConfig config, ResourceLocation resourceLocation) {
		super(config, resourceLocation);
	}

	@Override
	public void onPress() {
		ModMessages.sendToServer(new RuneButtonUsage(runeId, config.getPosition()));
	}
}
