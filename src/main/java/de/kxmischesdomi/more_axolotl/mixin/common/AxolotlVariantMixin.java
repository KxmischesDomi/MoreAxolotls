package de.kxmischesdomi.more_axolotl.mixin.common;

import de.kxmischesdomi.more_axolotl.common.AxolotlVariantManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.axolotl.Axolotl.Variant;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(value = Axolotl.Variant.class, priority = 10000)
public abstract class AxolotlVariantMixin {

	@Inject(method = "getRareSpawnVariant", at = @At("HEAD"), cancellable = true)
	private static void getBreedOffspring(RandomSource random, CallbackInfoReturnable<Variant> cir) {
		cir.setReturnValue(AxolotlVariantManager.getRandomBreed(random));
	}

}
