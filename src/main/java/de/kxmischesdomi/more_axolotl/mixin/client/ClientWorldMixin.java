package de.kxmischesdomi.more_axolotl.mixin.client;

import de.kxmischesdomi.more_axolotl.client.screen.AxolotlCatalogScreen;
import de.kxmischesdomi.more_axolotl.common.GuiAgeable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.passive.AxolotlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		Screen currentScreen = client.currentScreen;
		if (currentScreen instanceof AxolotlCatalogScreen catalogScreen) {
			for (AxolotlEntity entity : catalogScreen.variants.values()) {
				GuiAgeable ageable = (GuiAgeable) entity;
				ageable.setGuiAge(ageable.getGuiAge()+1);
			}
		}

	}

}
