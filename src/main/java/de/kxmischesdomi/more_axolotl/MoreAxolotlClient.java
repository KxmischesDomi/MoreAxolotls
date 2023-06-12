package de.kxmischesdomi.more_axolotl;

import de.kxmischesdomi.more_axolotl.client.screen.AxolotlCatalogScreen;
import de.kxmischesdomi.more_axolotl.common.item.AxolotlCatalogItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
public class MoreAxolotlClient implements ClientModInitializer {

	public static Map<Axolotl.Variant, ResourceLocation> VARIANT_BUCKET_MODELS = new HashMap<>();

	@Override
	public void onInitializeClient() {

		AxolotlCatalogItem.openCatalogAction = player -> {
			AxolotlCatalogScreen screen = new AxolotlCatalogScreen(player.level());
			Minecraft.getInstance().setScreen(screen);
		};

	}

}