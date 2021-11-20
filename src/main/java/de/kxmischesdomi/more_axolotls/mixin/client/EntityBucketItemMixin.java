package de.kxmischesdomi.more_axolotls.mixin.client;

import de.kxmischesdomi.more_axolotls.common.AxolotlVariantManager;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Locale;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(EntityBucketItem.class)
public abstract class EntityBucketItemMixin {

	@Shadow @Final private EntityType<?> entityType;

	@Inject(method = "appendTooltip", at = @At(value = "HEAD"))
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
		if (entityType == EntityType.AXOLOTL) {
			NbtCompound nbtCompound = stack.getNbt();
			if (nbtCompound != null && nbtCompound.contains("Variant")) {
				int variantId = nbtCompound.getInt("Variant");

				AxolotlEntity.Variant variant = AxolotlVariantManager.getVariantById(variantId);
				System.out.println(variant);
				if (variant != null) {
					String titleName = variant.getName().replace("_", " ");
					titleName = String.valueOf(titleName.charAt(0)).toUpperCase(Locale.ROOT) + titleName.substring(1);
					Text title = new LiteralText("§7§o" + titleName);
					tooltip.add(title);
				}

			}
		}
	}

}
