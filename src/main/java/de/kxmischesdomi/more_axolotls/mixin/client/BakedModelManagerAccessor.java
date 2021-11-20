package de.kxmischesdomi.more_axolotls.mixin.client;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(BakedModelManager.class)
public interface BakedModelManagerAccessor {

	@Accessor("models")
	Map<Identifier, BakedModel> getModels();

}
