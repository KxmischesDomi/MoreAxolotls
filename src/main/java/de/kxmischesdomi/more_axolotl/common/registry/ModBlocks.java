package de.kxmischesdomi.more_axolotl.common.registry;

import de.kxmischesdomi.more_axolotl.MoreAxolotl;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModBlocks {

	private static <T extends Block> T register(String name, T block) {
		Registry.register(Registry.BLOCK, new Identifier(MoreAxolotl.MOD_ID, name), block);
		return block;
	}

}
