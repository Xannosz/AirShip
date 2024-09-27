package hu.xannosz.airship.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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
	public boolean renderClouds(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, double camX, double camY, double camZ, Matrix4f projectionMatrix) {
		return true;
	}

	@Override
	public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack,
							 Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
		firstRun = !firstRun;
		if (firstRun) {
			return false;
		}

		poseStack.pushPose();
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
	public boolean renderSnowAndRain(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX, double camY, double camZ) {
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
	public DimensionSpecialEffects.SkyType skyType() {
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
	public void adjustLightmapColors(ClientLevel level, float partialTicks, float skyDarken, float skyLight, float blockLight, int pixelX, int pixelY, Vector3f colors) {
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
}
