package de.kxmischesdomi.more_axolotls.mixin.client;

import de.kxmischesdomi.more_axolotls.client.feature.AbstractAxolotlFeatureRenderer;
import de.kxmischesdomi.more_axolotls.client.feature.AxolotlLeafFeatureRenderer;
import de.kxmischesdomi.more_axolotls.client.feature.AxolotlMouthFeatureRenderer;
import net.minecraft.client.render.entity.AxolotlEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.AxolotlEntityModel;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxolotlEntityRenderer.class)
public abstract class AxolotlEntityRendererMixin extends MobEntityRenderer<AxolotlEntity, AxolotlEntityModel<AxolotlEntity>> {

	public AxolotlEntityRendererMixin(EntityRendererFactory.Context context, AxolotlEntityModel<AxolotlEntity> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(EntityRendererFactory.Context context, CallbackInfo ci) {
		this.addFeature(new AxolotlMouthFeatureRenderer(this));
		this.addFeature(new AxolotlLeafFeatureRenderer(this));
	}

	@Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
	public void getTexture(AxolotlEntity axolotlEntity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(new Identifier(String.format("textures/entity/axolotl/axolotl_%s.png", AbstractAxolotlFeatureRenderer.getSkinTextureName(axolotlEntity))));
	}

}
