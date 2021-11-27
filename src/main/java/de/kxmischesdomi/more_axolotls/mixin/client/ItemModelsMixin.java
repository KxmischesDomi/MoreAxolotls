package de.kxmischesdomi.more_axolotls.mixin.client;

import de.kxmischesdomi.more_axolotls.MoreAxolotlsClient;
import de.kxmischesdomi.more_axolotls.common.AxolotlVariantManager;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(ItemModels.class)
public abstract class ItemModelsMixin {

	@Shadow public abstract BakedModelManager getModelManager();

	@Shadow public @Nullable abstract BakedModel getModel(Item item);

	@Inject(method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;", at = @At("HEAD"), cancellable = true)
	private void getModel(ItemStack stack, CallbackInfoReturnable<BakedModel> cir) {
		if (stack.isOf(Items.AXOLOTL_BUCKET)) {
			if (stack.getNbt() != null && stack.getNbt().contains("Variant")) {
				int variantId = stack.getNbt().getInt("Variant");
				boolean customNameTexture = stack.hasCustomName() && AxolotlVariantManager.CUSTOM_NAME_VARIANTS.get().containsKey(stack.getName().asString());
				if (variantId != 0 || customNameTexture) {

					AxolotlEntity.Variant variant;
					if (customNameTexture) {
						variant = AxolotlVariantManager.CUSTOM_NAME_VARIANTS.get().get(stack.getName().asString());
					} else {
						variant = AxolotlVariantManager.getVariantById(variantId);
					}

					if (variant != null && MoreAxolotlsClient.VARIANT_BUCKET_MODELS.containsKey(variant)) {
						Identifier id = MoreAxolotlsClient.VARIANT_BUCKET_MODELS.get(variant);
						Map<Identifier, BakedModel> models = ((BakedModelManagerAccessor) getModelManager()).getModels();
						BakedModel bakedModel = models.get(id);
						if (bakedModel != null) cir.setReturnValue(bakedModel);
					}

				}
			}
		}
	}

}
