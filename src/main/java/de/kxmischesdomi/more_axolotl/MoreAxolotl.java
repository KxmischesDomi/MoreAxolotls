package de.kxmischesdomi.more_axolotl;

import de.kxmischesdomi.more_axolotl.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class MoreAxolotl implements ModInitializer {

	public static final String MOD_ID = "more-axolotl";

	@Override
	public void onInitialize() {
		ModItems.init();

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 1, factories -> {
			factories.add((entity, random) -> new TradeOffer(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.AXOLOTL_CATALOG), 0, 1, 3, 0));
		});

	}
}
