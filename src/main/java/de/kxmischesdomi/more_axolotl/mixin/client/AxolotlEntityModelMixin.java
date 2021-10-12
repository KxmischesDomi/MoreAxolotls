package de.kxmischesdomi.more_axolotl.mixin.client;

import net.minecraft.client.render.entity.model.AxolotlEntityModel;
import net.minecraft.entity.passive.AxolotlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxolotlEntityModel.class)
public abstract class AxolotlEntityModelMixin<T extends AxolotlEntity> {

	@Inject(method = "setAngles(Lnet/minecraft/entity/passive/AxolotlEntity;FFFFF)V", at = @At("HEAD"), cancellable = true)
	public void setAngles(T axolotlEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {

	}


}
