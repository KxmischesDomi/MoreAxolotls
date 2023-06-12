package de.kxmischesdomi.more_axolotl.mixin.client;

import de.kxmischesdomi.more_axolotl.common.AxolotlVariantManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Locale;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(MobBucketItem.class)
public abstract class EntityBucketItemMixin {

	@Shadow @Final private EntityType<?> entityType;

	@Inject(method = "appendTooltip", at = @At(value = "HEAD"))
	public void appendTooltip(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context, CallbackInfo ci) {
		if (entityType == EntityType.AXOLOTL) {
			CompoundTag nbtCompound = stack.getTag();
			if (nbtCompound != null && nbtCompound.contains("Variant")) {
				int variantId = nbtCompound.getInt("Variant");

				Axolotl.Variant variant = AxolotlVariantManager.getVariantById(variantId);
				if (variant != null) {
					String titleName = variant.getName().replace("_", " ");
					titleName = String.valueOf(titleName.charAt(0)).toUpperCase(Locale.ROOT) + titleName.substring(1);
					Component title = new TextComponent("§7§o" + titleName);
					tooltip.add(title);
				}

			}
		}
	}

}
