package de.kxmischesdomi.more_axolotl.mixin.client;

import de.kxmischesdomi.more_axolotl.client.feature.AbstractAxolotlFeatureRenderer;
import de.kxmischesdomi.more_axolotl.client.feature.AxolotlLeafFeatureRenderer;
import de.kxmischesdomi.more_axolotl.client.feature.AxolotlMouthFeatureRenderer;
import de.kxmischesdomi.more_axolotl.common.AxolotlVariantManager;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxolotlRenderer.class)
public abstract class AxolotlRendererMixin extends MobRenderer<Axolotl, AxolotlModel<Axolotl>> {

	public AxolotlRendererMixin(EntityRendererProvider.Context context, AxolotlModel<Axolotl> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(EntityRendererProvider.Context context, CallbackInfo ci) {
		this.addLayer(new AxolotlMouthFeatureRenderer(this));
		this.addLayer(new AxolotlLeafFeatureRenderer(this));
	}

	@Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/axolotl/Axolotl;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
	public void getTexture(Axolotl axolotlEntity, CallbackInfoReturnable<ResourceLocation> cir) {

		ResourceLocation resourceLocation = AbstractAxolotlFeatureRenderer.getSkinTextureName(axolotlEntity);
		if (!resourceLocation.equals(new ResourceLocation(axolotlEntity.getVariant().getName()))) {
			ResourceLocation location = new ResourceLocation(resourceLocation.getNamespace(), "textures/entity/axolotl/axolotl_" + resourceLocation.getPath() + ".png");
			cir.setReturnValue(location);
		}
	}

}
