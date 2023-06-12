package de.kxmischesdomi.more_axolotl;

import de.kxmischesdomi.more_axolotl.common.AxolotlAccessor;
import de.kxmischesdomi.more_axolotl.common.registry.ModGameEvents;
import de.kxmischesdomi.more_axolotl.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;

public class MoreAxolotls implements ModInitializer {

	public static final String MOD_ID = "more-axolotl";

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
