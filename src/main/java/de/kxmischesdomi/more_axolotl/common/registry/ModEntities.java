package de.kxmischesdomi.more_axolotl.common.registry;

import de.kxmischesdomi.more_axolotl.MoreAxolotl;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModEntities {

	private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(MoreAxolotls.MOD_ID, id), builder.build());
	}

}
