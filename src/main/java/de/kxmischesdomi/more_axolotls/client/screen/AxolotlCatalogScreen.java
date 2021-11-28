package de.kxmischesdomi.more_axolotls.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import de.kxmischesdomi.more_axolotls.MoreAxolotls;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

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
	private static final Identifier CATALOG = new Identifier(MoreAxolotls.MOD_ID, "textures/gui/catalog.png");

	public final Map<AxolotlEntity.Variant, AxolotlEntity> variants;

	private final int bookWidth = 246;
	private final int bookHeight = 182;
	private final int pages;
	private final World world;

	private int page;

	private PageTurnWidget nextPageButton;
	private PageTurnWidget previousPageButton;

	public AxolotlCatalogScreen(World world) {
		super(NarratorManager.EMPTY);

		this.world = world;

		variants = new HashMap<>();
		reloadAxolotl();

		pages = (int) Math.ceil((float) AxolotlEntity.Variant.values().length / 2) - 1;
		page = 0;

	}

	@Override
	protected void init() {
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 100, 200, 20, ScreenTexts.DONE, (button) -> {
			this.client.setScreen(null);
		}));
		addPageButtons();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, new Identifier(MoreAxolotls.MOD_ID, "textures/gui/catalog.png"));

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
		drawTexture(matrices, x, y, 0, 0, uiWidth, uiHeight, uiWidth, uiHeight);

		for (int i = 0; i < 2; i++) {
			int variantOrdinal = i + page * 2;
			int pageCenterX = midX + xOffset;
			int frameCenterY = (int) (midY - bookHeight / 5.2);

			if (AxolotlEntity.Variant.values().length > variantOrdinal) {
				AxolotlEntity.Variant variant = AxolotlEntity.Variant.values()[variantOrdinal];

				// ENTITY IMAGE
				int variantId = variant.getId();
				int size = 50;
				renderAxolotl(pageCenterX - size / 6, frameCenterY, size, -60, -40, variant);

				// BUCKET ITEM
				MinecraftClient client = MinecraftClient.getInstance();
				ItemStack itemStack = Items.AXOLOTL_BUCKET.getDefaultStack();
				NbtCompound nbt = new NbtCompound();
				nbt.putInt("Variant", variantId);
				itemStack.setNbt(nbt);
				client.getItemRenderer().renderInGui(itemStack, (int) (midX + xOffset + (frameWidth / 2.9)), frameCenterY - (frameHeight / 20));

				// INFO TEXTS
				String titleName = variant.getName().replace("_", " ");
				titleName = String.valueOf(titleName.charAt(0)).toUpperCase(Locale.ROOT) + titleName.substring(1);
				Text title = new LiteralText(titleName);

				renderAxolotlInfoText(matrices, title, pageCenterX, midY - (bookHeight / 6), 0, 1, true);

				// HAS TO BE REPLACED WITH MINECRAFT'S SPLITTING STUFF :sob:
				double scale = 0.7;
				renderAxolotlInfoText(matrices, getLinesOfMessage("gui.more-axolotls.catalog.desc." + variant.getName()), (int) ((pageCenterX - bookWidth / 5.7) / scale), (int) ((midY - bookHeight / 11.2) / scale), 0, (float) scale, 10, 145);

				renderPageIndicator(matrices, variantId, i == 1);
			}

			xOffset = -xOffset;
		}

		super.render(matrices, mouseX, mouseY, delta);
	}

	public void renderPageIndicator(MatrixStack matrices, int id, boolean right) {
		int x = (this.width) / 2;
		int y = (this.height) / 2;

		LiteralText rightPageIndicator = new LiteralText(id + "");
		this.textRenderer.draw(matrices, rightPageIndicator, right ? x - textRenderer.getWidth(rightPageIndicator) + 122 : x - 122, y - 78, 0);
	}

	public void addPageButtons() {
		int x = (this.width) / 2;
		int y = (this.height) / 2;

		this.nextPageButton = this.addDrawableChild(new PageTurnWidget(x + 80 + 25, y + 65, true, (button) -> {
			nextPage();
		}, true));
		this.previousPageButton = this.addDrawableChild(new PageTurnWidget(x - 105 - 25, y + 65, false, (button) -> {
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

	public void renderAxolotl(int x, int y, int size, float mouseX, float mouseY, AxolotlEntity.Variant variant) {
		AxolotlEntity axolotlEntity = variants.get(variant);

		if (axolotlEntity.age == startAge) {
			axolotlEntity.age = 0;
			for (int i = 0; i < 230; i++) {
				InventoryScreen.drawEntity(x, y+1000, size, mouseX, mouseY, axolotlEntity);
			}
		}

		InventoryScreen.drawEntity(x, y, size, mouseX, mouseY, axolotlEntity);
	}

	public void reloadAxolotl() {
		variants.clear();

		for (int i = 0; i < 2; i++) {
			int variantOrdinal = i + page * 2;

			if (AxolotlEntity.Variant.values().length > variantOrdinal) {
				AxolotlEntity.Variant variant = AxolotlEntity.Variant.values()[variantOrdinal];
				variants.put(variant, getEntityForVariant(variant, world));
			}

		}

	}

	public void renderAxolotlInfoText(MatrixStack matrices, String[] text, int x, int y, int color, float scale, int spacing, int maxWidth) {
		renderAxolotlInfoText(matrices, text, x, y, color, scale, spacing, maxWidth, false);
	}

	public void renderAxolotlInfoText(MatrixStack matrices, String[] text, int x, int y, int color, float scale, int spacing, int maxWidth, boolean centered) {

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

	public int renderAxolotlInfoText(MatrixStack matrices, String text, int x, int y, int color, float scale, int spacing, boolean centered, int currentSpacing, int maxWidth) {

		int additionalLines = 0;
		int width = textRenderer.getWidth(text);
		if (width > maxWidth) {
			StringBuilder oldText = new StringBuilder(text);
			StringBuilder newText = new StringBuilder();

			StringBuilder lastOldText = new StringBuilder(text);
			StringBuilder lastNewText = new StringBuilder();

			for (char c : text.toCharArray()) {

				if (textRenderer.getWidth(newText.toString() + c) > maxWidth + 5) {
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

		renderAxolotlInfoText(matrices, new LiteralText(text), x, y, color, scale, centered);
		return currentSpacing + additionalLines * spacing;
	}

	public void renderAxolotlInfoText(MatrixStack matrices, Text text, int x, int y, int color, float scale) {
		renderAxolotlInfoText(matrices, text, x, y, color, scale, false);
	}

	public void renderAxolotlInfoText(MatrixStack matrices, Text text, int x, int y, int color, float scale, boolean centered) {

		matrices.push();
		matrices.scale(scale, scale, 1);

		if (centered) {
			x -= this.textRenderer.getWidth(text) / 2;
		}

		this.textRenderer.draw(matrices, text, x, y, color);
		matrices.pop();
	}

	public static String[] getLinesOfMessage(String key) {
		String translate = I18n.translate(key);
		if (translate.equals(key)) return new String[] { I18n.translate("gui.more-axolotls.catalog.no-desc") };
		return translate.split("§ö");
	}

	public static AxolotlEntity getEntityForVariant(AxolotlEntity.Variant variant, World world) {
		AxolotlEntity axolotlEntity = new AxolotlEntity(EntityType.AXOLOTL, world);
		NbtCompound nbt = new NbtCompound();
		nbt.putInt("Variant", variant.getId());
		axolotlEntity.readCustomDataFromNbt(nbt);
		axolotlEntity.setAiDisabled(true);
		axolotlEntity.setOnGround(true);
		axolotlEntity.age = startAge;

		return axolotlEntity;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
