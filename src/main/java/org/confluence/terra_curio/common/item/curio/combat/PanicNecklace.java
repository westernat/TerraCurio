package org.confluence.terra_curio.common.item.curio.combat;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCAttachments;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.util.CuriosUtils;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

public class PanicNecklace extends BaseCurioItem {
    public static final String KEY = TerraCurio.MODID + ":last_hurt";

    public PanicNecklace(Builder builder) {
        super(builder.initialize());
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Level level = slotContext.entity().level();
        if (level.isClientSide) return;
        CompoundTag nbt = slotContext.entity().getPersistentData();
        long lastHurt = nbt.getLong(KEY);
        if (lastHurt == 0) return;
        if (level.getGameTime() - lastHurt > 160) {
            nbt.putLong(KEY, 0);
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        LivingEntity living = slotContext.entity();
        if (living == null) return EMPTY_ATTRIBUTE;
        return living.getPersistentData().getLong(KEY) == 0 ? EMPTY_ATTRIBUTE : super.getAttributeModifiers(slotContext, id, stack);
    }

    @Override
    public boolean canEquip(@NotNull ItemStack stack, @NotNull EquipmentSlot armorType, @NotNull LivingEntity entity) {
        return CuriosUtils.noSameCurio(entity, PanicNecklace.class);
    }

    public static void apply(LivingEntity living) {
        if (living.getData(TCAttachments.ACCESSORIES).isPanicNecklace()) {
            living.getPersistentData().putLong(KEY, living.level().getGameTime());
        }
    }
}
