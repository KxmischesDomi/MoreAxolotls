package de.kxmischesdomi.more_axolotl.mixin.client;

import de.kxmischesdomi.more_axolotl.client.screen.AxolotlCatalogScreen;
import de.kxmischesdomi.more_axolotl.common.AxolotlAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import net.minecraft.client.multiplayer.ClientLevel;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(ClientLevel.class)
public abstract class ClientWorldMixin {

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		Screen currentScreen = client.currentScreen;
		if (currentScreen instanceof AxolotlCatalogScreen catalogScreen) {
			for (AxolotlEntity entity : catalogScreen.variants.values()) {
				AxolotlAccessor ageable = (AxolotlAccessor) entity;
				ageable.setGuiAge(ageable.getGuiAge()+1);
			}
		}

	}

}
