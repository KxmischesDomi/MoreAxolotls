package de.kxmischesdomi.more_axolotls.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import de.kxmischesdomi.more_axolotls.MoreAxolotls;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
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
 */
@Environment(EnvType.CLIENT)
public class AxolotlCatalogScreen extends Screen {

	public static final int startAge = 951753697;
	private static final Identifier CATALOG = new Identifier(MoreAxolotls.MOD_ID, "textures/gui/catalog_brown.png");

	public final Map<AxolotlEntity.Variant, AxolotlEntity> variants;

	private final int backgroundWidth = 246;
	private final int backgroundHeight = 182;
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
		addPageButtons();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, CATALOG);

		int x = (this.width - backgroundWidth) / 2;
		int y = (this.height - backgroundHeight) / 2;

		this.drawTexture(matrices, x, y, 6, 0, this.backgroundWidth, this.backgroundHeight);

		int xOffset = 0;
		int bucketXOffset = 0;
		for (int i = 0; i < 2; i++) {
			int variantOrdinal = i + page * 2;


			if (AxolotlEntity.Variant.values().length > variantOrdinal) {
				AxolotlEntity.Variant variant = AxolotlEntity.Variant.values()[variantOrdinal];
				int variantId = variant.getId();
				renderAxolotl(x + 62 + xOffset, y + 58, 40, -60, -40, variant);

				MinecraftClient client = MinecraftClient.getInstance();
				ItemStack itemStack = Items.AXOLOTL_BUCKET.getDefaultStack();
				NbtCompound nbt = new NbtCompound();
				nbt.putInt("Variant", variantId);
				itemStack.setNbt(nbt);
				client.getItemRenderer().renderInGui(itemStack, x + 90 + bucketXOffset, y + 55);

				int frameStartX = x + 17 + xOffset;
				int frameMiddleX = x + 25 + xOffset;
				int currentLineY = y + 62;

				String titleName = variant.getName().replace("_", " ");
				titleName = String.valueOf(titleName.charAt(0)).toUpperCase(Locale.ROOT) + titleName.substring(1);
				Text title = new LiteralText(titleName);
				renderAxolotlInfoText(matrices, title, frameMiddleX, currentLineY, 0, 1, true);

				currentLineY += 15;

				renderAxolotlInfoText(matrices, getLinesOfMessage("gui.more-axolotls.catalog.desc." + variant.getName()), (int) (frameStartX * 1.55), (int) (currentLineY * 1.55), 0, 0.65f, 10);

				renderPageIndicator(matrices, variantId, i == 1);
			}

			bucketXOffset = 47;
			xOffset = backgroundWidth / 2 - 10;
		}


		super.render(matrices, mouseX, mouseY, delta);
	}

	public void renderPageIndicator(MatrixStack matrices, int id, boolean right) {
		int x = (this.width) / 2;
		int y = (this.height) / 2;

		LiteralText rightPageIndicator = new LiteralText(id + "");
		this.textRenderer.draw(matrices, rightPageIndicator, right ? x - textRenderer.getWidth(rightPageIndicator) + 95 : x - 98, y - 78, 0);
	}

	public void addPageButtons() {
		int x = (this.width) / 2;
		int y = (this.height) / 2;

		this.nextPageButton = this.addDrawableChild(new PageTurnWidget(x + 80, y + 65, true, (button) -> {
			nextPage();
		}, true));
		this.previousPageButton = this.addDrawableChild(new PageTurnWidget(x - 105, y + 65, false, (button) -> {
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

	public void renderAxolotlInfoText(MatrixStack matrices, String[] text, int x, int y, int color, float scale, int spacing) {
		renderAxolotlInfoText(matrices, text, x, y, color, scale, spacing, false);
	}

	public void renderAxolotlInfoText(MatrixStack matrices, String[] text, int x, int y, int color, float scale, int spacing, boolean centered) {

		for (String s : text) {
			int additionalSpacing = renderAxolotlInfoText(matrices, s, x, y, color, scale, spacing, centered, 0);
			y += additionalSpacing;
			y += spacing;
		}

	}

	public int renderAxolotlInfoText(MatrixStack matrices, String text, int x, int y, int color, float scale, int spacing, boolean centered, int currentSpacing) {

		int additionalLines = 0;
		int width = textRenderer.getWidth(text);
		if (width > 145) {
			StringBuilder oldText = new StringBuilder(text);
			StringBuilder newText = new StringBuilder();

			StringBuilder lastOldText = new StringBuilder(text);
			StringBuilder lastNewText = new StringBuilder();

			for (char c : text.toCharArray()) {

				if (textRenderer.getWidth(newText.toString() + c) > 150) {
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
				currentSpacing = renderAxolotlInfoText(matrices, oldText.toString().trim(), x, y + spacing, color, scale, spacing, centered, currentSpacing);
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
			x += 42 - this.textRenderer.getWidth(text) / 2;
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
