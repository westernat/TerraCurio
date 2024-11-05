package org.confluence.terra_curio.common.item.curio.movement;

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
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.ClientConfigs;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.common.init.TCSoundEvents;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.network.c2s.SpeedBootsNBTPacketC2S;
import org.confluence.terra_curio.util.CuriosUtils;
import org.confluence.terra_curio.util.TCUtils;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class BaseSpeedBoots extends BaseCurioItem {
    public static final String KEY = TerraCurio.MODID + ":boots_speed";
    public static final ResourceLocation ID = TerraCurio.asResource("base_speed_boots");

    private final int acceleration;
    private final int maxSpeed;

    public BaseSpeedBoots(int acceleration, int maxSpeed, Builder builder) {
        super(builder);
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return EMPTY_TOOLTIP;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        speedUp(slotContext, acceleration, maxSpeed);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);
        slotContext.entity().getPersistentData().putInt(KEY, 0);
    }

    protected void speedUp(SlotContext slotContext, int acceleration, int maxSpeed) {
        TCUtils.forConfluence$Inject();
        if (slotContext.entity() instanceof Player player && player.isLocalPlayer()) {
            int speed = player.getPersistentData().getInt(KEY);
            if (player.zza > 0) {
                if (player.onGround()) {
                    if (TCClientPacketHandler.isHasMagiluminescence()) acceleration *= 2;
                    int actually = Math.min(maxSpeed - speed, acceleration);
                    int value = speed + actually;
                    if (actually > 0) {
                        PacketDistributor.sendToServer(new SpeedBootsNBTPacketC2S(slotContext.index(), value));
                    }
                    float ratio = (float) value / maxSpeed;
                    if (ClientConfigs.playShoesSound && player.level().getGameTime() % (ratio < 0.5F ? 6L : 4L) == 0) {
                        player.playSound(TCSoundEvents.SHOES_WALK.get());
                    }
                }
                if (ClientConfigs.showShoesParticle) {
                    // todo particle
                }
            } else if (speed != 0) {
                PacketDistributor.sendToServer(new SpeedBootsNBTPacketC2S(slotContext.index(), 0));
            }
        }
    }

    protected static AttributeModifier getSpeedModifier(LivingEntity living) {
        double speed = living == null ? 0.0 : living.getPersistentData().getInt(KEY) * 0.01;
        return new AttributeModifier(ID, speed, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return ImmutableMultimap.of(Attributes.MOVEMENT_SPEED, getSpeedModifier(slotContext.entity()));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosUtils.noSameCurio(slotContext.entity(), BaseSpeedBoots.class);
    }
}
