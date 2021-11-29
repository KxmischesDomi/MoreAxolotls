package de.kxmischesdomi.more_axolotls.mixin.client;

import de.kxmischesdomi.more_axolotls.MoreAxolotlsClient;
import de.kxmischesdomi.more_axolotls.common.AxolotlVariantManager;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
@Mixin(ItemModelShaper.class)
public abstract class ItemModelShaperMixin {

	@Shadow public abstract ModelManager getModelManager();

	@Inject(method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;", at = @At("HEAD"), cancellable = true)
	private void getModel(ItemStack stack, CallbackInfoReturnable<BakedModel> cir) {
		if (stack.is(Items.AXOLOTL_BUCKET)) {
			if (stack.getTag() != null && stack.getTag().contains("Variant")) {
				int variantId = stack.getTag().getInt("Variant");
				boolean customNameTexture = stack.hasCustomHoverName() && AxolotlVariantManager.CUSTOM_NAME_VARIANTS.get().containsKey(stack.getHoverName().getContents());
				if (variantId != 0 || customNameTexture) {

					Axolotl.Variant variant;
					if (customNameTexture) {
						variant = AxolotlVariantManager.CUSTOM_NAME_VARIANTS.get().get(stack.getHoverName().getContents());
					} else {
						variant = AxolotlVariantManager.getVariantById(variantId);
					}

					if (variant != null && MoreAxolotlsClient.VARIANT_BUCKET_MODELS.containsKey(variant)) {
						ResourceLocation id = MoreAxolotlsClient.VARIANT_BUCKET_MODELS.get(variant);
						Map<ResourceLocation, BakedModel> models = ((ModelManagerAccessor) getModelManager()).getBakedRegistry();
						BakedModel bakedModel = models.get(id);
						if (bakedModel != null) cir.setReturnValue(bakedModel);
					}

				}
			}
		}
	}

}
