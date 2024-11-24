package org.confluence.terra_curio.client.renderer.tooltip;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.TooltipComponentsValue;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.confluence.terra_curio.common.item.IFunctionCouldEnable;
import org.confluence.terra_curio.network.InfoDisablePacket;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;

public class MultiFunctionTooltip implements ClientTooltipComponent {
    private static final Component TOOLTIP = Component.translatable("tooltip.terra_curio.hold_and_scroll");
    private static final ResourceLocation ENABLED = TerraCurio.asResource("textures/gui/information/enabled.png");
    public static final float SCALE = 10.0F / 18.0F;
    public static int mouseScrollY = 0;

    private final List<TooltipComponentsValue.Storage> storages;
    private int mouseScrolledY = -1;
    private int width = 0;

    public MultiFunctionTooltip(List<TooltipComponentsValue.Storage> storages) {
        this.storages = storages;
    }

    @Override
    public int getHeight() {
        return isShiftKeyDown() ? storages.size() * 10 : 10;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        if (isShiftKeyDown()) {
            for (TooltipComponentsValue.Storage storage : storages) {
                int w = font.width(storage.text());
                if (w > width) this.width = w;
            }
            this.width += 10;
            return width;
        }
        return font.width(TOOLTIP);
    }

    @Override
    public void renderText(@NotNull Font font, int mouseX, int mouseY, @NotNull Matrix4f matrix, MultiBufferSource.@NotNull BufferSource bufferSource) {
        if (isShiftKeyDown()) {
            int size = storages.size();
            for (int i = 0; i < size; i++) {
                font.drawInBatch(storages.get(i).text(), mouseX + 10, mouseY + i * 10, -1, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            }
            this.mouseScrolledY = Mth.clamp(mouseScrollY, 0, size) - 1;
        } else {
            font.drawInBatch(TOOLTIP, mouseX, mouseY, 0xAAAAAAAA, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        }
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {
        int size = storages.size();
        if (mouseScrollY > size) {
            mouseScrollY = 0;
        } else if (mouseScrollY < 0) {
            mouseScrollY = size;
        }
        if (isShiftKeyDown()) {
            for (int i = 0; i < size; i++) {
                guiGraphics.pose().pushPose();
                int x1 = x - 2;
                int y1 = y - 1 + i * 10;
                guiGraphics.pose().translate(x1, y1,0);
                guiGraphics.pose().scale(SCALE, SCALE, SCALE);
                guiGraphics.blit(storages.get(i).texture(), 2, 2, 0, 0, 14, 14, 14, 14);
                if (!InformationHandler.DISABLE[i]) {
                    guiGraphics.blit(ENABLED, 0, 0, 0,0 , 18, 18, 18, 18);
                }
                guiGraphics.pose().popPose();
            }
            if (mouseScrolledY >= 0) {
                guiGraphics.renderOutline(x - 2, y - 2 + mouseScrolledY * 10, width + 2, 12, 0xFFFF0000);
            }
        } else {
            if (mouseScrollY > 0) {
                int index = IFunctionCouldEnable.Multi.INDEX_MAP.getOrDefault(storages.get(mouseScrollY - 1), -1);
                InformationHandler.DISABLE[index] = !InformationHandler.DISABLE[index];
                InfoDisablePacket.sendToServer(InformationHandler.DISABLE);
            }
            mouseScrollY = 0;
        }
    }

    private static boolean isShiftKeyDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT);
    }
}
