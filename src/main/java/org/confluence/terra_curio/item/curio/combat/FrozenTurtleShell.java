package org.confluence.terra_curio.item.curio.combat;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.item.curio.CurioItems;
import org.confluence.terra_curio.misc.ModRarity;
import org.confluence.terra_curio.util.CuriosUtils;

public class FrozenTurtleShell extends BaseCurioItem {
    public FrozenTurtleShell() {
        super(ModRarity.PINK);
    }

    public static float apply(LivingEntity living, float amount) {
        if (living.getHealth() / living.getMaxHealth() > 0.5F ||
            CuriosUtils.noSameCurio(living, CurioItems.FROZEN_TURTLE_SHELL.get()) ||
            CuriosUtils.noSameCurio(living, CurioItems.FROZEN_SHIELD.get())
        ) return amount;
        return amount * 0.75F;
    }

    public Component[] getInformation() {
        return new Component[]{
            Component.translatable("item.terra_curio.frozen_turtle_shell.info")
        };
    }
}
