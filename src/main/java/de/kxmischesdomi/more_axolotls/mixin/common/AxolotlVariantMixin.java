package de.kxmischesdomi.more_axolotls.mixin.common;

import de.kxmischesdomi.more_axolotls.common.AxolotlVariantManager;
import de.kxmischesdomi.more_axolotls.common.CustomAxolotlVariant;
import net.minecraft.util.ByIdMap;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(value = Axolotl.Variant.class, priority = 10000)
public abstract class AxolotlVariantMixin {

	@SuppressWarnings("InvokerTarget")
	@Invoker("<init>")
	private static Axolotl.Variant newVariant(String internalName, int internalId, int id, String name, boolean natural) {
		throw new AssertionError();
	}

	@SuppressWarnings("ShadowTarget")
	@Shadow @Mutable @Final private static Variant[] $VALUES;

	@Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ByIdMap;continuous(Ljava/util/function/ToIntFunction;[Ljava/lang/Object;Lnet/minecraft/util/ByIdMap$OutOfBoundsStrategy;)Ljava/util/function/IntFunction;"))
	private static <T> IntFunction<Variant> byIdMap(ToIntFunction<T> toIntFunction, T[] objects, ByIdMap.OutOfBoundsStrategy outOfBoundsStrategy) {
		return AxolotlVariantManager::getVariantById;
	}

	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(method = "<clinit>", at = @At(value = "FIELD",
			opcode = Opcodes.PUTSTATIC,
			target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl$Variant;$VALUES:[Lnet/minecraft/world/entity/animal/axolotl/Axolotl$Variant;",
			shift = At.Shift.AFTER))
	private static void addCustomVariant(CallbackInfo ci) {
		List<Variant> variants = new ArrayList<>(Arrays.asList($VALUES));

		final int startId = 99; // Starting at 99 to prevent issues with other mods that add axolotl variants
		Variant last = variants.get(variants.size() - 1);

		for (CustomAxolotlVariant value : CustomAxolotlVariant.values()) {
			last = newVariant(value.name(), last.ordinal() + 1, startId + value.getIndex(), value.getName(), value.isNatural());
			value.setVariant(last);
			variants.add(last);
		}

		$VALUES = variants.toArray(new Axolotl.Variant[0]);
	}

	@Inject(method = "getRareSpawnVariant", at = @At("HEAD"), cancellable = true)
	private static void getBreedOffspring(RandomSource random, CallbackInfoReturnable<Variant> cir) {
		cir.setReturnValue(AxolotlVariantManager.getRandomBreed(random));
	}

}
