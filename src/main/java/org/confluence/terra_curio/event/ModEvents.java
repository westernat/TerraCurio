package org.confluence.terra_curio.event;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.effect.ModEffects;
import org.confluence.terra_curio.effect.neutral.CerebralMindtrickEffect;
import org.confluence.terra_curio.item.curio.CurioItems;
import org.confluence.terra_curio.misc.ModAttributes;
import org.confluence.terra_curio.misc.ModConfigs;
import org.confluence.terra_curio.network.NetworkHandler;
import org.confluence.terra_curio.recipe.AmountIngredient;

import java.util.function.BiConsumer;

import static org.confluence.terra_curio.TerraCurio.MODID;

@Mod.EventBusSubscriber(modid = TerraCurio.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.register();
            ModConfigs.onLoadCommon();
        });
    }

    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            ModEffects.CEREBRAL_MINDTRICK.get().addAttributeModifier(ModAttributes.getCriticalChance(), CerebralMindtrickEffect.CRIT_UUID, 0.04, AttributeModifier.Operation.ADDITION);
        });
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void register(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.ITEM) {
            for (CurioItems value : CurioItems.values()) {
                ResourceLocation id = value.getValue().getId();
                ((ForgeRegistry<?>) ForgeRegistries.ITEMS).addAlias(new ResourceLocation("confluence", id.getPath()), id);
            }
        } else if (event.getRegistryKey() == Registries.ATTRIBUTE) {
            for (RegistryObject<Attribute> entry : ModAttributes.ATTRIBUTES.getEntries()) {
                ResourceLocation id = entry.getId();
                ((ForgeRegistry<?>) ForgeRegistries.ATTRIBUTES).addAlias(new ResourceLocation("confluence", id.getPath()), id);
            }
        } else if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, helper -> {
                CraftingHelper.register(new ResourceLocation(MODID, "amount"), AmountIngredient.Serializer.INSTANCE);
            });
        }
    }

    @SubscribeEvent
    public static void modify(EntityAttributeModificationEvent event) {
        ModAttributes.readJsonConfig();
        BiConsumer<EntityType<? extends LivingEntity>, Attribute> add = event::add;
        ModAttributes.registerAttribute(ModAttributes.CRIT_CHANCE.get(), add);
        ModAttributes.registerAttribute(ModAttributes.RANGED_VELOCITY.get(), add);
        ModAttributes.registerAttribute(ModAttributes.RANGED_DAMAGE.get(), add);
        ModAttributes.registerAttribute(ModAttributes.DODGE_CHANCE.get(), add);
        ModAttributes.registerAttribute(ModAttributes.MINING_SPEED.get(), add);
        ModAttributes.registerAttribute(ModAttributes.AGGRO.get(), add);
        ModAttributes.registerAttribute(ModAttributes.MAGIC_DAMAGE.get(), add);
        ModAttributes.registerAttribute(ModAttributes.ARMOR_PASS.get(), add);
        ModAttributes.registerAttribute(ModAttributes.PICKUP_RANGE.get(), add);
    }
}
