package de.kxmischesdomi.more_axolotl.mixin;

import de.kxmischesdomi.more_axolotl.common.CustomAxolotlVariant;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.AxolotlEntity.Variant;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxolotlEntity.Variant.class)
public class AxolotlVariantMixin {

	@SuppressWarnings("InvokerTarget")
	@Invoker("<init>")
	private static AxolotlEntity.Variant newVariant(String internalName, int internalId, int id, String name, boolean natural) {
		throw new AssertionError();
	}

	@SuppressWarnings("ShadowTarget")
	@Shadow
	@Mutable
	private static @Final Variant[] field_28350;

	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(method = "<clinit>", at = @At(value = "FIELD",
			opcode = Opcodes.PUTSTATIC,
			target = "Lnet/minecraft/entity/passive/AxolotlEntity$Variant;field_28350:[Lnet/minecraft/entity/passive/AxolotlEntity$Variant;",
			shift = At.Shift.AFTER))
	private static void addCustomVariant(CallbackInfo ci) {
		List<Variant> variants = new ArrayList<>(Arrays.asList(field_28350));
		Variant last = variants.get(variants.size() - 1);

		for (CustomAxolotlVariant value : CustomAxolotlVariant.values()) {
			last = newVariant(value.name(), last.ordinal() + 1, last.getId() + 1, value.getName(), value.isNatural());
			value.setVariant(last);
			variants.add(last);
		}

		field_28350 = variants.toArray(new AxolotlEntity.Variant[0]);
	}

}
