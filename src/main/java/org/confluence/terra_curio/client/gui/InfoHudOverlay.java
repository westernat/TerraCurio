package org.confluence.terra_curio.client.gui;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.confluence.terra_curio.api.primitive.TooltipComponentsValue;
import org.confluence.terra_curio.client.TCClientConfigs;
import org.confluence.terra_curio.client.handler.InformationHandler;
import org.confluence.terra_curio.common.init.TCItems;
import org.jetbrains.annotations.NotNull;

public class InfoHudOverlay implements GuiLayer {
    private static final int background = (0x90 << 24) + 0x505050;
    private static final int textColor = 0xE0E0E0;
    private static final Identifier[] INFO_ICON = TCItems.FULL_INFO.stream().map(TooltipComponentsValue.Storage::texture).toArray(Identifier[]::new);

    @Override
    public void render(@NotNull GuiGraphicsExtractor guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui || minecraft.getDebugOverlay().showDebugScreen()) return;

        int screenWidth = guiGraphics.guiWidth();
        int top = (int) (guiGraphics.guiHeight() * TCClientConfigs.informationHudTop);
        Font font = minecraft.font;
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
            guiGraphics.blit(INFO_ICON[entry.getIntKey()], iconLeft, top, 0F, 0F, 7, 7, 7, 7);
            guiGraphics.fill(left - 1, top - 1, left + w + 1, top + font.lineHeight - 1, background);
            guiGraphics.drawString(font, info, left, top, textColor, false);
            top += font.lineHeight;
        }
    }
}
