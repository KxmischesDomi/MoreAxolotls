package de.kxmischesdomi.more_axolotl.common.registry;

import de.kxmischesdomi.more_axolotl.MoreAxolotls;
import de.kxmischesdomi.more_axolotl.common.item.AxolotlCatalogItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModItems {

	public static Item AXOLOTL_CATALOG = register("axolotl_catalog", new AxolotlCatalogItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

	public static void init() {}

	public static <T extends Item> T register(String name, T item) {
		Registry.register(Registry.ITEM, new ResourceLocation(MoreAxolotls.MOD_ID, name), item);
		return item;
	}

}
    