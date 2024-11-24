package org.confluence.terra_curio.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.confluence.terra_curio.client.ClientConfigs;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.jetbrains.annotations.NotNull;

public class InfoHudOverlay implements LayeredDraw.Layer {
    private static final int background = (0x90 << 24) + 0x505050;
    private static final int textColor = 0xE0E0E0;

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui || minecraft.getDebugOverlay().showDebugScreen()) return;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        minecraft.getProfiler().push("info");

        int screenWidth = guiGraphics.guiWidth();
        int top = (int) (guiGraphics.guiHeight() * ClientConfigs.informationHudTop);
        Font font = minecraft.font;
        for (Component info : InformationHandler.getInformation()) {
            int left;
            int w = font.width(info);
            if (ClientConfigs.informationIsLeft) {
                left = 2;
            } else {
                left = screenWidth - 2 - w;
            }
            guiGraphics.fill(left - 1, top - 1, left + w + 1, top + font.lineHeight - 1, background);
            guiGraphics.drawString(font, info, left, top, textColor, false);
            top += font.lineHeight;
        }

        minecraft.getProfiler().pop();
    }
}
