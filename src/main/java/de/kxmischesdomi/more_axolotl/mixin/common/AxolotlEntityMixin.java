package de.kxmischesdomi.more_axolotl.mixin.common;

import de.kxmischesdomi.more_axolotl.common.AxolotlAccessor;
import de.kxmischesdomi.more_axolotl.common.AxolotlBreeds;
import de.kxmischesdomi.more_axolotl.mixin.server.AxolotlVariantAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.AxolotlEntity.Variant;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Random;
import java.util.UUID;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxolotlEntity.class)
public abstract class AxolotlEntityMixin extends AnimalEntity implements AxolotlAccessor {

	private static final UUID AXOLOTL_ARMOR_BONUS_ID = UUID.fromString("0b66fd1e-2b86-11ec-8d3d-0242ac130003");
	private static final TrackedData<Float> LEAF_DURABILITY;
	private static final TrackedData<Boolean> HAS_LEAF;
	public int guiAge = 0;
	public int mouthOpenTicks = 0;

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

	@ModifyVariable(method = "createChild", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/passive/AxolotlEntity$Variant;getRandomUnnatural(Ljava/util/Random;)Lnet/minecraft/entity/passive/AxolotlEntity$Variant;"))
	public Variant createUnnaturalChild(Variant original, ServerWorld world, PassiveEntity entity) {
		return AxolotlBreeds.getRandomBreed(world.getRandom());
	}

	@Inject(method = "shouldBabyBeDifferent", at = @At("HEAD"), cancellable = true)
	private static void shouldBabyBeDifferent(Random random, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(random.nextInt(1200) < AxolotlBreeds.RARE_BREEDS.get().size());
	}

	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

		ItemStack stackInHand = player.getStackInHand(hand);
		if (stackInHand.isOf(Items.BIG_DRIPLEAF) && !hasLeaf()) {
			setLeaf(true);
			if (!player.isCreative()) {
				stackInHand.decrement(1);
			}
			cir.setReturnValue(ActionResult.SUCCESS);
		} else if (stackInHand.isOf(Items.BONE_MEAL) && hasLeaf() && getLeafDurability() <= 10) {
			setLeafDurability(getLeafDurability() + 5);
			if (!player.isCreative()) {
				stackInHand.decrement(1);
			}
			cir.setReturnValue(ActionResult.SUCCESS);
		}

	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void initDataTracker(CallbackInfo ci) {
		this.dataTracker.startTracking(HAS_LEAF, false);
		this.dataTracker.startTracking(LEAF_DURABILITY, 0f);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("HasLeaf", dataTracker.get(HAS_LEAF));
		nbt.putFloat("LeafDurability", dataTracker.get(LEAF_DURABILITY));
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		dataTracker.set(HAS_LEAF, nbt.getBoolean("HasLeaf"));
		dataTracker.set(LEAF_DURABILITY, nbt.getFloat("LeafDurability"));
	}

	@Inject(method = "baseTick", at = @At("HEAD"))
	public void baseTick(CallbackInfo ci) {
		if (world.isClient && mouthOpenTicks > 0) {
			mouthOpenTicks--;
		}
	}

	@Override
	public void setLeaf(boolean leaf) {
		dataTracker.set(HAS_LEAF, leaf);

		if (leaf) {
			setLeafDurability(15);
		} else {
			setLeafDurability(0);
		}

		if (!world.isClient) {
			this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR).removeModifier(AXOLOTL_ARMOR_BONUS_ID);

			if (hasLeaf()) {
				getAttributeInstance(EntityAttributes.GENERIC_ARMOR).addTemporaryModifier(new EntityAttributeModifier(AXOLOTL_ARMOR_BONUS_ID, "Axolotl armor bonus", 15, EntityAttributeModifier.Operation.ADDITION));
			}
		}

	}

	@Override
	protected void damageArmor(DamageSource source, float amount) {
		setLeafDurability(getLeafDurability() - amount);
		super.damageArmor(source, amount);
	}

	@Override
	public boolean hasLeaf() {
		return dataTracker.get(HAS_LEAF);
	}

	@Override
	public float getLeafDurability() {
		return dataTracker.get(LEAF_DURABILITY);
	}

	@Override
	public void setLeafDurability(float damage) {
		dataTracker.set(LEAF_DURABILITY, damage);

		if (hasLeaf() && getLeafDurability() < 0) {
			setLeaf(false);
			setLeafDurability(0);
			playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.5F, 1F);
		}

	}

	@Override
	public int getMouthOpenTicks() {
		return mouthOpenTicks;
	}

	@Override
	public void setMouthOpenTicks(int ticks) {
		this.mouthOpenTicks = ticks;
	}

	@Override
	public void setGuiAge(int guiAge) {
		this.guiAge = guiAge;
	}

	@Override
	public int getGuiAge() {
		return guiAge;
	}

	public AxolotlEntity getEntity() {
		return (AxolotlEntity) ((Object) this);
	}

	static {
		HAS_LEAF = DataTracker.registerData(AxolotlEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
		LEAF_DURABILITY = DataTracker.registerData(AxolotlEntity.class, TrackedDataHandlerRegistry.FLOAT);
	}

}
