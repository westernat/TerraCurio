package org.confluence.terra_curio.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.menu.WorkshopMenu;
import org.jetbrains.annotations.NotNull;

public class WorkshopScreen extends AbstractContainerScreen<WorkshopMenu> {
    private static final Identifier BACKGROUND = TerraCurio.asResource("textures/gui/container/workshop.png");
    private boolean upButtonClicked = false;
    private ItemStack upItem = null;
    private boolean downButtonClicked = false;
    private ItemStack downItem = null;

    public WorkshopScreen(WorkshopMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = imageWidth - font.width(title) - 8;
        this.inventoryLabelX = imageWidth - font.width(playerInventoryTitle) - 8;
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        extractTooltip(graphics, mouseX, mouseY);
        if (isOverUpButton(mouseX - leftPos, mouseY - topPos)) {
            if (upItem == null) this.upItem = menu.getUpResult();
            graphics.renderFakeItem(upItem, leftPos + 125, topPos + 35);
            this.downItem = null;
        } else if (isOverDownButton(mouseX - leftPos, mouseY - topPos)) {
            if (downItem == null) this.downItem = menu.getDownResult();
            graphics.renderFakeItem(downItem, leftPos + 125, topPos + 35);
            this.upItem = null;
        } else {
            graphics.renderFakeItem(menu.getSlot(0).getItem(), leftPos + 125, topPos + 35);
            this.upItem = null;
            this.downItem = null;
        }
        if (menu.getRecipesAmount() > 0) {
            String text = menu.getCurrentIndex() + 1 + "/" + menu.getRecipesAmount();
            graphics.drawString(font, text, leftPos + 144, topPos + 37 + (16 - font.lineHeight) / 2, 4210752, false);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blit(BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        if (upButtonClicked) {
            graphics.blit(BACKGROUND, leftPos + 128, topPos + 23, 177, 0, 10, 8);
        } else if (downButtonClicked) {
            graphics.blit(BACKGROUND, leftPos + 128, topPos + 54, 177, 8, 10, 9);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (isOverUpButton((int) event.x() - leftPos, (int) event.y() - topPos)) {
            int upIndex = menu.getUpIndex();
            if (menu.clickMenuButton(minecraft.player, upIndex)) {
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, upIndex);
                this.upButtonClicked = true;
                this.downButtonClicked = false;
                this.upItem = null;
                return true;
            }
            return false;
        } else if (isOverDownButton((int) event.x() - leftPos, (int) event.y() - topPos)) {
            int downIndex = menu.getDownIndex();
            if (menu.clickMenuButton(minecraft.player, downIndex)) {
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, downIndex);
                this.upButtonClicked = false;
                this.downButtonClicked = true;
                this.downItem = null;
                return true;
            }
            return false;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        this.upButtonClicked = false;
        this.downButtonClicked = false;
        return super.mouseReleased(event);
    }

    private static boolean isOverUpButton(int x, int y) {
        return x >= 128 && x <= 138 && y >= 23 && y <= 30;
    }

    private static boolean isOverDownButton(int x, int y) {
        return x >= 128 && x <= 138 && y >= 54 && y <= 61;
    }
}
