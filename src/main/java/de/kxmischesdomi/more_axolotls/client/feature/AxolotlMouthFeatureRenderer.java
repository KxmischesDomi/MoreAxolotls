package de.kxmischesdomi.more_axolotls.client.feature;

import de.kxmischesdomi.more_axolotls.common.AxolotlAccessor;
import de.kxmischesdomi.more_axolotls.common.AxolotlVariantManager;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.AxolotlEntityModel;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.util.Identifier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class AxolotlMouthFeatureRenderer extends AbstractAxolotlFeatureRenderer {

	public AxolotlMouthFeatureRenderer(FeatureRendererContext<AxolotlEntity, AxolotlEntityModel<AxolotlEntity>> context) {
		super(context);
	}

	@Override
	public boolean shouldRender(AxolotlEntity entity) {
		return ((entity instanceof AxolotlAccessor accessor && (accessor.getMouthOpenTicks() > 0 || entity.hasCustomName() && entity.getName().asString().equals("pogl")) && AxolotlVariantManager.isSupportedVariant(accessor.getVariantId())));
	}

	@Override
	public Identifier getTexture(AxolotlEntity entity) {
		return new Identifier(String.format("textures/entity/axolotl/mouth/axolotl_%s_mouth.png", entity.getVariant().getName()));
	}

	@Override
	public boolean isBody() {
		return true;
	}

}
