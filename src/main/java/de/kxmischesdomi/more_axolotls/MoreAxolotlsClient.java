package de.kxmischesdomi.more_axolotls;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class MoreAxolotlsClient implements ClientModInitializer {

	public static Map<AxolotlEntity.Variant, Identifier> VARIANT_BUCKET_MODELS = new HashMap<>();

	@Override
	public void onInitializeClient() {

		for (AxolotlEntity.Variant variant : AxolotlEntity.Variant.values()) {
			if (variant.getId() == 0) continue;
			ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
				Identifier identifier = new Identifier(MoreAxolotls.MOD_ID, String.format("item/axolotl_bucket_%s", variant.getName()));
				VARIANT_BUCKET_MODELS.put(variant, identifier);
				out.accept(identifier);
			});
		}

	}

}