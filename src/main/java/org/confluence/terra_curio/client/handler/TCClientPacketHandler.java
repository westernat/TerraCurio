package org.confluence.terra_curio.client.handler;

import it.unimi.dsi.fastutil.ints.Int2BooleanArrayMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanMap;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.confluence.terra_curio.client.TCClientConfigs;
import org.confluence.terra_curio.integration.bettercombat.BetterCombatHelper;
import org.confluence.terra_curio.mixin.client.accessor.MinecraftAccessor;
import org.confluence.terra_curio.network.s2c.*;

import static org.confluence.terra_curio.network.s2c.BroadcastRenderPacketS2C.LUMINANCE_MASK;
import static org.confluence.terra_curio.network.s2c.BroadcastRenderPacketS2C.NEPTUNES_SHELL;
import static org.confluence.terra_curio.network.s2c.CurioExistsPacketS2C.*;

@OnlyIn(Dist.CLIENT)
public final class TCClientPacketHandler {
    private static boolean autoAttack = false;
    private static boolean hasCthulhu = false;
    private static boolean hasTabi = false;
    private static boolean hasMagiluminescence = false;
    private static boolean canFloating = false;
    public static boolean floating = false;
    private static boolean hasNeptunesShell = false;
    private static final Int2BooleanMap remoteNeptuneShell = new Int2BooleanArrayMap();
    private static int rightClickSubtractor = 0;
    private static int luminance = 0;
    private static final Int2IntMap remoteLuminance = new Int2IntArrayMap();
    private static final Int2IntMap pickupDelayStorage = new Int2IntArrayMap();
    private static final Int2IntMap pickupDelayCounter = Util.make(new Int2IntArrayMap(), map -> map.defaultReturnValue(0));

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

    public static boolean isHasNeptunesShell() {
        return hasNeptunesShell;
    }

    public static boolean canShowNeptunesShell(LivingEntity living) {
        return ((hasNeptunesShell && living.getClass() == LocalPlayer.class) || (living.getClass() == RemotePlayer.class && remoteNeptuneShell.get(living.getId()))) && living.isInWaterOrBubble();
    }

    public static int getRightClickSubtractor() {
        return rightClickSubtractor;
    }

    public static int getLuminance(Entity entity) {
        int ret = entity == Minecraft.getInstance().player ? luminance : remoteLuminance.getOrDefault(entity.getId(), 0);
        if (ret < 0) { // 只能在水下发光
            return entity.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value()) ? -ret : 0;
        }
        return ret;
    }

    public static void handleSubstractor(RightClickSubtractorPacketS2C packet) {
        rightClickSubtractor = packet.amount();
    }

    public static void handleCurioExists(CurioExistsPacketS2C packet) {
        int item = packet.item();
        autoAttack = (item & AUTO_ATTACK) != 0;
        hasCthulhu = (item & SHIELD_OF_CTHULHU) != 0;
        hasTabi = (item & TABI) != 0;
        ScopeFovHandler.hasScope = (item & SCOPE) != 0;
        GravitationHandler.hasGlobe = (item & GRAVITY_GLOBE) != 0;
        hasMagiluminescence = (item & MAGILUMINESCENCE) != 0;
        canFloating = (item & FLOAT_ON_LIQUID_SURFACE) != 0;
    }

    public static void handleItemPickupDelay(SetItemEntityPickupDelayPacketS2C packet) {
        pickupDelayStorage.put(packet.id(), packet.delay());
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
        if (!TCClientConfigs.autoAttack || minecraft.gameMode == null || minecraft.gameMode.isDestroying()) return;
        if (BetterCombatHelper.LOADED) {
            ItemStack itemStack = localPlayer.getItemInHand(InteractionHand.MAIN_HAND);
            if (BetterCombatHelper.hasWeaponAttributes(itemStack)) return;
        }
        if (TCClientPacketHandler.couldAutoAttack() && minecraft.options.keyAttack.isDown()) {
            if (localPlayer.getAttackStrengthScale(0.5F) < 1.0F - Mth.EPSILON) return;
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
        FluidWalkUpdatePacketS2C.reset(player);
    }

    public static void handleRender(BroadcastRenderPacketS2C packet, Player player) {
        short render = packet.render();
        if (player == Minecraft.getInstance().player) {
            luminance = render & LUMINANCE_MASK;
            hasNeptunesShell = (render & NEPTUNES_SHELL) == NEPTUNES_SHELL;
        } else {
            int playerId = packet.playerId();
            remoteLuminance.put(playerId, render & LUMINANCE_MASK);
            remoteNeptuneShell.put(playerId, (render & NEPTUNES_SHELL) == NEPTUNES_SHELL);
        }
    }

    public static void reset() {
        autoAttack = false;
        hasCthulhu = false;
        hasTabi = false;
        hasMagiluminescence = false;
        rightClickSubtractor = 0;
        canFloating = false;
        hasNeptunesShell = false;
        floating = false;
        luminance = 0;
        pickupDelayStorage.clear();
        pickupDelayCounter.clear();
        remoteLuminance.clear();
        remoteNeptuneShell.clear();
    }
}
