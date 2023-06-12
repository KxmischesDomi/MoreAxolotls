package de.kxmischesdomi.more_axolotl.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

	@Override
	public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
		MutableComponent component = Component.translatable("item.more-axolotl.axolotl_catalog.tooltip");
		component.setStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.GRAY));
		list.add(component);
		super.appendHoverText(itemStack, level, list, tooltipFlag);
	}

}
