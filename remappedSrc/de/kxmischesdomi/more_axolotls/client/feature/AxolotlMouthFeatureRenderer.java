package de.kxmischesdomi.more_axolotl.client.feature;

import de.kxmischesdomi.more_axolotl.common.AxolotlAccessor;
import de.kxmischesdomi.more_axolotl.common.AxolotlVariantManager;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class AxolotlMouthFeatureRenderer extends AbstractAxolotlFeatureRenderer {

	public AxolotlMouthFeatureRenderer(RenderLayerParent<Axolotl, AxolotlModel<Axolotl>> context) {
		super(context);
	}

	@Override
	public boolean shouldRender(Axolotl entity) {
		return ((entity instanceof AxolotlAccessor accessor && (accessor.getMouthOpenTicks() > 0 || entity.hasCustomName() && entity.getName().asString().equals("pogl")) && AxolotlVariantManager.isSupportedVariant(accessor.getVariantId())));
	}

	@Override
	public ResourceLocation getTexture(Axolotl entity) {
		return new ResourceLocation(String.format("textures/entity/axolotl/mouth/axolotl_%s_mouth.png", getSkinTextureName(entity)));
	}

	@Override
	public boolean isBody() {
		return true;
	}

}
