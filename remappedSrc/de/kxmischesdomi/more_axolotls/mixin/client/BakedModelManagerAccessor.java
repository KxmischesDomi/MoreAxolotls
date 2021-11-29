package de.kxmischesdomi.more_axolotls.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(ModelManager.class)
public interface BakedModelManagerAccessor {

	@Accessor("models")
	Map<ResourceLocation, BakedModel> getModels();

}
