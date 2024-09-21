package hu.xannosz.airship.screen;

import hu.xannosz.airship.network.ClickOnButton;
import hu.xannosz.airship.network.ModMessages;
import lombok.Setter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GraphicalButton extends AbstractButton {
	protected final ButtonConfig config;
	private final ResourceLocation resourceLocation;
	@Setter
	private boolean selected = false;
	@Setter
	private boolean drawDefault = false;

	public GraphicalButton(ButtonConfig config, ResourceLocation resourceLocation) {
		super(config.getHitBoxX(), config.getHitBoxY(),
				config.getHitBoxW(), config.getHitBoxH(), Component.empty());
		this.config = config;
		this.resourceLocation = resourceLocation;
	}

	@Override
	public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (visible) {
			if ((isHovered || selected) && !(isHovered && selected)) {
				guiGraphics.blit(resourceLocation, config.getHitBoxX(), config.getHitBoxY(),
						config.getHoveredX(), config.getHoveredY(),
						config.getHitBoxW(), config.getHitBoxH());
			} else if (drawDefault) {
				guiGraphics.blit(resourceLocation, config.getHitBoxX(), config.getHitBoxY(),
						config.getNormalX(), config.getNormalY(),
						config.getHitBoxW(), config.getHitBoxH());
			}
		}
	}

	@Override
	public void onPress() {
		ModMessages.sendToServer(new ClickOnButton(config.getButtonId(), config.getPosition()));
	}

	@Override
	public void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

	}
}
