package de.kxmischesdomi.more_axolotl.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.kxmischesdomi.more_axolotl.MoreAxolotls;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 * Ugly code please don't look its a shame
 */
@Environment(EnvType.CLIENT)
public class AxolotlCatalogScreen extends Screen {

	public static final int startAge = 951753697;
	private static final ResourceLocation CATALOG = new ResourceLocation(MoreAxolotls.MOD_ID, "textures/gui/catalog.png");

	public final Map<Axolotl.Variant, Axolotl> variants;

	private final int bookWidth = 246;
	private final int bookHeight = 182;
	private final int pages;
	private final Level world;

	private int page;

	private PageButton nextPageButton;
	private PageButton previousPageButton;

	public AxolotlCatalogScreen(Level world) {
		super(NarratorChatListener.NO_TITLE);

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
		RenderSystem.setShaderTexture(0, new ResourceLocation(MoreAxolotls.MOD_ID, "textures/gui/catalog.png"));

		final int uiWidth = 424;
		final int uiHeight = 182;

		int bookWidth = 284;
		int bookHeight = 180;

		int x = (this.width - uiWidth) / 2;
		int y = (this.height - uiHeight) / 2;

		int midX = this.width / 2;
		int midY = this.height / 2;

		int frameWidth = 105;
		int frameHeight = 105;

		int xOffset = - bookWidth / 4;

		// START RENDERING
		blit(matrices, x, y, 0, 0, uiWidth, uiHeight, uiWidth, uiHeight);

		for (int i = 0; i < 2; i++) {
			int variantOrdinal = i + page * 2;
			int pageCenterX = midX + xOffset;
			int frameCenterY = (int) (midY - bookHeight / 5.2);

			if (Axolotl.Variant.values().length > variantOrdinal) {
				Axolotl.Variant variant = Axolotl.Variant.values()[variantOrdinal];

				// ENTITY IMAGE
				int variantId = variant.getId();
				int size = 50;
				renderAxolotl(pageCenterX - size / 6, frameCenterY, size, -60, -40, variant);

				// BUCKET ITEM
				Minecraft client = Minecraft.getInstance();
				ItemStack itemStack = Items.AXOLOTL_BUCKET.getDefaultInstance();
				CompoundTag nbt = new CompoundTag();
				nbt.putInt("Variant", variantId);
				itemStack.setTag(nbt);
				client.getItemRenderer().renderAndDecorateFakeItem(itemStack, (int) (midX + xOffset + (frameWidth / 2.9)), frameCenterY - (frameHeight / 20));

				// INFO TEXTS
				String titleName = variant.getName().replace("_", " ");
				titleName = String.valueOf(titleName.charAt(0)).toUpperCase(Locale.ROOT) + titleName.substring(1);
				Component title = new TextComponent(titleName);

				renderAxolotlInfoText(matrices, title, pageCenterX, midY - (bookHeight / 6), 0, 1, true);

				// HAS TO BE REPLACED WITH MINECRAFT'S SPLITTING STUFF :sob:
				double scale = 0.7;
				renderAxolotlInfoText(matrices, getLinesOfMessage("more-axolotl.desc." + variant.getName()), (int) ((pageCenterX - bookWidth / 5.7) / scale), (int) ((midY - bookHeight / 11.2) / scale), 0, (float) scale, 10, 145);

				renderPageIndicator(matrices, variantId, i == 1);
			}

			xOffset = -xOffset;
		}

		super.render(matrices, mouseX, mouseY, delta);
	}

	public void renderPageIndicator(PoseStack matrices, int id, boolean right) {
		int x = (this.width) / 2;
		int y = (this.height) / 2;

		TextComponent rightPageIndicator = new TextComponent(id + "");
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

	public void renderAxolotlInfoText(PoseStack matrices, String[] text, int x, int y, int color, float scale, int spacing, int maxWidth) {
		renderAxolotlInfoText(matrices, text, x, y, color, scale, spacing, maxWidth, false);
	}

	public void renderAxolotlInfoText(PoseStack matrices, String[] text, int x, int y, int color, float scale, int spacing, int maxWidth, boolean centered) {

		try {
			for (String s : text) {
				int additionalSpacing = renderAxolotlInfoText(matrices, s, x, y, color, scale, spacing, centered, 0,maxWidth);
				y += additionalSpacing;
				y += spacing;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public int renderAxolotlInfoText(PoseStack matrices, String text, int x, int y, int color, float scale, int spacing, boolean centered, int currentSpacing, int maxWidth) {

		int additionalLines = 0;
		int width = font.width(text);
		if (width > maxWidth) {
			StringBuilder oldText = new StringBuilder(text);
			StringBuilder newText = new StringBuilder();

			StringBuilder lastOldText = new StringBuilder(text);
			StringBuilder lastNewText = new StringBuilder();

			for (char c : text.toCharArray()) {

				if (font.width(newText.toString() + c) > maxWidth + 5) {
					oldText = new StringBuilder(lastOldText);
					newText = new StringBuilder(lastNewText);
					break;
				}

				if (String.valueOf(c).equals(" ")) {
					lastOldText = new StringBuilder(oldText);
					lastNewText = new StringBuilder(newText);
				}

				oldText.deleteCharAt(0);
				newText.append(c);
			}

			if (!oldText.toString().trim().isEmpty()) {
				text = newText.toString();
				additionalLines++;
				currentSpacing = renderAxolotlInfoText(matrices, oldText.toString().trim(), x, y + spacing, color, scale, spacing, centered, currentSpacing, maxWidth);
			}
		}

		renderAxolotlInfoText(matrices, new TextComponent(text), x, y, color, scale, centered);
		return currentSpacing + additionalLines * spacing;
	}

	public void renderAxolotlInfoText(PoseStack matrices, Component text, int x, int y, int color, float scale) {
		renderAxolotlInfoText(matrices, text, x, y, color, scale, false);
	}

	public void renderAxolotlInfoText(PoseStack matrices, Component text, int x, int y, int color, float scale, boolean centered) {

		matrices.pushPose();
		matrices.scale(scale, scale, 1);

		if (centered) {
			x -= this.font.width(text) / 2;
		}

		this.font.draw(matrices, text, x, y, color);
		matrices.popPose();
	}

	public static String[] getLinesOfMessage(String key) {
		String translate = I18n.get(key);
		if (translate.equals(key)) return new String[] { I18n.get("gui.more-axolotl.catalog.no-desc") };
		return translate.split("§ö");
	}

	public static Axolotl getEntityForVariant(Axolotl.Variant variant, Level world) {
		Axolotl axolotlEntity = new Axolotl(EntityType.AXOLOTL, world);
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("Variant", variant.getId());
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
