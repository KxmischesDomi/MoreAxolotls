package de.kxmischesdomi.more_axolotl.mixin.server;

import net.minecraft.entity.passive.AxolotlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxolotlEntity.Variant.class)
public interface AxolotlVariantAccessor {

	@Accessor("natural")
	boolean isNatural();

}
