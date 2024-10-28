package org.confluence.mod.terra_curio.common.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.effect.beneficial.GravitationEffect;
import org.confluence.mod.terra_curio.common.effect.beneficial.HoneyEffect;
import org.confluence.mod.terra_curio.common.effect.beneficial.PaladinsShieldEffect;
import org.confluence.mod.terra_curio.common.effect.harmful.ConfusedEffect;
import org.confluence.mod.terra_curio.common.effect.neutral.CerebralMindtrickEffect;


public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, TerraCurio.MODID);
    public static final DeferredHolder<MobEffect, MobEffect> CONFUSED = EFFECTS.register("confused", ConfusedEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> GRAVITATION = EFFECTS.register("gravitation", GravitationEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> PALADINS_SHIELD = EFFECTS.register("paladins_shield", PaladinsShieldEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> CEREBRAL_MINDTRICK = EFFECTS.register("cerebral_mindtrick", CerebralMindtrickEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> HONEY = EFFECTS.register("honey", HoneyEffect::new);

    public static void healPerSecond(LivingEntity living, float amount) {
        if (living.level().getGameTime() % 20 == 0) {
            if (living.hasEffect(HONEY)) amount += 1;
            living.heal(amount);
        }
    }
}
