package de.kxmischesdomi.more_axolotl.client.feature;

import de.kxmischesdomi.more_axolotl.common.AxolotlAccessor;
import de.kxmischesdomi.more_axolotl.common.AxolotlVariantManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

import java.util.List;

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
		return ((entity instanceof AxolotlAccessor accessor && (accessor.getMouthOpenTicks() > 0 || entity.hasCustomName() && entity.getName().getContents().equals("pogl")) && doesTextureExist(getMouthTextureLocation(entity))));
	}

	@Override
	public ResourceLocation getTexture(Axolotl entity) {
		return getMouthTextureLocation(entity);
	}

	private ResourceLocation getMouthTextureLocation(Axolotl entity) {
		ResourceLocation resourceLocation = getSkinTextureName(entity);
		return new ResourceLocation(resourceLocation.getNamespace(), String.format("textures/entity/axolotl/mouth/axolotl_%s_mouth.png", resourceLocation.getPath()));
	}

	 private boolean doesTextureExist(ResourceLocation resourceLocation) {
		List<Resource> stack = Minecraft.getInstance().getResourceManager().getResourceStack(resourceLocation);
		return stack.size() > 0; // Model exists
	}

	@Override
	public boolean isBody() {
		return true;
	}

}
