package de.kxmischesdomi.more_axolotl.client.feature;

import de.kxmischesdomi.more_axolotl.common.AxolotlAccessor;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.AxolotlEntityModel;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.util.Identifier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class AxolotlLeafFeatureRenderer extends AbstractAxolotlFeatureRenderer {

	public AxolotlLeafFeatureRenderer(FeatureRendererContext<AxolotlEntity, AxolotlEntityModel<AxolotlEntity>> context) {
		super(context);
	}

	@Override
	public boolean shouldRender(AxolotlEntity entity) {
		return ((AxolotlAccessor) entity).hasLeaf();
	}

	@Override
	public Identifier getTexture(AxolotlEntity entity) {

		float durability = ((AxolotlAccessor) entity).getLeafDurability();
		String texture = "repaired";

		if (durability <= 5) {
			texture = "damaged";
		} else if (durability <= 10) {
			texture = "damaging";
		}

		return new Identifier(String.format("textures/entity/axolotl/utilities/axolotl_leaf_armor_%s.png", texture));
	}

}
