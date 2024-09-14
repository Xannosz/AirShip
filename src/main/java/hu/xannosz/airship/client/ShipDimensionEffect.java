package hu.xannosz.airship.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class ShipDimensionEffect extends DimensionSpecialEffects {
	public static final ShipDimensionEffect INSTANCE = new ShipDimensionEffect();

	public ShipDimensionEffect() {
		super(-30, true, SkyType.NONE, false, true);
	}

	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
		return fogColor;
	}

	@Override
	public boolean isFoggyAt(int pX, int pY) {
		return false;
	}

	@Override
	public boolean renderClouds(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, double camX, double camY, double camZ, Matrix4f projectionMatrix) {
		return true;
	}

	@Override
	public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack,
							 Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
		camera.getBlockPosition();

		//camera.setAnglesInternal(3f,camera.getXRot());
		//projectionMatrix.rotateLocalY(2f);

		renderOverWorld( level,  ticks,  partialTick,  poseStack, camera,  projectionMatrix,isFoggy, setupFog);

		return true;
	}


	@Override
	public boolean renderSnowAndRain(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX, double camY, double camZ) {
		return true;
	}

	private void renderOverWorld(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix,boolean isFoggy, Runnable skyFogSetup) {
		float q;
		float p;
		float o;
		int m;
		float k;
		float i;

		float timeOfTheDay=6000f;
		float sunAngle= 2f;
		float startBrightness = 6f;
		int moonPhase = 2;

		var levelRenderer = Minecraft.getInstance().levelRenderer;

		skyFogSetup.run();
		if (isFoggy) {
			return;
		}

		FogRenderer.levelFogColor();
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		RenderSystem.depthMask(false);
		float g = Mth.cos(timeOfTheDay * ((float)Math.PI * 2)) * 2.0f + 0.5f;
		g = Mth.clamp(g, 0.0f, 1.0f);
		RenderSystem.setShaderColor(0.529f * g, 0.808f * g, 0.922f * g, 1.0f); //sky color
		ShaderInstance shaderInstance = RenderSystem.getShader();
		//levelRenderer.skyBuffer.bind();
		//levelRenderer.skyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderInstance);
		VertexBuffer.unbind();
		RenderSystem.enableBlend();
		float[] fs = getSunriseColor(timeOfTheDay, partialTick);

		if (fs != null) {
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
			i = Mth.sin(sunAngle) < 0.0f ? 180.0f : 0.0f;
			poseStack.mulPose(Axis.ZP.rotationDegrees(i));
			poseStack.mulPose(Axis.ZP.rotationDegrees(90.0f));
			float j = fs[0];
			k = fs[1];
			float l = fs[2];
			Matrix4f matrix4f = poseStack.last().pose();
			bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
			bufferBuilder.vertex(matrix4f, 0.0f, 100.0f, 0.0f).color(j, k, l, fs[3]).endVertex();
			m = 16;
			for (int n = 0; n <= 16; ++n) {
				o = (float)n * ((float)Math.PI * 2) / 16.0f;
				p = Mth.sin(o);
				q = Mth.cos(o);
				bufferBuilder.vertex(matrix4f, p * 120.0f, q * 120.0f, -q * 40.0f * fs[3]).color(fs[0], fs[1], fs[2], 0.0f).endVertex();
			}
			BufferUploader.drawWithShader(bufferBuilder.end());
			poseStack.popPose();
		}
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		poseStack.pushPose();
//        i = 1.0f - 0; //level.getRainLevel(partialTick); TODO: Find out if we want this
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1);
		poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
		poseStack.mulPose(Axis.XP.rotationDegrees(timeOfTheDay * 360.0f));
		Matrix4f matrix4f2 = poseStack.last().pose();
		k = 30.0f;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, new ResourceLocation("textures/environment/sun.png"));
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix4f2, -k, 100.0f, -k).uv(0.0f, 0.0f).endVertex();
		bufferBuilder.vertex(matrix4f2, k, 100.0f, -k).uv(1.0f, 0.0f).endVertex();
		bufferBuilder.vertex(matrix4f2, k, 100.0f, k).uv(1.0f, 1.0f).endVertex();
		bufferBuilder.vertex(matrix4f2, -k, 100.0f, k).uv(0.0f, 1.0f).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		k = 20.0f;
		RenderSystem.setShaderTexture(0, new ResourceLocation("textures/environment/moon_phases.png"));
		int r = moonPhase;
		int s = r % 4;
		m = r / 4 % 2;
		float t = (float)(s) / 4.0f;
		o = (float)(m) / 2.0f;
		p = (float)(s + 1) / 4.0f;
		q = (float)(m + 1) / 2.0f;
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix4f2, -k, -100.0f, k).uv(p, q).endVertex();
		bufferBuilder.vertex(matrix4f2, k, -100.0f, k).uv(t, q).endVertex();
		bufferBuilder.vertex(matrix4f2, k, -100.0f, -k).uv(t, o).endVertex();
		bufferBuilder.vertex(matrix4f2, -k, -100.0f, -k).uv(p, o).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		float u = startBrightness;
		if (u > 0.0f) {
			RenderSystem.setShaderColor(u, u, u, u);
			FogRenderer.setupNoFog();
			//levelRenderer.starBuffer.bind();
			//levelRenderer.starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
			VertexBuffer.unbind();
			skyFogSetup.run();
		}
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		poseStack.popPose();
		RenderSystem.setShaderColor(0.0f, 0.0f, 0.0f, 1.0f);
		double d = Minecraft.getInstance().player.getEyePosition((float)partialTick).y - level.getLevelData().getHorizonHeight(Minecraft.getInstance().level);
		if (d < 0.0) {
			poseStack.pushPose();
			poseStack.translate(0.0f, 12.0f, 0.0f);
			//levelRenderer.darkBuffer.bind();
			//levelRenderer.darkBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderInstance);
			VertexBuffer.unbind();
			poseStack.popPose();
		}
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.depthMask(true);
	}
}
