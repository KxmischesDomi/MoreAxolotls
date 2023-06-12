package de.kxmischesdomi.more_axolotl.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.kxmischesdomi.more_axolotl.MoreAxolotl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 * Ugly code please don't look it's a shame
 */
@Environment(EnvType.CLIENT)
public class AxolotlCatalogScreen extends Screen {

	public static final int startAge = 951753697;
	private static final ResourceLocation CATALOG = new ResourceLocation(MoreAxolotl.MOD_ID, "textures/gui/catalog.png");

	public final Map<Axolotl.Variant, Axolotl> variants;

	private final int pages;
	private final Level world;

	private int page;

	private PageButton nextPageButton;
	private PageButton previousPageButton;

	private ResourceLocation hoveredSummonButton = null;

	public AxolotlCatalogScreen(Level world) {
		super(Component.empty());

		this.world = world;

		variants = new HashMap<>();
		reloadAxolotl();

		pages = (int) Math.ceil((float) Axolotl.Variant.values().length / 2) - 1;
		page = 0;

	}

	@Override
	protected void init() {
		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 2 + 100, 200, 20, CommonComponents.GUI_DONE, (button) -> {
			this.minecraft.setScreen(null);
		}));
		addPageButtons();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, CATALOG);

		final int uiWidth = 424;
		final int uiHeight = 182;

		final int bookWidth = 264;
		final int bookHeight = 164;

		final int x = (this.width - uiWidth) / 2;
		final int y = (this.height - uiHeight) / 2;

		final int midX = this.width / 2;
//		final int midY = this.height / 2;

		final int frameWidth = 103;
		final int frameHeight = 45;
		int frameCenterY = 46 + y;

		int xOffset = - bookWidth / 4;

		// START RENDERING
		blit(matrices, x, y, 0, 0, uiWidth, uiHeight, uiWidth, uiHeight);

		hoveredSummonButton = null;

		for (int i = 0; i < 2; i++) {
			int variantOrdinal = i + page * 2;
			int pageCenterX = midX + xOffset;

			Minecraft client = Minecraft.getInstance();

			if (Axolotl.Variant.values().length > variantOrdinal) {
				Axolotl.Variant variant = Axolotl.Variant.values()[variantOrdinal];

				ResourceLocation location = new ResourceLocation(variant.getName());

				// ENTITY IMAGE
				int size = 50;
				renderAxolotl(pageCenterX - 8, frameCenterY + size / 6, size, -60, -40, variant);

				// BUCKET ITEM
				ItemStack bucketStack = Items.AXOLOTL_BUCKET.getDefaultInstance();
				CompoundTag nbt = new CompoundTag();
				nbt.putString("Variant", location.toString());
				bucketStack.setTag(nbt);

				int itemsX = pageCenterX + frameWidth / 2 - 16;
				int itemsY = frameCenterY + frameHeight / 2 - 16;

				client.getItemRenderer().renderAndDecorateFakeItem(bucketStack, itemsX, itemsY);
				itemsY -= 18;

				// COMMAND ITEM
				if (client.player.isCreative()) {
					ItemStack commandStack = Items.COMMAND_BLOCK.getDefaultInstance();
					client.getItemRenderer().renderAndDecorateFakeItem(commandStack, itemsX, itemsY);

					// Check if mouseX and mouseY hover over the command item
					if (mouseX >= itemsX && mouseX <= itemsX + 16 && mouseY >= itemsY && mouseY <= itemsY + 16) {
						renderTooltip(matrices, Component.literal("Summon"), mouseX, mouseY);
						hoveredSummonButton = location;					}
				}


				// INFO TEXTS
				String translationString = "mavapi.variant." + location.getNamespace() + "." + location.getPath();
				Component title;

				if (I18n.exists(translationString)) {
					title = Component.translatable(translationString);
				} else {
					String name = location.getPath().replace("_", "");
					name = String.valueOf(name.charAt(0)).toUpperCase(Locale.ROOT) + name.substring(1);
					title = Component.literal(name);
				}

				renderAxolotlInfoText(matrices, title, pageCenterX, frameCenterY + frameHeight / 2 - this.font.lineHeight, 0, 1, true);

				float scale = 0.7f;

				String decscriptionString = "more-axolotl.desc." + location.getNamespace() + "." + location.getPath();

				renderAxolotlInfoText(
						matrices,
						getLinesOfMessage(decscriptionString),
						pageCenterX - frameWidth / 2 - 5,
						frameCenterY + frameHeight / 2 + this.font.lineHeight,
						0,
						scale,
						(int) (10 * scale)
				);

			}

			xOffset = -xOffset;
		}

		super.render(matrices, mouseX, mouseY, delta);
	}

	public void renderPageIndicator(PoseStack matrices, int id, boolean right) {
		int x = (this.width) / 2;
		int y = (this.height) / 2;

		Component rightPageIndicator = Component.literal(String.valueOf(id));
		this.font.draw(matrices, rightPageIndicator, right ? x - font.width(rightPageIndicator) + 122 : x - 122, y - 78, 0);
	}

	public void addPageButtons() {
		int x = (this.width) / 2;
		int y = (this.height) / 2;

		this.nextPageButton = this.addRenderableWidget(new PageButton(x + 80 + 25, y + 65, true, (button) -> {
			nextPage();
		}, true));
		this.previousPageButton = this.addRenderableWidget(new PageButton(x - 105 - 25, y + 65, false, (button) -> {
			previousPage();
		}, true));

		updatePageButtons();
	}

	@Override
	public boolean mouseClicked(double d, double e, int i) {
		if (hoveredSummonButton != null && Minecraft.getInstance().player.isCreative()) {
			String command = "summon minecraft:axolotl ~ ~ ~ {\"Variant\":'" + hoveredSummonButton.toString() + "'}";
			executeCommand(command);
		}
		return super.mouseClicked(d, e, i);
	}

	public void executeCommand(String s) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		Class<? extends LocalPlayer> playerClass = player.getClass();

		String mappedUnsignedCommand = "commandUnsigned";
		String intermediaryUnsignedCommand = "method_44099";

		String mappedCommand = "command";
		String intermediaryCommand = "method_44098";

		String versionString = SharedConstants.getCurrentVersion().getName();
		try {
			if (versionString.compareTo("1.19.1") >= 0) {
				try {
					playerClass.getDeclaredMethod(intermediaryUnsignedCommand, String.class).invoke(player, s);
				} catch (NoSuchMethodException noSuchMethodException) {
					playerClass.getDeclaredMethod(mappedUnsignedCommand, String.class).invoke(player, s);
				}
			} else {
				try {
					playerClass.getDeclaredMethod(intermediaryCommand, String.class).invoke(player, s);
				} catch (NoSuchMethodException noSuchMethodException) {
					playerClass.getDeclaredMethod(mappedCommand, String.class).invoke(player, s);
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public void updatePageButtons() {
		this.nextPageButton.visible = page < pages;
		this.previousPageButton.visible = page > 0;
	}

	public void previousPage() {
		setPage(page - 1);
	}

	public void nextPage() {
		setPage(page + 1);
	}

	public void setPage(int newPage) {
		if (newPage > pages) {
			newPage = pages;
		} else if (newPage < 0) {
			newPage = 0;
		}
		page = newPage;
		reloadAxolotl();
		updatePageButtons();
	}

	public void renderAxolotl(int x, int y, int size, float mouseX, float mouseY, Axolotl.Variant variant) {
		Axolotl axolotlEntity = variants.get(variant);

		if (axolotlEntity.tickCount == startAge) {
			axolotlEntity.tickCount = 0;
			for (int i = 0; i < 230; i++) {
				InventoryScreen.renderEntityInInventory(x, y+1000, size, mouseX, mouseY, axolotlEntity);
			}
		}

		InventoryScreen.renderEntityInInventory(x, y, size, mouseX, mouseY, axolotlEntity);
	}

	public void reloadAxolotl() {
		variants.clear();

		for (int i = 0; i < 2; i++) {
			int variantOrdinal = i + page * 2;

			if (Axolotl.Variant.values().length > variantOrdinal) {
				Axolotl.Variant variant = Axolotl.Variant.values()[variantOrdinal];
				variants.put(variant, getEntityForVariant(variant, world));
			}

		}

	}

	public void renderAxolotlInfoText(PoseStack matrices, String[] text, int x, int y, int color, float scale, int spacing) {
		renderAxolotlInfoText(matrices, text, x, y, color, scale, spacing, false);
	}

	public void renderAxolotlInfoText(PoseStack matrices, String[] text, int x, int y, int color, float scale, int spacing, boolean centered) {

		try {
			for (String s : text) {
				renderAxolotlInfoText(matrices, Component.literal(s), x, y, color, scale, centered);
				y += spacing;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public void renderAxolotlInfoText(PoseStack matrices, Component text, int x, int y, int color, float scale) {
		renderAxolotlInfoText(matrices, text, x, y, color, scale, false);
	}

	public void renderAxolotlInfoText(PoseStack matrices, Component text, int x, int y, int color, float scale, boolean centered) {

		matrices.pushPose();
		matrices.scale(scale, scale, scale);

		if (centered) {
			x -= this.font.width(text) / 2;
		}

		// draw with ignored scale
		this.font.draw(matrices, text, x / scale, y / scale, color);
		matrices.popPose();
	}

	public static String[] getLinesOfMessage(String key) {
		String translate = I18n.get(key);
		if (translate.equals(key)) return new String[] { I18n.get("more-axolotl.no-desc") };
		return translate.split("\n");
	}

	public static Axolotl getEntityForVariant(Axolotl.Variant variant, Level world) {
		Axolotl axolotlEntity = new Axolotl(EntityType.AXOLOTL, world);
		CompoundTag nbt = new CompoundTag();
		nbt.putString("Variant", new ResourceLocation(variant.getName()).toString());
		axolotlEntity.readAdditionalSaveData(nbt);
		axolotlEntity.setNoAi(true);
		axolotlEntity.setOnGround(true);
		axolotlEntity.tickCount = startAge;

		return axolotlEntity;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
