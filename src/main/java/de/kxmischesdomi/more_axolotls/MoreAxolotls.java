package de.kxmischesdomi.more_axolotls;

import de.kxmischesdomi.more_axolotls.common.AxolotlAccessor;
import de.kxmischesdomi.more_axolotls.common.registry.ModGameEvents;
import de.kxmischesdomi.more_axolotls.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class MoreAxolotls implements ModInitializer {

	public static final String MOD_ID = "more-axolotls";

	@Override
	public void onInitialize() {
		ModItems.init();
		ModGameEvents.init();

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 1, factories -> {
			factories.add((entity, random) -> new TradeOffer(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.AXOLOTL_CATALOG), 0, 1, 3, 0));
		});

		// TEMPORARILY IMPLEMENTATION OF THE OPEN MOUTH
		// TODO: IMPLEMENT OPEN MOUTH INTO AI WHEN HUNTING
		UseEntityCallback.EVENT.register((player, world1, hand, entity, hitResult) -> {

			if (world1.isClient) {
				if (entity instanceof AxolotlAccessor accessor) {
					if (player.getStackInHand(hand).isOf(Items.TROPICAL_FISH_BUCKET)) {
						accessor.setMouthOpenTicks(10);
					}

				}
			}


			return ActionResult.PASS;
		});

	}
}
