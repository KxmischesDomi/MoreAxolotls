package de.kxmischesdomi.more_axolotl.mixin;

import de.kxmischesdomi.more_axolotl.common.AxolotlBreeds;
import net.minecraft.client.render.entity.AxolotlEntityRenderer;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxolotlEntityRenderer.class)
public abstract class AxolotlEntityRendererMixin {

	@Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
	public void getTexture(AxolotlEntity axolotlEntity, CallbackInfoReturnable<Identifier> cir) {

		if (axolotlEntity.hasCustomName()) {
			String name = axolotlEntity.getName().asString();

			if (name.equals("pogl")) {
				AxolotlEntity.Variant variant = axolotlEntity.getVariant();
				if (AxolotlBreeds.isSupportedVariant(variant)) {
					cir.setReturnValue(new Identifier(String.format("textures/entity/axolotl/pog/axolotl_%s_pog.png", variant.getName())));
				}
				return;
			}

			for (Map.Entry<String, AxolotlEntity.Variant> entry : AxolotlBreeds.CUSTOM_NAME_VARIANTS.get().entrySet()) {
				if (name.equals(entry.getKey())) {
					cir.setReturnValue(new Identifier(String.format("textures/entity/axolotl/axolotl_%s.png", entry.getValue().getName())));
					break;
				}
			}
		}

	}

}
