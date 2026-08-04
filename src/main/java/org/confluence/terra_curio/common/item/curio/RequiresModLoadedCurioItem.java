package org.confluence.terra_curio.common.item.curio;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.fml.ModList;

import java.util.Arrays;
import java.util.function.Consumer;

public class RequiresModLoadedCurioItem extends BaseCurioItem {
    private final String[] required;
    private final boolean noneLoaded;

    public RequiresModLoadedCurioItem(Builder builder, String... required) {
        super(builder);
        this.required = required;
        this.noneLoaded = Arrays.stream(required).noneMatch(ModList.get()::isLoaded);
    }

    public RequiresModLoadedCurioItem(Properties properties, String... required) {
        super(properties);
        this.required = required;
        this.noneLoaded = Arrays.stream(required).noneMatch(ModList.get()::isLoaded);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        if (noneLoaded) builder.accept(Component.translatable("tooltip.terra_curio.requires_mod_loaded", Arrays.toString(required)));
    }
}
