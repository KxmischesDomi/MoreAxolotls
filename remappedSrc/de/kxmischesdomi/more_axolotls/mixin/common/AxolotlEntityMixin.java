package de.kxmischesdomi.more_axolotls.mixin.common;

import de.kxmischesdomi.more_axolotls.common.AxolotlAccessor;
import de.kxmischesdomi.more_axolotls.common.AxolotlVariantManager;
import de.kxmischesdomi.more_axolotls.mixin.server.AxolotlVariantAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Random;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.axolotl.Axolotl.Variant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(Axolotl.class)
public abstract class AxolotlEntityMixin extends Animal implements AxolotlAccessor {

	@Shadow @Final private static EntityDataAccessor<Integer> VARIANT;

	@Shadow public abstract InteractionResult mobInteract(Player player, InteractionHand hand);

	@Shadow public abstract void setFromBucket(boolean fromBucket);

	@Shadow protected abstract void setVariant(Variant variant);

	@Shadow public abstract Variant getVariant();

	private static final UUID AXOLOTL_ARMOR_BONUS_ID = UUID.fromString("0b66fd1e-2b86-11ec-8d3d-0242ac130003");
	private static final EntityDataAccessor<Float> LEAF_DURABILITY;
	private static final EntityDataAccessor<Boolean> HAS_LEAF;
	public int guiAge = 0;
	public int mouthOpenTicks = 0;

	public AxolotlEntityMixin(EntityType<? extends Animal> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "getVariant", at = @At("HEAD"), cancellable = true)
	public void getVariant(CallbackInfoReturnable<Variant> cir) {
		cir.setReturnValue(AxolotlVariantManager.getVariantById(entityData.get(VARIANT)));
	}

	@ModifyArgs(method = "createChild", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AxolotlEntity;setVariant(Lnet/minecraft/entity/passive/AxolotlEntity$Variant;)V"))
	public void createChildVariant(Args args, ServerLevel world, AgeableMob entity) {
		if (args.get(0) != null && !((AxolotlVariantAccessor) args.get(0)).isNatural()) return;

		Axolotl thisEntity = getEntity();
		Variant variant1 = thisEntity.getVariant();
		Variant variant2 = ((Axolotl) entity).getVariant();
		if (variant1 != variant2 && entity.getRandom().nextInt(10) < 4) {
			Variant result = AxolotlVariantManager.getBreed(variant1, variant2, world.getRandom());
			if (result != null) {
				args.set(0, result);
			}
		}
	}

	@ModifyVariable(method = "createChild", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/passive/AxolotlEntity$Variant;getRandomUnnatural(Ljava/util/Random;)Lnet/minecraft/entity/passive/AxolotlEntity$Variant;"))
	public Variant createUnnaturalChild(Variant original, ServerLevel world, AgeableMob entity) {
		return AxolotlVariantManager.getRandomBreed(world.getRandom());
	}

	@Inject(method = "shouldBabyBeDifferent", at = @At("HEAD"), cancellable = true)
	private static void shouldBabyBeDifferent(Random random, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(random.nextInt(1200) < AxolotlVariantManager.RARE_BREEDS.get().size());
	}

	@Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
	public void interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

		ItemStack stackInHand = player.getItemInHand(hand);
		if (stackInHand.is(Items.BIG_DRIPLEAF) && !hasLeaf()) {
			setLeaf(true);
			if (!player.isCreative()) {
				stackInHand.shrink(1);
			}
			cir.setReturnValue(InteractionResult.SUCCESS);
		} else if (stackInHand.is(Items.BONE_MEAL) && hasLeaf() && getLeafDurability() <= 10) {
			setLeafDurability(getLeafDurability() + 5);
			if (!player.isCreative()) {
				stackInHand.shrink(1);
			}
			cir.setReturnValue(InteractionResult.SUCCESS);
		}

	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void initDataTracker(CallbackInfo ci) {
		this.entityData.define(HAS_LEAF, false);
		this.entityData.define(LEAF_DURABILITY, 0f);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {
		nbt.putBoolean("HasLeaf", entityData.get(HAS_LEAF));
		nbt.putFloat("LeafDurability", entityData.get(LEAF_DURABILITY));
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"), cancellable = true)
	public void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {
		super.readAdditionalSaveData(nbt);
		this.setVariant(AxolotlVariantManager.getVariantById(nbt.getInt("Variant")));
		this.setFromBucket(nbt.getBoolean("FromBucket"));
		entityData.set(HAS_LEAF, nbt.getBoolean("HasLeaf"));
		entityData.set(LEAF_DURABILITY, nbt.getFloat("LeafDurability"));
		ci.cancel();
	}

	@Inject(method = "baseTick", at = @At("HEAD"))
	public void baseTick(CallbackInfo ci) {
		if (level.isClientSide && mouthOpenTicks > 0) {
			mouthOpenTicks--;
		}
	}

	@Override
	public int getVariantId() {
		return entityData.get(VARIANT);
	}

	@Override
	public void setLeaf(boolean leaf) {
		entityData.set(HAS_LEAF, leaf);

		if (leaf) {
			setLeafDurability(15);
		} else {
			setLeafDurability(0);
		}

		if (!level.isClientSide) {
			this.getAttribute(Attributes.ARMOR).removeModifier(AXOLOTL_ARMOR_BONUS_ID);

			if (hasLeaf()) {
				getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(AXOLOTL_ARMOR_BONUS_ID, "Axolotl armor bonus", 15, AttributeModifier.Operation.ADDITION));
			}
		}

	}

	@Override
	protected void hurtArmor(DamageSource source, float amount) {
		setLeafDurability(getLeafDurability() - amount);
		super.hurtArmor(source, amount);
	}

	@Override
	public boolean hasLeaf() {
		return entityData.get(HAS_LEAF);
	}

	@Override
	public float getLeafDurability() {
		return entityData.get(LEAF_DURABILITY);
	}

	@Override
	public void setLeafDurability(float damage) {
		entityData.set(LEAF_DURABILITY, damage);

		if (hasLeaf() && getLeafDurability() < 0) {
			setLeaf(false);
			setLeafDurability(0);
			playSound(SoundEvents.ITEM_BREAK, 0.5F, 1F);
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

	public Axolotl getEntity() {
		return (Axolotl) ((Object) this);
	}

	static {
		HAS_LEAF = SynchedEntityData.defineId(Axolotl.class, EntityDataSerializers.BOOLEAN);
		LEAF_DURABILITY = SynchedEntityData.defineId(Axolotl.class, EntityDataSerializers.FLOAT);
	}

}
