package org.confluence.terra_curio.client.renderer.tooltip;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.TooltipComponentsValue;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.confluence.terra_curio.common.item.IMultiFunctionCouldEnable;
import org.confluence.terra_curio.network.InfoDisablePacket;
import org.joml.Matrix3x2fStack;

import java.util.List;

public class MultiFunctionTooltip implements ClientTooltipComponent {
    private static final Component TOOLTIP = Component.translatable("tooltip.terra_curio.hold_and_scroll");
    public static final Identifier HIGHLIGHT = TerraCurio.asResource("textures/gui/information/highlight.png");
    public static final float SCALE = 10.0F / 9.0F;
    public static int mouseScrollY = 0;
    public static boolean isShowing = false;

    private final List<TooltipComponentsValue.Storage> storages;
    private int mouseScrolledY = -1;
    private int width = 0;

    public MultiFunctionTooltip(List<TooltipComponentsValue.Storage> storages) {
        this.storages = storages;
    }

    @Override
    public int getHeight(Font font) {
        return isShiftKeyDown() ? storages.size() * 10 : 10;
    }

    @Override
    public int getWidth(Font font) {
        if (isShiftKeyDown()) {
            isShowing = true;
            for (TooltipComponentsValue.Storage storage : storages) {
                int w = font.width(storage.text());
                if (w > width) this.width = w;
            }
            this.width += 10;
            return width;
        } else {
            isShowing = false;
        }
        return font.width(TOOLTIP);
    }

    @Override
    public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {
        if (isShowing) {
            int size = storages.size();
            for (int i = 0; i < size; i++) {
                graphics.text(font, storages.get(i).text(), x + 10, y + i * 10, -1, true);
            }
            this.mouseScrolledY = Mth.clamp(mouseScrollY, 0, size) - 1;
        } else {
            graphics.text(font, TOOLTIP, x, y, 0xAAAAAAAA, true);
        }
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        int size = storages.size();
        if (mouseScrollY > size) {
            mouseScrollY = 0;
        } else if (mouseScrollY < 0) {
            mouseScrollY = size;
        }
        if (isShowing) {
            for (int i = 0; i < size; i++) {
                Matrix3x2fStack pose = graphics.pose();
                pose.pushMatrix();
                pose.translate(x - 2.0F, y - 1.0F + i * 10.0F);
                pose.scale(SCALE, SCALE);
                TooltipComponentsValue.Storage storage = storages.get(i);
                graphics.blit(storage.texture(), 1, 1, 0F, 0F, 7, 7, 7, 7);
                int index = IMultiFunctionCouldEnable.INDEX_MAP.getOrDefault(storage, -1);
                if (index != -1 && !InformationHandler.DISABLE[index]) {
                    graphics.blit(HIGHLIGHT, 0, 0, 0F, 0F, 9, 9, 9, 9);
                }
                pose.popMatrix();
            }
            if (mouseScrolledY >= 0) {
                graphics.renderOutline(x - 2, y - 2 + mouseScrolledY * 10, width + 2, 12, 0xFFFF0000);
            }
        } else {
            if (mouseScrollY > 0) {
                int index = IMultiFunctionCouldEnable.INDEX_MAP.getOrDefault(storages.get(mouseScrollY - 1), -1);
                if (index != -1) {
                    InformationHandler.DISABLE[index] = !InformationHandler.DISABLE[index];
                }
                InfoDisablePacket.sendToServer(InformationHandler.DISABLE);
            }
            mouseScrollY = 0;
        }
    }

    private static boolean isShiftKeyDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_LSHIFT);
    }
}
