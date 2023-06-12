package de.kxmischesdomi.more_axolotl.common.registry;

import de.kxmischesdomi.more_axolotl.MoreAxolotl;
import de.kxmischesdomi.more_axolotl.common.item.AxolotlCatalogItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModItems {

	public static Item AXOLOTL_CATALOG = register("axolotl_catalog", new AxolotlCatalogItem(new Item.Properties().stacksTo(1)));

	public static void init() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.accept(AXOLOTL_CATALOG);
		});
	}

	public static <T extends Item> T register(String name, T item) {
		Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(MoreAxolotl.MOD_ID, name), item);
		return item;
	}

}
    