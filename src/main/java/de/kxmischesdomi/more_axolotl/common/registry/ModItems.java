package de.kxmischesdomi.more_axolotl.common.registry;

import de.kxmischesdomi.more_axolotl.MoreAxolotl;
import de.kxmischesdomi.more_axolotl.common.item.AxolotlCatalogItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModItems {

	public static Item AXOLOTL_CATALOG = register("axolotl_catalog", new AxolotlCatalogItem(new Item.Settings().group(ItemGroup.MISC)));

	public static void init() {}

	public static <T extends Item> T register(String name, T item) {
		Registry.register(Registry.ITEM, new Identifier(MoreAxolotl.MOD_ID, name), item);
		return item;
	}

}
    