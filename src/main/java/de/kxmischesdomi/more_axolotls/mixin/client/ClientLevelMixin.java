package de.kxmischesdomi.more_axolotls.mixin.client;


import de.kxmischesdomi.more_axolotls.client.screen.AxolotlCatalogScreen;
import de.kxmischesdomi.more_axolotls.common.AxolotlAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		Minecraft client = Minecraft.getInstance();
		Screen currentScreen = client.screen;
		if (currentScreen instanceof AxolotlCatalogScreen catalogScreen) {
			for (Axolotl entity : catalogScreen.variants.values()) {
				AxolotlAccessor ageable = (AxolotlAccessor) entity;
				ageable.setGuiAge(ageable.getGuiAge()+1);
			}
		}

	}

}
