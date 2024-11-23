package org.confluence.terra_curio.client.renderer.tooltip;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.confluence.terra_curio.api.primitive.TooltipComponentsValue;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.confluence.terra_curio.common.item.IFunctionCouldEnable;
import org.confluence.terra_curio.network.InfoDisablePacket;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;

public class MultiFunctionTooltip implements ClientTooltipComponent {
    public static final MutableComponent EMPTY = Component.translatable("tooltip.terra_curio.empty");
    public static int mouseScrollY = 0;

    private final List<TooltipComponentsValue.Storage> storages;
    private int mouseScrolledY = -1;

    public MultiFunctionTooltip(List<TooltipComponentsValue.Storage> storages) {
        this.storages = storages;
    }

    @Override
    public int getHeight() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) ? storages.size() * 10 : 10;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return 128;
    }

    @Override
    public void renderText(@NotNull Font font, int mouseX, int mouseY, @NotNull Matrix4f matrix, MultiBufferSource.@NotNull BufferSource bufferSource) {
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340)) {
            int size = storages.size();
            for (int i = 0; i < size; i++) {
                font.drawInBatch(storages.get(i).text(), mouseX + 10, mouseY + i * 10, -1, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            }
            this.mouseScrolledY = Mth.clamp(mouseScrollY, 0, size) - 1;
            if (mouseScrollY > size) mouseScrollY = size;
        } else {
            font.drawInBatch(EMPTY, mouseX, mouseY, -1, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        }
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340)) {
            for (int i = 0; i < storages.size(); i++) {
                if (!InformationHandler.DISABLE[i]) {
                    guiGraphics.blit(storages.get(i).texture(), x, y + i * 10, 0, 0, 10, 10);
                }
            }
            if (mouseScrolledY >= 0) {
                guiGraphics.fill(x + 1, y - 1 + mouseScrolledY * 10, x + 128, y + mouseScrolledY * 10, 0xFFFF0000);
                guiGraphics.fill(x + 128, y + 9 + mouseScrolledY * 10, x + 129, y + mouseScrolledY * 10, 0xFFFF0000);
                guiGraphics.fill(x + 1, y + 9 + mouseScrolledY * 10, x + 128, y + 10 + mouseScrolledY * 10, 0xFFFF0000);
                guiGraphics.fill(x, y + 9 + mouseScrolledY * 10, x + 1, y + mouseScrolledY * 10, 0xFFFF0000);
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
}
