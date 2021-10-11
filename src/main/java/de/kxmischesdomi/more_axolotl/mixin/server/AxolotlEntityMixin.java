package de.kxmischesdomi.more_axolotl.mixin.server;

import de.kxmischesdomi.more_axolotl.common.AxolotlBreeds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.AxolotlEntity.Variant;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxolotlEntity.class)
public abstract class AxolotlEntityMixin extends AnimalEntity {

	@Shadow public abstract void readCustomDataFromNbt(NbtCompound nbt);

	public AxolotlEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyArgs(method = "createChild", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AxolotlEntity;setVariant(Lnet/minecraft/entity/passive/AxolotlEntity$Variant;)V"))
	public void setVariant(Args args, ServerWorld world, PassiveEntity entity) {
		if (args.get(0) != null && !((AxolotlVariantAccessor) args.get(0)).isNatural()) return;

		AxolotlEntity thisEntity = getEntity();
		Variant variant1 = thisEntity.getVariant();
		Variant variant2 = ((AxolotlEntity) entity).getVariant();
		if (variant1 != variant2 && entity.getRandom().nextInt(10) < 4) {
			Variant result = AxolotlBreeds.getBreed(variant1, variant2, world.getRandom());
			if (result != null) {
				args.set(0, result);
			}
		}
	}

	@Inject(method = "shouldBabyBeDifferent", at = @At("HEAD"), cancellable = true)
	private static void shouldBabyBeDifferent(Random random, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(random.nextInt(1200) < AxolotlBreeds.RARE_BREEDS.get().size());
	}

	@ModifyVariable(
			method = "createChild",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/passive/AxolotlEntity$Variant;getRandomUnnatural(Ljava/util/Random;)Lnet/minecraft/entity/passive/AxolotlEntity$Variant;"))
	public Variant createUnnaturalChild(Variant original, ServerWorld world, PassiveEntity entity) {
		return AxolotlBreeds.getRandomBreed(world.getRandom());
	}

	public AxolotlEntity getEntity() {
		return (AxolotlEntity) ((Object) this);
	}

}
