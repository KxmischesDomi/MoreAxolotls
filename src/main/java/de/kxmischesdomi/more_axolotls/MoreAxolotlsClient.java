package de.kxmischesdomi.more_axolotls;

import de.kxmischesdomi.more_axolotls.client.screen.AxolotlCatalogScreen;
import de.kxmischesdomi.more_axolotls.common.AxolotlVariantManager;
import de.kxmischesdomi.more_axolotls.common.item.AxolotlCatalogItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class MoreAxolotlsClient implements ClientModInitializer {

	public static Map<Axolotl.Variant, ResourceLocation> VARIANT_BUCKET_MODELS = new HashMap<>();

	@Override
	public void onInitializeClient() {

		AxolotlCatalogItem.openCatalogAction = player -> {
			AxolotlCatalogScreen screen = new AxolotlCatalogScreen(player.level());
			Minecraft.getInstance().setScreen(screen);
		};

		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {

			for (Axolotl.Variant variant : Axolotl.Variant.values()) {
				if (variant.getId() == 0) continue;
				if (AxolotlVariantManager.isSupportedVariant(variant.getId())) {
					ResourceLocation identifier = new ResourceLocation(MoreAxolotls.MOD_ID, String.format("item/axolotl_bucket_%s", variant.getName()));
					VARIANT_BUCKET_MODELS.put(variant, identifier);
					out.accept(identifier);
				}
			}
		});

	}

}