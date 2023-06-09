package de.kxmischesdomi.more_axolotls;

import de.kxmischesdomi.more_axolotls.common.registry.ModGameEvents;
import de.kxmischesdomi.more_axolotls.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public class MoreAxolotls implements ModInitializer {

	public static final String MOD_ID = "more-axolotls";

	@Override
	public void onInitialize() {
		ModItems.init();
		ModGameEvents.init();

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 1, factories -> {
			factories.add((entity, random) -> new MerchantOffer(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.AXOLOTL_CATALOG), 0, 1, 3, 0));
		});

	}
}
