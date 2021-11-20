package de.kxmischesdomi.more_axolotls.common;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import de.kxmischesdomi.more_axolotls.mixin.server.AxolotlVariantAccessor;
import net.minecraft.entity.passive.AxolotlEntity.Variant;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class AxolotlVariantManager {

	private static final Supplier<Map<Integer, Variant>> ID_TO_VARIANT = Suppliers.memoize(() -> {
		ImmutableBiMap.Builder<Integer, Variant> builder = ImmutableBiMap.<Integer, Variant>builder();
		for (Variant variant : Variant.values()) {
			builder.put(variant.getId(), variant);
		}
		return builder.build();
	});

	// VARIANTS THAT ARE OBTAINABLE BY BREEDING SPECIFIC VARIANTS WITH EACH OTHER
	public static Supplier<Map<Variant[], Variant[]>> BREEDS = Suppliers.memoize(() -> {
		return ImmutableBiMap.<Variant[], Variant[]>builder()
				.put(new Variant[]{Variant.GOLD, CustomAxolotlVariant.BLACK.getVariant()}, new Variant[]{CustomAxolotlVariant.ENIGMA.getVariant()})
				.put(new Variant[]{Variant.LUCY, Variant.WILD}, new Variant[]{CustomAxolotlVariant.CHIMERA.getVariant()})
				.put(new Variant[]{CustomAxolotlVariant.ALBINO.getVariant(), CustomAxolotlVariant.BLACK.getVariant()}, new Variant[]{CustomAxolotlVariant.FIREFLY.getVariant(), CustomAxolotlVariant.PIEBALD.getVariant(), CustomAxolotlVariant.FIREFLY_INVERTED.getVariant()})
				.put(new Variant[]{CustomAxolotlVariant.LAPIS.getVariant(), Variant.LUCY}, new Variant[]{CustomAxolotlVariant.MOSAIC.getVariant()})
				.build();
	});

	// VARIANTS WITH WHICH SPECIFIC VARIANTS ARE BRED
	public static Supplier<Map<Variant[], Variant[]>> BREED_PARENTS = Suppliers.memoize(() -> {
		return ((BiMap)BREEDS.get()).inverse();
	});

	// VARIANTS THAT ARE OBTAINABLE BY RENAMING AXOLOTL
	public static Supplier<Map<String, Variant>> CUSTOM_NAME_VARIANTS = Suppliers.memoize(() -> {
		return ImmutableBiMap.<String, Variant>builder()
				.put("skelotl", CustomAxolotlVariant.SKELETON.getVariant())
				.put("drownelotl", CustomAxolotlVariant.DROWNED.getVariant())
				.build();
	});

	// VARIANTS THAT ARE OBTAINED BY RARE BREEDING RESULTS
	public static Supplier<HashSet<Variant>> RARE_BREEDS = Suppliers.memoize(() -> {
		HashSet<Variant> variants = new HashSet<>(Arrays.asList(Variant.values()));
		variants.addAll(Arrays.stream(CustomAxolotlVariant.values()).map(CustomAxolotlVariant::getVariant).collect(Collectors.toList()));
		variants.removeIf(variant -> ((AxolotlVariantAccessor)((Object) variant)).isNatural());
		for (Variant value : CUSTOM_NAME_VARIANTS.get().values()) {
			variants.remove(value);
		}
		for (Variant[] value : BREEDS.get().values()) {
			for (Variant variant : value) {
				variants.remove(variant);
			}
		}
		return variants;
	});

	public static Variant getRandomBreed(Random random) {
		Variant[] variants = RARE_BREEDS.get().toArray(new Variant[0]);
		return variants[random.nextInt(variants.length)];
	}

	public static Variant getBreed(Variant parent1, Variant parent2, Random random) {
		for (Map.Entry<Variant[], Variant[]> entry : BREEDS.get().entrySet()) {
			List<Variant> variants = Arrays.asList(entry.getKey());
			if (variants.contains(parent1) && variants.contains(parent2)) {
				return entry.getValue()[random.nextInt(entry.getValue().length)];
			}
		}

		return null;
	}

	public static Variant getVariantById(int id) {
		return ID_TO_VARIANT.get().get(id);
	}

	// TODO: REPLACE WITH HASHSET SUPPLIER
	public static boolean isSupportedVariant(int id) {

		if (id > 4) {
			boolean custom = false;
			for (CustomAxolotlVariant value : CustomAxolotlVariant.values()) {
				if (value.getVariant().getId() == id) {
					custom = true;
					break;
				}
			}

			return custom;
		}

		return true;
	}

}
