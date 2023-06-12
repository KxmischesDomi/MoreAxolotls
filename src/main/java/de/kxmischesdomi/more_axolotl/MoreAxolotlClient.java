package de.kxmischesdomi.more_axolotl;

import de.kxmischesdomi.more_axolotl.client.screen.AxolotlCatalogScreen;
import de.kxmischesdomi.more_axolotl.common.item.AxolotlCatalogItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class MoreAxolotlClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		AxolotlCatalogItem.openCatalogAction = player -> {
			AxolotlCatalogScreen screen = new AxolotlCatalogScreen(player.level);
			Minecraft.getInstance().setScreen(screen);
		};

	}

}