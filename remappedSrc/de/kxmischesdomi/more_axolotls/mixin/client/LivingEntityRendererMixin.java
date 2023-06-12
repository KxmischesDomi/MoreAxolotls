package de.kxmischesdomi.more_axolotl.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.kxmischesdomi.more_axolotl.client.RainbowRendering;
import de.kxmischesdomi.more_axolotl.common.CustomAxolotlVariant;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin <T extends LivingEntity> {

	@ModifyArgs(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
	private void onRender(Args args, T livingEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

		if (livingEntity instanceof Axolotl && ((Axolotl) livingEntity).getVariant() == CustomAxolotlVariant.RAINBOW.getVariant()) {
			float[] colors = RainbowRendering.getColors(livingEntity);

			args.set(4, colors[0]);
			args.set(5, colors[1]);
			args.set(6, colors[2]);
		}

	}

}
