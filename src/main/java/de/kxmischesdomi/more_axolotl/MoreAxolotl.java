package de.kxmischesdomi.more_axolotl;

import de.kxmischesdomi.more_axolotl.common.CustomAxolotlVariant;
import de.kxmischesdomi.more_axolotl.common.registry.ModItems;
import fabric.io.github.akashiikun.mavapi.v1.api.ModdedAxolotlVariant;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public class MoreAxolotl implements ModInitializer {

	public static final String MOD_ID = "more-axolotl";

	@Override
	public void onInitialize() {
		ModItems.init();

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 1, factories -> {
			factories.add((entity, random) -> new MerchantOffer(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, 3), new ItemStack(ModItems.AXOLOTL_CATALOG), 0, 1, 3, 0));
		});

		for (CustomAxolotlVariant variant : CustomAxolotlVariant.values()) {
			ModdedAxolotlVariant.Builder builder = ModdedAxolotlVariant.register(new ResourceLocation(MOD_ID, variant.getName()));

			if (variant.isNatural()) {
				builder.natural();
			}

			Axolotl.Variant build = builder.build();
			variant.setVariant(build);


		}

	}
}
