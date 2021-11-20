package de.kxmischesdomi.more_axolotls.common.item;

import de.kxmischesdomi.more_axolotls.client.screen.AxolotlCatalogScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class AxolotlCatalogItem extends Item {

	public AxolotlCatalogItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

		if (world.isClient) {
			AxolotlCatalogScreen screen = new AxolotlCatalogScreen(user.world);
			MinecraftClient.getInstance().setScreen(screen);
		}

		return TypedActionResult.pass(user.getStackInHand(hand));
	}

}
