package de.kxmischesdomi.more_axolotls.common;

import net.minecraft.world.entity.animal.axolotl.Axolotl;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public enum CustomAxolotlVariant {

	ALBINO("albino", true),
	BLACK("black", true),
	CHIMERA("chimera", false),
	ENIGMA("enigma", false),
	FIREFLY("firefly", false),
	FIREFLY_INVERTED("firefly_inverted", false),
	LAPIS("lapis", true),
	NEON("neon", true),
	GLOW("glow", true),
	PIEBALD("piebald", false),
	GRAY("gray", true),
	SHINY("shiny", false),
	MOSAIC("mosaic", false),
	SKELETON("skeleton", false),
	DROWNED("drowned", false),
	RAINBOW("rainbow", false),
	;

	private final String name;
	private final boolean natural;
	private Axolotl.Variant variant;

	CustomAxolotlVariant(String name, boolean natural) {
		this.name = name;
		this.natural = natural;
	}

	public String getName() {
		return this.name;
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
