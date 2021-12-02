package de.kxmischesdomi.more_axolotls.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class AxolotlCatalogItem extends Item {

	public static Consumer<Player> openCatalogAction = player -> {};

	public AxolotlCatalogItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {

		if (world.isClientSide) {
			openCatalogAction.accept(user);
		}

		return InteractionResultHolder.pass(user.getItemInHand(hand));
	}

}
