package org.confluence.terra_curio.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terra_curio.api.primitive.TooltipComponentsValue;
import org.confluence.terra_curio.client.TCClientConfigs;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.confluence.terra_curio.common.init.TCItems;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class InfoHudOverlay implements LayeredDraw.Layer {
    private static final int background = (0x90 << 24) + 0x505050;
    private static final int textColor = 0xE0E0E0;
    private static final ResourceLocation[] INFO_ICON = TCItems.FULL_INFO.stream().map(TooltipComponentsValue.Storage::texture).toArray(ResourceLocation[]::new);

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
        int top = (int) (guiGraphics.guiHeight() * TCClientConfigs.informationHudTop);
        Font font = minecraft.font;
        PoseStack pose = guiGraphics.pose();
        for (Int2ObjectMap.Entry<Component> entry : InformationHandler.getInformation().int2ObjectEntrySet()) {
            int left;
            int iconLeft;
            Component info = entry.getValue();
            int w = font.width(info);
            if (TCClientConfigs.informationIsLeft) {
                left = 12;
                iconLeft = 1;
            } else {
                left = screenWidth - 12 - w;
                iconLeft = screenWidth - 10;
            }
            pose.pushPose();
            pose.translate(iconLeft, top - 0.5F, 0);
            float scale = font.lineHeight / 18.0F;
            pose.scale(scale, scale, scale);
            guiGraphics.blit(INFO_ICON[entry.getIntKey()], 0, 0, 0, 0, 18, 18, 18, 18);
            pose.popPose();
            guiGraphics.fill(left - 1, top - 1, left + w + 1, top + font.lineHeight - 1, background);
            guiGraphics.drawString(font, info, left, top, textColor, false);
            top += font.lineHeight;
        }

        minecraft.getProfiler().pop();
    }
}
