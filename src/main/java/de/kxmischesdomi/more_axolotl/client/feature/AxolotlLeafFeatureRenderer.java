package de.kxmischesdomi.more_axolotl.client.feature;

import de.kxmischesdomi.more_axolotl.MoreAxolotl;
import de.kxmischesdomi.more_axolotl.common.AxolotlAccessor;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class AxolotlLeafFeatureRenderer extends AbstractAxolotlFeatureRenderer {

	public AxolotlLeafFeatureRenderer(RenderLayerParent<Axolotl, AxolotlModel<Axolotl>> context) {
		super(context);
	}

	@Override
	public boolean shouldRender(Axolotl entity) {
		return ((AxolotlAccessor) entity).hasLeaf();
	}

	@Override
	public ResourceLocation getTexture(Axolotl entity) {

		float durability = ((AxolotlAccessor) entity).getLeafDurability();
		String texture = "repaired";

		if (durability <= 5) {
			texture = "damaged";
		} else if (durability <= 10) {
			texture = "damaging";
		}

		return new ResourceLocation(MoreAxolotl.MOD_ID, String.format("textures/entity/axolotl/utilities/axolotl_leaf_armor_%s.png", texture));
	}

}
