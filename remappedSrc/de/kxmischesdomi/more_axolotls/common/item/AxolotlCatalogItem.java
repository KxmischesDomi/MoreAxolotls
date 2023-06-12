package de.kxmischesdomi.more_axolotl.common.item;

import de.kxmischesdomi.more_axolotl.client.screen.AxolotlCatalogScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class AxolotlCatalogItem extends Item {

	public AxolotlCatalogItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {

		if (world.isClientSide) {
			AxolotlCatalogScreen screen = new AxolotlCatalogScreen(user.level);
			Minecraft.getInstance().setScreen(screen);
		}

		return InteractionResultHolder.pass(user.getItemInHand(hand));
	}

}
