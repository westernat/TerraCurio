package org.confluence.mod.terra_curio.common.item.curio.movement;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.client.handler.ClientPacketHandler;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.component.SpeedBootsComponent;
import org.confluence.mod.terra_curio.common.init.ModDataComponentTypes;
import org.confluence.mod.terra_curio.common.init.ModSoundEvents;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.mod.terra_curio.network.c2s.SpeedBootsNBTPacketC2S;
import org.confluence.mod.terra_curio.util.CuriosUtils;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class BaseSpeedBoots extends BaseCurioItem {
    public static final ResourceLocation ID = TerraCurio.asResource("base_speed_boots");
    public static final Component TOOLTIP = Component.translatable("curios.tooltip.speed_boots");
//    private static final Vector3f COLOR = new Vector3f(1, 1, 1);

    public BaseSpeedBoots(ModRarity rarity) {
        super(new Properties().component(ModDataComponentTypes.MOD_RARITY, rarity).stacksTo(1).fireResistant());
    }

    public BaseSpeedBoots() {
        super(new Properties().component(ModDataComponentTypes.MOD_RARITY, ModRarity.BLUE).stacksTo(1).fireResistant());
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return EMPTY_TOOLTIP;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        speedUp(slotContext, stack, 1, 40);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);
        stack.set(ModDataComponentTypes.SPEED_BOOTS, SpeedBootsComponent.ZERO);
    }

    protected void speedUp(SlotContext slotContext, ItemStack stack, int addition, int max) {
        LivingEntity living = slotContext.entity();
        if (living instanceof Player player && player.isLocalPlayer()) {
            SpeedBootsComponent component = stack.get(ModDataComponentTypes.SPEED_BOOTS);
            int speed = component == null ? 0 : component.speed();
            if (player.zza > 0) {
                if (player.onGround()) {
                    if (ClientPacketHandler.isHasMagiluminescence()) addition *= 2;
                    int actually = Math.min(max - speed, addition);
                    if (actually > 0) {
                        PacketDistributor.sendToServer(new SpeedBootsNBTPacketC2S(slotContext.index(), speed + actually));
                    }
                    if (player.level().getGameTime() % 4 == 0) player.playSound(ModSoundEvents.SHOES_WALK.get());
                }
//                spawnParticles(player.level(), player.position());
            } else if (speed != 0) {
                PacketDistributor.sendToServer(new SpeedBootsNBTPacketC2S(slotContext.index(), 0));
            }
        }
    }

//    public void spawnParticles(Level level, Vec3 vec3) {
//        int rand = level.getRandom().nextInt(3, 5);
//        double particleRandX = (double) (level.getRandom().nextInt(100, 300) - 200) / 1000;
//        double particleRandY = (double) (level.getRandom().nextInt(100, 300) - 200) / 1000;
//        double particleRandZ = (double) (level.getRandom().nextInt(100, 300) - 200) / 1000;
//        CurrentDustOptions options = new CurrentDustOptions(getParticleColorStart(), getParticleColorEnd(), 1.2F);
//        for (int i = 0; i < rand; ++i) {
//            level.addParticle(options, vec3.x + particleRandX, vec3.y + particleRandY, vec3.z + particleRandZ, 0, 0, 0);
//        }
//    }
//
//    public Vector3f getParticleColorStart() {
//        return COLOR;
//    }
//
//    public Vector3f getParticleColorEnd() {
//        return COLOR;
//    }

    protected static AttributeModifier getSpeedModifier(ItemStack stack) {
        SpeedBootsComponent component = stack.get(ModDataComponentTypes.SPEED_BOOTS);
        int speed = component == null ? 0 : component.speed();
        return new AttributeModifier(ID, speed * 0.01, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return ImmutableMultimap.of(Attributes.MOVEMENT_SPEED, getSpeedModifier(stack));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosUtils.noSameCurio(slotContext.entity(), BaseSpeedBoots.class);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(TOOLTIP);
    }
}
