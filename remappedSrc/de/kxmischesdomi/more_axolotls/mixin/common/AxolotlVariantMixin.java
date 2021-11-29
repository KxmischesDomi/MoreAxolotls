package de.kxmischesdomi.more_axolotls.mixin.common;

import de.kxmischesdomi.more_axolotls.common.CustomAxolotlVariant;
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
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.axolotl.Axolotl.Variant;

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
		int currentId = 99; // Starting at 99 to prevent issues with other mods that add axolotl variants
		Variant last = variants.get(variants.size() - 1);

		for (CustomAxolotlVariant value : CustomAxolotlVariant.values()) {
			last = newVariant(value.name(), last.ordinal() + 1, currentId + 1, value.getName(), value.isNatural());
			value.setVariant(last);
			variants.add(last);
			currentId++;
		}

		field_28350 = variants.toArray(new Axolotl.Variant[0]);
	}

}
