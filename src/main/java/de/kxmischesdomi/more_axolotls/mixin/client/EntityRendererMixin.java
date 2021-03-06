package de.kxmischesdomi.more_axolotls.mixin.client;

import de.kxmischesdomi.more_axolotls.common.CustomAxolotlVariant;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Akashii_Kun_
 * @since 1.0
 */
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

	@Inject(method = "getBlockLightLevel", at = @At("TAIL"), cancellable = true)
	protected void getBlockLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if (entity instanceof Axolotl axolotl) {
			Axolotl.Variant variant = axolotl.getVariant();
			if (variant == CustomAxolotlVariant.GLOW.getVariant())
				cir.setReturnValue(15);
		}
	}

}