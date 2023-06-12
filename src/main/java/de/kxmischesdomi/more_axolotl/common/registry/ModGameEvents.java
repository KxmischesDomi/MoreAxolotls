package de.kxmischesdomi.more_axolotl.common.registry;

import de.kxmischesdomi.more_axolotl.MoreAxolotl;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModGameEvents {

	public static GameEvent AXOLOTL_EAT = register("axolotl_eat");

	public static void init() {}

	private static GameEvent register(String id) {
		return register(id, 16);
	}

	private static GameEvent register(String id, int range) {
		return Registry.register(BuiltInRegistries.GAME_EVENT, new ResourceLocation(MoreAxolotl.MOD_ID, id), new GameEvent(id, range));
	}

}
