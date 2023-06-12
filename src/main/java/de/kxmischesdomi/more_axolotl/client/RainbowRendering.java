package de.kxmischesdomi.more_axolotl.client;

import de.kxmischesdomi.more_axolotl.common.AxolotlAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RainbowRendering {

	public static float[] getColors(LivingEntity entity) {
		int age = entity.tickCount + (entity instanceof AxolotlAccessor ? ((AxolotlAccessor) entity).getGuiAge() : 0);
		int n = age / 25;
		int o = DyeColor.values().length;
		int p = n % o;
		int q = (n + 1) % o;
		float r = ((float)(age % 25)) / 25.0F;
		float[] fs = Sheep.getColorArray(DyeColor.byId(p));
		float[] gs = Sheep.getColorArray(DyeColor.byId(q));
		float v = fs[0] * (1.0F - r) + gs[0] * r;
		float w = fs[1] * (1.0F - r) + gs[1] * r;
		float x = fs[2] * (1.0F - r) + gs[2] * r;

		return new float[] {v, w, x};
	}

}
