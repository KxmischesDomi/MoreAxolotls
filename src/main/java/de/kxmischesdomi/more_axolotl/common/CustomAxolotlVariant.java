package de.kxmischesdomi.more_axolotl.common;

import net.minecraft.world.entity.animal.axolotl.Axolotl;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public enum CustomAxolotlVariant {

	ALBINO("albino", 1, true),
	BLACK("black", 2, true),
	CHIMERA("chimera", 3, false),
	ENIGMA("enigma", 4, false),
	FIREFLY("firefly", 5, false),
	FIREFLY_INVERTED("firefly_inverted", 6, false),
	LAPIS("lapis", 7, true),
	NEON("neon", 8, true),
	GLOW("glow", 9, true),
	PIEBALD("piebald", 10, false),
	GRAY("gray", 11, true),
	SHINY("shiny", 12, false),
	MOSAIC_BLACK("mosaic_black", 13, false),
	MOSAIC_WILD("mosaic_wild", 17, false),
	SKELETON("skeleton", 14, false),
	DROWNED("drowned", 15, false),
	RAINBOW("rainbow", 16, false),
	SCULK("sculk", 18, false),
	;

	private final String name;
	private final int index;
	private final boolean natural;
	private Axolotl.Variant variant;

	CustomAxolotlVariant(String name, int index, boolean natural) {
		this.name = name;
		this.index = index;
		this.natural = natural;
	}

	public String getName() {
		return this.name;
	}

	public int getIndex() {
		return index;
	}

	public boolean isNatural() {
		return natural;
	}

	public Axolotl.Variant getVariant() {
		return variant;
	}

	public void setVariant(Axolotl.Variant variant) {
		this.variant = variant;
	}

	static {
		Axolotl.Variant.values(); // Ensure class is loaded before the variant is accessed
	}

}
