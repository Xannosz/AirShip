package hu.xannosz.airship.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import hu.xannosz.airship.AirShip;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static net.minecraft.client.renderer.blockentity.TheEndPortalRenderer.END_SKY_LOCATION;

@Slf4j
@OnlyIn(Dist.CLIENT)
public class ShipDimensionEffect extends DimensionSpecialEffects {
	public static final ShipDimensionEffect INSTANCE = new ShipDimensionEffect();

	private boolean firstRun = true;

	public ShipDimensionEffect() {
		super(-30, true, SkyType.NONE, false, true);
	}

	@Override
	public @NotNull Vec3 getBrightnessDependentFogColor(@NotNull Vec3 fogColor, float brightness) {
		return getEffect().getBrightnessDependentFogColor(fogColor, brightness);
	}

	@Override
	public boolean isFoggyAt(int pX, int pY) {
		return getEffect().isFoggyAt(pX, pY);
	}

	@Override
	public boolean renderClouds(@NotNull ClientLevel level, int ticks, float partialTick, @NotNull PoseStack poseStack, double camX, double camY, double camZ, @NotNull Matrix4f projectionMatrix) {
		return true;
	}

	@Override
	public boolean renderSky(@NotNull ClientLevel level, int ticks, float partialTick, @NotNull PoseStack poseStack,
							 @NotNull Camera camera, @NotNull Matrix4f projectionMatrix, boolean isFoggy, @NotNull Runnable setupFog) {
		firstRun = !firstRun;
		if (firstRun) {
			return false;
		}

		poseStack.pushPose();
		if (ClientInformationContainer.getDimensionName().equals("minecraft:the_nether")) {
			renderNetherSky(poseStack);
		}
		poseStack.mulPose(Axis.YP.rotationDegrees(ClientInformationContainer.getDirection()));
		if (!getEffect().renderSky(
				level, ticks, partialTick, poseStack,
				camera, projectionMatrix, isFoggy, setupFog
		)) {
			Minecraft.getInstance().levelRenderer.renderSky(poseStack, projectionMatrix,
					partialTick, camera, isFoggy, setupFog);
		}
		poseStack.popPose();

		return true;
	}


	@Override
	public boolean renderSnowAndRain(@NotNull ClientLevel level, int ticks, float partialTick, @NotNull LightTexture lightTexture, double camX, double camY, double camZ) {
		return true;
	}

	@Override
	public float[] getSunriseColor(float p_108888_, float p_108889_) {
		return getEffect().getSunriseColor(p_108888_, p_108889_);
	}

	@Override
	public boolean hasGround() {
		return getEffect().hasGround();
	}

	@Override
	public @NotNull DimensionSpecialEffects.SkyType skyType() {
		return getEffect().skyType();
	}

	@Override
	public boolean forceBrightLightmap() {
		return getEffect().forceBrightLightmap();
	}

	@Override
	public boolean constantAmbientLight() {
		return getEffect().constantAmbientLight();
	}

	@Override
	public void adjustLightmapColors(@NotNull ClientLevel level, float partialTicks, float skyDarken, float skyLight, float blockLight, int pixelX, int pixelY, @NotNull Vector3f colors) {
		getEffect().adjustLightmapColors(level, partialTicks, skyDarken, skyLight, blockLight, pixelX, pixelY, colors);
	}

	private DimensionSpecialEffects getEffect() {
		if (ClientInformationContainer.getDimensionName().equals("minecraft:overworld")) {
			return new DimensionSpecialEffects.OverworldEffects();
		}
		if (ClientInformationContainer.getDimensionName().equals("minecraft:the_nether")) {
			return new DimensionSpecialEffects.NetherEffects();
		}
		return new DimensionSpecialEffects.EndEffects();
	}

	private void renderNetherSky(PoseStack poseStack) {
		RenderSystem.enableBlend();
		RenderSystem.depthMask(false);
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.setShaderTexture(0, new ResourceLocation(AirShip.MOD_ID,"textures/dim/extended_nether.png"));
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tesselator.getBuilder();

		for(int i = 0; i < 6; ++i) {
			poseStack.pushPose();
			if (i == 1) {
				poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
			}

			if (i == 2) {
				poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
			}

			if (i == 3) {
				poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
			}

			if (i == 4) {
				poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
			}

			if (i == 5) {
				poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-90.0F));
			}

			Matrix4f matrix4f = poseStack.last().pose();
			bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
			bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(0.0F, 0.0F).color(40, 40, 40, 255).endVertex();
			bufferbuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(0.0F, 16.0F).color(40, 40, 40, 255).endVertex();
			bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(16.0F, 16.0F).color(40, 40, 40, 255).endVertex();
			bufferbuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(16.0F, 0.0F).color(40, 40, 40, 255).endVertex();
			tesselator.end();
			poseStack.popPose();
		}

		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
	}
}
