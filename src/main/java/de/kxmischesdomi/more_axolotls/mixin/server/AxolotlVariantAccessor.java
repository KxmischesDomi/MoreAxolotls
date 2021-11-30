package de.kxmischesdomi.more_axolotls.mixin.server;

import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Axolotl.Variant.class)
public interface AxolotlVariantAccessor {

	@Accessor("common")
	boolean isCommon();

}
