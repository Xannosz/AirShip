package hu.xannosz.airship.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import hu.xannosz.airship.AirShip;
import hu.xannosz.airship.util.ButtonId;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@Slf4j
@OnlyIn(Dist.CLIENT)
public class NavigationTableScreen extends AbstractContainerScreen<NavigationTableMenu> {
	private static final ResourceLocation TEXTURE =
			new ResourceLocation(AirShip.MOD_ID, "textures/gui/navigation_table.png");

	private int x;
	private int y;
	private GraphicalButton toggleShipsDisplay;

	public NavigationTableScreen(NavigationTableMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		imageHeight = 219;
		imageWidth = 254;
	}

	@Override
	protected void init() {
		super.init();

		x = (width - imageWidth) / 2;
		y = (height - imageHeight) / 2;

		toggleShipsDisplay = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.TOGGLE_SHIPS_DISPLAY)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 111)
				.hitBoxY(y + 7)
				.hitBoxW(14)
				.hitBoxH(14)
				.hoveredX(15)
				.hoveredY(219)
				.build(), TEXTURE);
		final GraphicalButton writeCoordinates = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.WRITE_COORDINATES)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 75)
				.hitBoxY(y + 114)
				.hitBoxW(14)
				.hitBoxH(14)
				.hoveredX(0)
				.hoveredY(219)
				.build(), TEXTURE);
		final GraphicalButton downScale = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.DOWN_SCALE)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 127)
				.hitBoxY(y + 7)
				.hitBoxW(14)
				.hitBoxH(14)
				.hoveredX(30)
				.hoveredY(219)
				.build(), TEXTURE);
		final GraphicalButton upScale = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.UP_SCALE)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 143)
				.hitBoxY(y + 7)
				.hitBoxW(14)
				.hitBoxH(14)
				.hoveredX(45)
				.hoveredY(219)
				.build(), TEXTURE);

		addRenderableWidget(toggleShipsDisplay);
		addRenderableWidget(writeCoordinates);
		addRenderableWidget(downScale);
		addRenderableWidget(upScale);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.enableBlend();
		guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
		getMenu().getBlockEntity().setOpened(true);
		toggleShipsDisplay.setSelected(getMenu().getMapData().isShowAirShips());

		for (int runX = 0; runX < 100; runX++) {
			for (int runY = 0; runY < 100; runY++) {
				guiGraphics.fill(x + 8 + runX, y + 8 + runY,
						x + 8 + runX + 1, +y + 8 + runY + 1,
						correctHue(MapColor.getColorFromPackedId(getMenu().getMapData().getColor(runX, runY)))
				);
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
		if (!getMenu().getMapData().getName().isEmpty()) {
			guiGraphics.drawString(font, Component.translatable("navigationtable.name").append(getMenu().getMapData().getName()), 115, 23, 0x904931,false);
			guiGraphics.drawString(font, Component.translatable("navigationtable.direction." + getMenu().getMapData().getDirection().name()), 115, 33, 0x904931,false);
			guiGraphics.drawString(font, "X: " + getMenu().getMapData().getRealX(), 115, 53, 0x385fA1,false);
			guiGraphics.drawString(font, "Z: " + getMenu().getMapData().getRealZ(), 115, 63, 0x385fA1,false);
			guiGraphics.drawString(font, Component.translatable("navigationtable.speed").append("" + getMenu().getMapData().getSpeed()), 115, 43, 0x1B764A,false);
		}
		if (getMenu().getMapData().isHasTarget()) {
			guiGraphics.drawString(font, Component.translatable("navigationtable.target"), 115, 83, 0x904931,false);
			guiGraphics.drawString(font, "X: " + getMenu().getMapData().getTargetX(), 115, 93, 0x385fA1,false);
			guiGraphics.drawString(font, "Z: " + getMenu().getMapData().getTargetZ(), 115, 103, 0x385fA1,false);
		}
	}

	public int correctHue(int color) {
		return FastColor.ARGB32.color(255, FastColor.ABGR32.red(color), FastColor.ABGR32.green(color), FastColor.ABGR32.blue(color));
	}
}
