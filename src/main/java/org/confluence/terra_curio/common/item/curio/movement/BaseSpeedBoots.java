package org.confluence.terra_curio.common.item.curio.movement;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.api.primitive.AttributeModifiersValue;
import org.confluence.terra_curio.client.TCClientConfigs;
import org.confluence.terra_curio.client.handler.GravitationHandler;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import org.confluence.terra_curio.client.handler.TCClientPacketHandler;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.init.TCDataMaps;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.init.TCSoundEvents;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.network.c2s.SpeedBootsNBTPacketC2S;
import org.confluence.terra_curio.util.CuriosUtils;
import org.joml.Vector3f;
import org.mesdag.particlestorm.particle.ParticleEmitter;
import top.theillusivec4.curios.api.SlotContext;

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
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        speedUp(slotContext, stack, acceleration, maxSpeed);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void particleTick(LivingEntity living, ParticleEmitter emitter, ResourceLocation particle) {
        if (GravitationHandler.isShouldRot() && living.getClass() == LocalPlayer.class) {
            emitter.active = false;
        } else {
            emitter.offsetPos = new Vec3(0.0, 0.0, living.zza * 0.5);
            if (emitter.parentRotation == null) {
                emitter.parentRotation = new Vector3f();
            }
            emitter.active = living.zza > 0.0F && !living.horizontalCollision;
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);
        LibUtils.getItemStackNbt(stack).putInt(KEY, 0);
    }

    protected void speedUp(SlotContext slotContext, ItemStack stack, int acceleration, int maxSpeed) {
        LibUtils.forConfluence$Inject();
        if (TCClientConfigs.speedUp && slotContext.entity() instanceof Player player && player.isLocalPlayer()) {
            int speed = LibUtils.getItemStackNbt(stack).getInt(KEY);
            if (player.zza > 0 && !player.horizontalCollision && !player.isCrouching()) {
                if (player.onGround()) {
                    if (TCClientPacketHandler.isHasMagiluminescence() || PlayerJumpHandler.isInfiniteFlight()) acceleration *= 2;
                    int actually = Math.min(maxSpeed - speed, acceleration);
                    int value = speed + actually;
                    if (actually > 0) {
                        PacketDistributor.sendToServer(new SpeedBootsNBTPacketC2S(slotContext.index(), value));
                    }
                    float ratio = (float) value / maxSpeed;
                    if (TCClientConfigs.playShoesSound && player.level().getGameTime() % (ratio < 0.5F ? 6L : 4L) == 0) {
                        player.playSound(TCSoundEvents.SHOES_WALK.get(), TCClientConfigs.shoesSoundVolume, 1.0F);
                    }
                }
                if (TCClientConfigs.showShoesParticle) {
                    // todo particle
                }
            } else if (speed != 0) {
                PacketDistributor.sendToServer(new SpeedBootsNBTPacketC2S(slotContext.index(), 0));
            }
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder1 = ImmutableMultimap.builder();
        builder1.putAll(builder.getAttributes());
        AccessoriesComponent component = stack.getItemHolder().getData(TCDataMaps.ACCESSORIES);
        AttributeModifiersValue value;
        if (component != null && (value = component.get(TCItems.ATTRIBUTES)) != null) {
            builder1.putAll(value.get());
        }
        double speed = LibUtils.getItemStackNbt(stack).getInt(KEY) * 0.01;
        if (speed > 0.0) {
            builder1.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ID, speed, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        return builder1.build();
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosUtils.noSameCurio(slotContext.entity(), BaseSpeedBoots.class);
    }
}
