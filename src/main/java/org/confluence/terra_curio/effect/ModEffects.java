package org.confluence.terra_curio.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.effect.beneficial.GravitationEffect;
import org.confluence.terra_curio.effect.beneficial.HoneyEffect;
import org.confluence.terra_curio.effect.beneficial.PaladinsShieldEffect;
import org.confluence.terra_curio.effect.harmful.ConfusedEffect;
import org.confluence.terra_curio.effect.neutral.CerebralMindtrickEffect;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TerraCurio.MODID);

    public static final RegistryObject<ConfusedEffect> CONFUSED = EFFECTS.register("confused", ConfusedEffect::new);
    public static final RegistryObject<GravitationEffect> GRAVITATION = EFFECTS.register("gravitation", GravitationEffect::new);
    public static final RegistryObject<PaladinsShieldEffect> PALADINS_SHIELD = EFFECTS.register("paladins_shield", PaladinsShieldEffect::new);
    public static final RegistryObject<CerebralMindtrickEffect> CEREBRAL_MINDTRICK = EFFECTS.register("cerebral_mindtrick", CerebralMindtrickEffect::new);
    public static final RegistryObject<HoneyEffect> HONEY = EFFECTS.register("honey", HoneyEffect::new);

    public static void healPerSecond(LivingEntity living, float amount) {
        if (living.level().getGameTime() % 20 == 0) {
            if (living.hasEffect(HONEY.get())) amount += 1;
            living.heal(amount);
        }
    }
}
