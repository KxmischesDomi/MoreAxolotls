package de.kxmischesdomi.more_axolotls.common.registry;

import de.kxmischesdomi.more_axolotls.MoreAxolotls;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModBlocks {

	private static <T extends Block> T register(String name, T block) {
		Registry.register(Registry.BLOCK, new ResourceLocation(MoreAxolotls.MOD_ID, name), block);
		return block;
	}

}
