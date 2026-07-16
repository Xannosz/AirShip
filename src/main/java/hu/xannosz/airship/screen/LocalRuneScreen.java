package hu.xannosz.airship.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import hu.xannosz.airship.AirShip;
import hu.xannosz.airship.network.LocalRuneData;
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
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static hu.xannosz.airship.util.ShipUtils.writeEGACharacters;

@Slf4j
@OnlyIn(Dist.CLIENT)
public class LocalRuneScreen extends AbstractContainerScreen<LocalRuneMenu> {
	private static final ResourceLocation TEXTURE =
			new ResourceLocation(AirShip.MOD_ID, "textures/gui/local_rune.png");

	private int x;
	private int y;
	private RuneButton rune1;
	private RuneButton rune2;
	private RuneButton rune3;
	private RuneButton rune4;
	private RuneButton rune5;
	private RuneButton rune6;
	private RuneButton rune7;
	private RuneButton rune8;
	private RuneButton rune9;

	public LocalRuneScreen(LocalRuneMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		imageHeight = 117;
		imageWidth = 74;
	}

	@Override
	protected void init() {
		super.init();

		x = (width - imageWidth) / 2;
		y = (height - imageHeight) / 2;

		GraphicalButton previous = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.PREVIOUS)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 8)
				.hitBoxY(y + 29)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(20)
				.hoveredY(118)
				.build(), TEXTURE);
		GraphicalButton next = new GraphicalButton(ButtonConfig.builder()
				.buttonId(ButtonId.NEXT)
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 48)
				.hitBoxY(y + 29)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(39)
				.hoveredY(118)
				.build(), TEXTURE);

		rune1 = new RuneButton(ButtonConfig.builder()
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 8)
				.hitBoxY(y + 52)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(1)
				.hoveredY(118)
				.build(), TEXTURE);

		rune2 = new RuneButton(ButtonConfig.builder()
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 28)
				.hitBoxY(y + 52)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(1)
				.hoveredY(118)
				.build(), TEXTURE);

		rune3 = new RuneButton(ButtonConfig.builder()
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 48)
				.hitBoxY(y + 52)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(1)
				.hoveredY(118)
				.build(), TEXTURE);

		rune4 = new RuneButton(ButtonConfig.builder()
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 8)
				.hitBoxY(y + 72)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(1)
				.hoveredY(118)
				.build(), TEXTURE);

		rune5 = new RuneButton(ButtonConfig.builder()
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 28)
				.hitBoxY(y + 72)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(1)
				.hoveredY(118)
				.build(), TEXTURE);

		rune6 = new RuneButton(ButtonConfig.builder()
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 48)
				.hitBoxY(y + 72)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(1)
				.hoveredY(118)
				.build(), TEXTURE);

		rune7 = new RuneButton(ButtonConfig.builder()
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 8)
				.hitBoxY(y + 92)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(1)
				.hoveredY(118)
				.build(), TEXTURE);

		rune8 = new RuneButton(ButtonConfig.builder()
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 28)
				.hitBoxY(y + 92)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(1)
				.hoveredY(118)
				.build(), TEXTURE);

		rune9 = new RuneButton(ButtonConfig.builder()
				.position(getMenu().getBlockEntity().getBlockPos())
				.hitBoxX(x + 48)
				.hitBoxY(y + 92)
				.hitBoxW(18)
				.hitBoxH(18)
				.hoveredX(1)
				.hoveredY(118)
				.build(), TEXTURE);

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

		LocalRuneData runeData = getMenu().getBlockEntity().getRuneData();

		rune1.setRuneId(runeData.getRune1());
		rune2.setRuneId(runeData.getRune2());
		rune3.setRuneId(runeData.getRune3());
		rune4.setRuneId(runeData.getRune4());
		rune5.setRuneId(runeData.getRune5());
		rune6.setRuneId(runeData.getRune6());
		rune7.setRuneId(runeData.getRune7());
		rune8.setRuneId(runeData.getRune8());
		rune9.setRuneId(runeData.getRune9());
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		//call built-in functions
		renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, delta);
		renderEGALabels(guiGraphics);

		//call built-in function
		renderTooltip(guiGraphics, mouseX, mouseY);
		renderExtendedTexts(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {

	}

	private void renderEGALabels(GuiGraphics guiGraphics) {
		LocalRuneData runeData = getMenu().getBlockEntity().getRuneData();

		String name = runeData.getId().toLowerCase(Locale.US);
		if (name.length() > 7) {
			name = name.substring(0, 8);
		}

		writeEGACharacters(name, x + 13, y + 14, guiGraphics);

		writeEGACharacters(runeData.getPage() + "1", x + 11, y + 58, guiGraphics);
		writeEGACharacters(runeData.getPage() + "2", x + 31, y + 58, guiGraphics);
		writeEGACharacters(runeData.getPage() + "3", x + 51, y + 58, guiGraphics);
		writeEGACharacters(runeData.getPage() + "4", x + 11, y + 78, guiGraphics);
		writeEGACharacters(runeData.getPage() + "5", x + 31, y + 78, guiGraphics);
		writeEGACharacters(runeData.getPage() + "6", x + 51, y + 78, guiGraphics);
		writeEGACharacters(runeData.getPage() + "7", x + 11, y + 98, guiGraphics);
		writeEGACharacters(runeData.getPage() + "8", x + 31, y + 98, guiGraphics);
		writeEGACharacters(runeData.getPage() + "9", x + 51, y + 98, guiGraphics);
	}

	private void renderExtendedTexts(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		LocalRuneData runeData = getMenu().getBlockEntity().getRuneData();

		if (y + 8 < mouseY && mouseY < y + 23 && x + 8 < mouseX && mouseX < x + 66) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getId()), x - 1, y + 9);
		}
		if (rune1.isHovered() && !Strings.isEmpty(runeData.getRune1())) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getRune1()), x + 11, y + 45);
		}
		if (rune2.isHovered() && !Strings.isEmpty(runeData.getRune2())) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getRune2()), x + 11, y + 45);
		}
		if (rune3.isHovered() && !Strings.isEmpty(runeData.getRune3())) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getRune3()), x + 11, y + 45);
		}
		if (rune4.isHovered() && !Strings.isEmpty(runeData.getRune4())) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getRune4()), x + 11, y + 45);
		}
		if (rune5.isHovered() && !Strings.isEmpty(runeData.getRune5())) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getRune5()), x + 11, y + 45);
		}
		if (rune6.isHovered() && !Strings.isEmpty(runeData.getRune6())) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getRune6()), x + 11, y + 45);
		}
		if (rune7.isHovered() && !Strings.isEmpty(runeData.getRune7())) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getRune7()), x + 11, y + 45);
		}
		if (rune8.isHovered() && !Strings.isEmpty(runeData.getRune8())) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getRune8()), x + 11, y + 45);
		}
		if (rune9.isHovered() && !Strings.isEmpty(runeData.getRune9())) {
			guiGraphics.renderTooltip(this.font,
					Component.literal(runeData.getRune9()), x + 11, y + 45);
		}
	}
}
