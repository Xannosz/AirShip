package hu.xannosz.airship.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import hu.xannosz.airship.AirShip;
import hu.xannosz.airship.network.RadarData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class RadarScreen extends AbstractContainerScreen<RadarMenu> {
	private static final ResourceLocation TEXTURE =
			new ResourceLocation(AirShip.MOD_ID, "textures/gui/radar.png");

	private int x;
	private int y;

	public RadarScreen(RadarMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		imageHeight = 88;
		imageWidth = 238;
	}

	@Override
	protected void init() {
		super.init();

		x = (width - imageWidth) / 2;
		y = (height - imageHeight) / 2;
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.enableBlend();
		guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

		getMenu().getBlockEntity().setOpened(true);

		RadarData radarData = getMenu().getBlockEntity().getRadarData();

		for (int runX = -75; runX < 75; runX++) {
			for (int runZ = -75; runZ < 75; runZ++) {
				if (!radarData.getShip(runX, runZ).equals("")) {
					guiGraphics.fill(x + 8 + runX, y + 8 + runZ,
							x + 8 + runX + 1, +y + 8 + runZ + 1,
							0x385fA1
					);
				}
			}
		}
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		//call built-in functions
		renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, delta);

		//call built-in function
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (!getMenu().getBlockEntity().getRadarData().getName().isEmpty()) {
			guiGraphics.drawString(font, Component.translatable(AirShip.MOD_ID + ".navigation_table.name").append(getMenu().getBlockEntity().getRadarData().getName()), 115, 23, 0x904931, false);
			guiGraphics.drawString(font, Component.translatable(AirShip.MOD_ID + ".navigation_table.direction." + getMenu().getBlockEntity().getRadarData().getDirection().name()), 115, 33, 0x904931, false);
			guiGraphics.drawString(font, "X: " + getMenu().getBlockEntity().getRadarData().getRealX(), 115, 53, 0x385fA1, false);
			guiGraphics.drawString(font, "Z: " + getMenu().getBlockEntity().getRadarData().getRealZ(), 115, 63, 0x385fA1, false);
			guiGraphics.drawString(font, Component.translatable(AirShip.MOD_ID + ".navigation_table.speed").append("" + getMenu().getBlockEntity().getRadarData().getSpeed()), 115, 43, 0x1B764A, false);
		}
	}
}
