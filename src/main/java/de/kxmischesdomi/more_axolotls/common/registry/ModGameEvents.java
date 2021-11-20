package de.kxmischesdomi.more_axolotls.common.registry;

import de.kxmischesdomi.more_axolotls.MoreAxolotls;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

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
		return Registry.register(Registry.GAME_EVENT, new Identifier(MoreAxolotls.MOD_ID, id), new GameEvent(id, range));
	}

}
