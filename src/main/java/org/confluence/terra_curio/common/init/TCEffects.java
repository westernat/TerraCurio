package org.confluence.terra_curio.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.effect.beneficial.GravitationEffect;
import org.confluence.terra_curio.common.effect.beneficial.HoneyEffect;
import org.confluence.terra_curio.common.effect.beneficial.PaladinsShieldEffect;
import org.confluence.terra_curio.common.effect.harmful.ConfusedEffect;
import org.confluence.terra_curio.common.effect.neutral.CerebralMindtrickEffect;

public final class TCEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, TerraCurio.MODID);
    public static final DeferredHolder<MobEffect, MobEffect> CONFUSED = EFFECTS.register("confused", ConfusedEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> GRAVITATION = EFFECTS.register("gravitation", GravitationEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> PALADINS_SHIELD = EFFECTS.register("paladins_shield", PaladinsShieldEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> CEREBRAL_MINDTRICK = EFFECTS.register("cerebral_mindtrick", CerebralMindtrickEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> HONEY = EFFECTS.register("honey", HoneyEffect::new);

    public static void healPerSecond(LivingEntity living, float amount) {
        if (living.level().getGameTime() % 20L == 0) {
            if (living.hasEffect(HONEY)) amount += 1.0F;
            living.heal(amount);
        }
    }
}
