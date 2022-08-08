package de.kxmischesdomi.more_axolotls.client.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.kxmischesdomi.more_axolotls.client.RainbowRendering;
import de.kxmischesdomi.more_axolotls.common.AxolotlVariantManager;
import de.kxmischesdomi.more_axolotls.common.CustomAxolotlVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractAxolotlFeatureRenderer extends RenderLayer<Axolotl, AxolotlModel<Axolotl>>{

	private final EntityModel<Axolotl> model;

	public AbstractAxolotlFeatureRenderer(RenderLayerParent<Axolotl, AxolotlModel<Axolotl>> context) {
		super(context);
		model = context.getModel();
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, Axolotl entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (shouldRender(entity)) {

			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.entityCutoutNoCull(getTexture(entity)));
			int overlay = isBody() ? LivingEntityRenderer.getOverlayCoords(entity, 0) : OverlayTexture.NO_OVERLAY;

			if (isBody() && entity.getVariant() == CustomAxolotlVariant.RAINBOW.getVariant()) {
				float[] colors = RainbowRendering.getColors(entity);
				this.model.renderToBuffer(matrices, vertexConsumer, light, overlay, colors[0], colors[1], colors[2], 1.0F);
				return;
			}

			this.model.renderToBuffer(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public boolean shouldRender(Axolotl entity) {
		return true;
	}

	public boolean isBody() {
		return false;
	}

	public abstract ResourceLocation getTexture(Axolotl entity);

	public static String getSkinTextureName(Axolotl entity) {
		if (entity.hasCustomName()) {
			String name = entity.getName().getString();

			for (Map.Entry<String, Axolotl.Variant> entry : AxolotlVariantManager.CUSTOM_NAME_VARIANTS.get().entrySet()) {
				if (name.equalsIgnoreCase(entry.getKey())) {
					return entry.getValue().getName();
				}
			}
		}

		return entity.getVariant().getName();
	}

}
