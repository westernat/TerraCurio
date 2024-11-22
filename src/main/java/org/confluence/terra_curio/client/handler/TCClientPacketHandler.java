package org.confluence.terra_curio.client.handler;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.integration.bettercombat.BetterCombatHelper;
import org.confluence.terra_curio.mixin.client.accessor.MinecraftAccessor;
import org.confluence.terra_curio.network.s2c.CurioExistsPacketS2C;
import org.confluence.terra_curio.network.s2c.LuminancePacketS2C;
import org.confluence.terra_curio.network.s2c.RightClickSubtractorPacketS2C;
import org.confluence.terra_curio.network.s2c.SetItemEntityPickupDelayPacketS2C;
import org.confluence.terra_curio.util.CuriosUtils;

import java.util.HashSet;
import java.util.Set;

import static org.confluence.terra_curio.network.s2c.CurioExistsPacketS2C.*;

@OnlyIn(Dist.CLIENT)
public final class TCClientPacketHandler {
    private static boolean autoAttack = false;
    private static boolean hasCthulhu = false;
    private static boolean hasTabi = false;
    private static boolean hasMagiluminescence = false;
    private static boolean canFloating = false;
    public static boolean floating = false;
    private static int rightClickSubtractor = 0;
    private static int luminance = 0;
    private static final Int2IntMap remoteLuminance = new Int2IntArrayMap();
    private static final Int2IntMap pickupDelayStorage = new Int2IntArrayMap();
    private static final Int2IntMap pickupDelayCounter = Util.make(new Int2IntArrayMap(), map -> map.defaultReturnValue(0));
    private static final Set<FluidState> walkableFluidStates = new HashSet<>();

    public static boolean couldAutoAttack() {
        return autoAttack;
    }

    public static boolean isHasCthulhu() {
        return hasCthulhu;
    }

    public static boolean isHasTabi() {
        return hasTabi;
    }

    public static boolean isHasMagiluminescence() {
        return hasMagiluminescence;
    }

    public static boolean isCanFloating() {
        return canFloating;
    }

    public static int getRightClickSubtractor() {
        return rightClickSubtractor;
    }

    public static int getWaterLuminance(Entity entity) {
        if (entity.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value())) {
            if (entity == Minecraft.getInstance().player) return luminance;
            return remoteLuminance.getOrDefault(entity.getId(), 0);
        }
        return 0;
    }

    public static void handleSubstractor(RightClickSubtractorPacketS2C packet) {
        rightClickSubtractor = packet.amount();
    }

    public static void handleCurioExists(CurioExistsPacketS2C packet) {
        int item = packet.item();
        autoAttack = (item & AUTO_ATTACK) == AUTO_ATTACK;
        hasCthulhu = (item & SHIELD_OF_CTHULHU) == SHIELD_OF_CTHULHU;
        hasTabi = (item & TABI) == TABI;
        ScopeFovHandler.hasScope = (item & SCOPE) == SCOPE;
        GravitationHandler.hasGlobe = (item & GRAVITY_GLOBE) == GRAVITY_GLOBE;
        hasMagiluminescence = (item & MAGILUMINESCENCE) == MAGILUMINESCENCE;
        canFloating = (item & FLOAT_ON_LIQUID_SURFACE) == FLOAT_ON_LIQUID_SURFACE;
    }

    public static void handleItemPickupDelay(SetItemEntityPickupDelayPacketS2C packet) {
        pickupDelayStorage.put(packet.id(), packet.delay());
    }

    public static Set<FluidState> getWalkableFluidStates() {
        return walkableFluidStates;
    }

    public static void handle(Minecraft minecraft, LocalPlayer player) {
        applyAutoAttack(minecraft, player);
        setPickupDelay(player);
    }

    private static void setPickupDelay(LocalPlayer player) {
        if (pickupDelayStorage.isEmpty()) return;
        ObjectIterator<Int2IntMap.Entry> iterator = pickupDelayStorage.int2IntEntrySet().iterator();
        while (iterator.hasNext()) {
            Int2IntMap.Entry next = iterator.next();
            int id = next.getIntKey();
            if (player.level().getEntity(id) instanceof ItemEntity itemEntity) {
                itemEntity.setPickUpDelay(next.getIntValue());
                iterator.remove();
                pickupDelayCounter.remove(id);
            } else {
                int count = pickupDelayCounter.get(id);
                if (count == 20) {
                    iterator.remove();
                    pickupDelayCounter.remove(id);
                } else {
                    pickupDelayCounter.put(id, count + 1);
                }
            }
        }
    }

    private static void applyAutoAttack(Minecraft minecraft, LocalPlayer localPlayer) {
        if (minecraft.gameMode == null || minecraft.gameMode.isDestroying()) return;
        if (BetterCombatHelper.isLoaded()) {
            ItemStack itemStack = localPlayer.getItemInHand(InteractionHand.MAIN_HAND);
            if (BetterCombatHelper.hasWeaponAttributes(itemStack)) return;
        }
        if (TCClientPacketHandler.couldAutoAttack() && minecraft.options.keyAttack.isDown()) {
            if (localPlayer.getAttackStrengthScale(0.5F) < 1.0F) return;
            MinecraftAccessor accessor = (MinecraftAccessor) minecraft;
            if (accessor.getMissTime() > 0) accessor.setMissTime(0);
            double reach = Math.max(localPlayer.entityInteractionRange(), localPlayer.blockInteractionRange());
            Vec3 from = localPlayer.getEyePosition(1.0F);
            Vec3 viewVector = localPlayer.getViewVector(1.0F);
            Vec3 to = from.add(viewVector.x * reach, viewVector.y * reach, viewVector.z * reach);
            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(
                    localPlayer, from, to, new AABB(from, to),
                    entity -> !entity.isSpectator() && entity.isPickable(), reach);
            if (entityhitresult != null && minecraft.gameMode != null) {
                minecraft.gameMode.attack(localPlayer, entityhitresult.getEntity());
            }
            localPlayer.resetAttackStrengthTicker();
            localPlayer.swing(InteractionHand.MAIN_HAND);
        }
    }

    public static void handleFluidWalk(Player player) {
        walkableFluidStates.clear();
        Set<TagKey<Fluid>> tagKeys = CuriosUtils.calculateValue(player, TCItems.FLUID$WALK);
        BuiltInRegistries.FLUID.stream().flatMap(fluid -> fluid.getStateDefinition().getPossibleStates().stream()).forEach(state -> {
            if (tagKeys.stream().anyMatch(state::is)) {
                walkableFluidStates.add(state);
            }
        });
    }

    public static void handleLuminance(LuminancePacketS2C packet, Player player) {
        if (packet.playerId() == player.getId()) {
            luminance = packet.luminance();
        } else {
            remoteLuminance.put(packet.playerId(), packet.luminance());
        }
    }

    public static void reset() {
        autoAttack = false;
        hasCthulhu = false;
        hasTabi = false;
        hasMagiluminescence = false;
        rightClickSubtractor = 0;
        canFloating = false;
        floating = false;
        luminance = 0;
        pickupDelayStorage.clear();
        pickupDelayCounter.clear();
        walkableFluidStates.clear();
        remoteLuminance.clear();
    }
}
