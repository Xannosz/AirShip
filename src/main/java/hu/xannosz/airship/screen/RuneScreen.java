package hu.xannosz.airship.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import hu.xannosz.airship.AirShip;
import hu.xannosz.airship.network.SmallRuneData;
import hu.xannosz.airship.util.ButtonId;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static hu.xannosz.airship.util.ShipUtils.writeEGACharacters;

@Slf4j
@OnlyIn(Dist.CLIENT)
public class RuneScreen extends AbstractContainerScreen<RuneMenu> {
	private static final ResourceLocation TEXTURE =
			new ResourceLocation(AirShip.MOD_ID, "textures/gui/rune.png");

	private int x;
	private int y;
	private GraphicalButton land;
	private GraphicalButton disable;
	private GraphicalButton rune1;
	private GraphicalButton rune2;
	private GraphicalButton rune3;
	private GraphicalButton rune4;
	private GraphicalButton rune5;
	private GraphicalButton rune6;
	private GraphicalButton rune7;
	private GraphicalButton rune8;
	private GraphicalButton rune9;

	public RuneScreen(RuneMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		imageHeight = 88;
		imageWidth = 238;
	}

	@Override
	protected void init() {
		super.init();

		x = (width - imageWidth) / 2;
		y = (height - imageHeight) / 2;

		land = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.LAND)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 157)
				.hitBoxY(y + 5)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(92)
				.hoveredY(89)
				.normalX(92)
				.normalY(108)
				.build(), TEXTURE);
		disable = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.TOGGLE_RUNE_ENABLED)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 178)
				.hitBoxY(y + 5)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(111)
				.hoveredY(89)
				.build(), TEXTURE);
		final GraphicalButton near = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.NEAR)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 199)
				.hitBoxY(y + 5)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(130)
				.hoveredY(89)
				.build(), TEXTURE);
		final GraphicalButton previous = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.PREVIOUS)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 5)
				.hitBoxY(y + 25)
				.hitBoxW(11)
				.hitBoxH(58)
				.hoveredX(1)
				.hoveredY(89)
				.build(), TEXTURE);
		final GraphicalButton next = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.NEXT)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 222)
				.hitBoxY(y + 25)
				.hitBoxW(11)
				.hitBoxH(58)
				.hoveredX(13)
				.hoveredY(89)
				.build(), TEXTURE);

		rune1 = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.RUNE_1)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 18)
				.hitBoxY(y + 25)
				.hitBoxW(66)
				.hitBoxH(18)
				.hoveredX(25)
				.hoveredY(89)
				.normalX(25)
				.normalY(108)
				.build(), TEXTURE);
		rune2 = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.RUNE_2)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 18)
				.hitBoxY(y + 45)
				.hitBoxW(66)
				.hitBoxH(18)
				.hoveredX(25)
				.hoveredY(89)
				.normalX(25)
				.normalY(108)
				.build(), TEXTURE);
		rune3 = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.RUNE_3)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 18)
				.hitBoxY(y + 65)
				.hitBoxW(66)
				.hitBoxH(18)
				.hoveredX(25)
				.hoveredY(89)
				.normalX(25)
				.normalY(108)
				.build(), TEXTURE);
		rune4 = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.RUNE_4)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 86)
				.hitBoxY(y + 25)
				.hitBoxW(66)
				.hitBoxH(18)
				.hoveredX(25)
				.hoveredY(89)
				.normalX(25)
				.normalY(108)
				.build(), TEXTURE);
		rune5 = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.RUNE_5)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 86)
				.hitBoxY(y + 45)
				.hitBoxW(66)
				.hitBoxH(18)
				.hoveredX(25)
				.hoveredY(89)
				.normalX(25)
				.normalY(108)
				.build(), TEXTURE);
		rune6 = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.RUNE_6)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 86)
				.hitBoxY(y + 65)
				.hitBoxW(66)
				.hitBoxH(18)
				.hoveredX(25)
				.hoveredY(89)
				.normalX(25)
				.normalY(108)
				.build(), TEXTURE);
		rune7 = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.RUNE_7)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 154)
				.hitBoxY(y + 25)
				.hitBoxW(66)
				.hitBoxH(18)
				.hoveredX(25)
				.hoveredY(89)
				.normalX(25)
				.normalY(108)
				.build(), TEXTURE);
		rune8 = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.RUNE_8)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 154)
				.hitBoxY(y + 45)
				.hitBoxW(66)
				.hitBoxH(18)
				.hoveredX(25)
				.hoveredY(89)
				.normalX(25)
				.normalY(108)
				.build(), TEXTURE);
		rune9 = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.RUNE_9)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 154)
				.hitBoxY(y + 65)
				.hitBoxW(66)
				.hitBoxH(18)
				.hoveredX(25)
				.hoveredY(89)
				.normalX(25)
				.normalY(108)
				.build(), TEXTURE);

		land.setDrawDefault(true);
		rune1.setDrawDefault(true);
		rune2.setDrawDefault(true);
		rune3.setDrawDefault(true);
		rune4.setDrawDefault(true);
		rune5.setDrawDefault(true);
		rune6.setDrawDefault(true);
		rune7.setDrawDefault(true);
		rune8.setDrawDefault(true);
		rune9.setDrawDefault(true);

		addRenderableWidget(land);
		addRenderableWidget(disable);
		addRenderableWidget(near);
		addRenderableWidget(previous);
		addRenderableWidget(next);
		addRenderableWidget(rune1);
		addRenderableWidget(rune2);
		addRenderableWidget(rune3);
		addRenderableWidget(rune4);
		addRenderableWidget(rune5);
		addRenderableWidget(rune6);
		addRenderableWidget(rune7);
		addRenderableWidget(rune8);
		addRenderableWidget(rune9);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.enableBlend();
		guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

		getMenu().getBlockEntity().setOpened(true);

		SmallRuneData runeData = getMenu().getBlockEntity().getRuneData();

		land.visible = runeData.isLandEnabled();
		disable.setSelected(!runeData.isEnabled());
		rune1.visible = !runeData.getRune1().equals("");
		rune2.visible = !runeData.getRune2().equals("");
		rune3.visible = !runeData.getRune3().equals("");
		rune4.visible = !runeData.getRune4().equals("");
		rune5.visible = !runeData.getRune5().equals("");
		rune6.visible = !runeData.getRune6().equals("");
		rune7.visible = !runeData.getRune7().equals("");
		rune8.visible = !runeData.getRune8().equals("");
		rune9.visible = !runeData.getRune9().equals("");
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		//call built-in functions
		renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, delta);
		renderEGALabels(guiGraphics);

		//call built-in function
		renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {

	}

	private void renderEGALabels(GuiGraphics guiGraphics) {
		SmallRuneData runeData = getMenu().getBlockEntity().getRuneData();

		writeEGACharacters(runeData.getId(), x + 23, y + 12, guiGraphics);

		writeEGACharacters(runeData.getRune1(), x + 20, y + 32, guiGraphics);
		writeEGACharacters(runeData.getRune2(), x + 20, y + 52, guiGraphics);
		writeEGACharacters(runeData.getRune3(), x + 20, y + 72, guiGraphics);
		writeEGACharacters(runeData.getRune4(), x + 88, y + 32, guiGraphics);
		writeEGACharacters(runeData.getRune5(), x + 88, y + 52, guiGraphics);
		writeEGACharacters(runeData.getRune6(), x + 88, y + 72, guiGraphics);
		writeEGACharacters(runeData.getRune7(), x + 156, y + 32, guiGraphics);
		writeEGACharacters(runeData.getRune8(), x + 156, y + 52, guiGraphics);
		writeEGACharacters(runeData.getRune9(), x + 156, y + 72, guiGraphics);
	}
}
