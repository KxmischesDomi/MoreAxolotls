package de.kxmischesdomi.more_axolotl.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AxolotlEntityModel;
import net.minecraft.entity.passive.AxolotlEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxolotlEntityModel.class)
public abstract class AxolotlEntityModelMixin <T extends AxolotlEntity> {

	@Shadow @Final private ModelPart leftHindLeg;

	@Shadow protected abstract void setMovingOnGroundAngles(float animationProgress, float headYaw);

	@Shadow public abstract void setAngles(T axolotlEntity, float f, float g, float h, float i, float j);

	@Inject(method = "setAngles(Lnet/minecraft/entity/passive/AxolotlEntity;FFFFF)V", at = @At(value = "HEAD"))
	public void setAngles(T axolotlEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if (axolotlEntity.age == 951753697) {
			axolotlEntity.age = 0;
			for (int i1 = 0; i1 < 500; i1++) {
				setAngles(axolotlEntity, f, g, h, i, j);
			}
		}

	}

}
