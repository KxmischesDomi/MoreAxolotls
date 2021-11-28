package de.kxmischesdomi.more_axolotls.client.feature;

import de.kxmischesdomi.more_axolotls.client.RainbowRendering;
import de.kxmischesdomi.more_axolotls.common.AxolotlVariantManager;
import de.kxmischesdomi.more_axolotls.common.CustomAxolotlVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.AxolotlEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.util.Identifier;

import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractAxolotlFeatureRenderer extends FeatureRenderer<AxolotlEntity, AxolotlEntityModel<AxolotlEntity>>{

	private final EntityModel<AxolotlEntity> model;

	public AbstractAxolotlFeatureRenderer(FeatureRendererContext<AxolotlEntity, AxolotlEntityModel<AxolotlEntity>> context) {
		super(context);
		model = context.getModel();
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AxolotlEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (shouldRender(entity)) {

			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(entity)));
			int overlay = isBody() ? LivingEntityRenderer.getOverlay(entity, 0) : OverlayTexture.DEFAULT_UV;

			if (isBody() && entity.getVariant() == CustomAxolotlVariant.RAINBOW.getVariant()) {
				float[] colors = RainbowRendering.getColors(entity);
				this.model.render(matrices, vertexConsumer, light, overlay, colors[0], colors[1], colors[2], 1.0F);
				return;
			}

			this.model.render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public boolean shouldRender(AxolotlEntity entity) {
		return true;
	}

	public boolean isBody() {
		return false;
	}

	public abstract Identifier getTexture(AxolotlEntity entity);

	public static String getSkinTextureName(AxolotlEntity entity) {
		if (entity.hasCustomName()) {
			String name = entity.getName().asString();

			for (Map.Entry<String, AxolotlEntity.Variant> entry : AxolotlVariantManager.CUSTOM_NAME_VARIANTS.get().entrySet()) {
				if (name.equals(entry.getKey())) {
					return entry.getValue().getName();
				}
			}
		}

		return entity.getVariant().getName();
	}

}
