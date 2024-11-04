package org.confluence.terra_curio.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.terra_curio.integration.bettercombat.BetterCombatHelper;
import org.confluence.terra_curio.mixin.client.accessor.MinecraftAccessor;
import org.confluence.terra_curio.network.s2c.CurioExistsPacketS2C;
import org.confluence.terra_curio.network.s2c.RightClickSubtractorPacketS2C;

import static org.confluence.terra_curio.network.s2c.CurioExistsPacketS2C.*;

@OnlyIn(Dist.CLIENT)
public final class TCClientPacketHandler {
    private static boolean autoAttack = false;
    private static boolean hasCthulhu = false;
    private static boolean hasTabi = false;
    private static boolean hasScope = false;
    private static boolean hasMagiluminescence = false;
    private static int rightClickSubtractor = 0;

    public static boolean couldAutoAttack() {
        return autoAttack;
    }

    public static boolean isHasCthulhu() {
        return hasCthulhu;
    }

    public static boolean isHasTabi() {
        return hasTabi;
    }

    public static boolean isHasScope() {
        return hasScope;
    }

    public static boolean isHasMagiluminescence() {
        return hasMagiluminescence;
    }

    public static int getRightClickSubtractor() {
        return rightClickSubtractor;
    }

    public static void handleSubstractor(RightClickSubtractorPacketS2C packet) {
        rightClickSubtractor = packet.amount();
    }

    public static void applyAutoAttack(Minecraft minecraft, LocalPlayer localPlayer) {
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

    public static void handleCurioExists(CurioExistsPacketS2C packet) {
        int item = packet.item();
        autoAttack = (item & AUTO_ATTACK) == AUTO_ATTACK;
        hasCthulhu = (item & SHIELD_OF_CTHULHU) == SHIELD_OF_CTHULHU;
        hasTabi = (item & TABI) == TABI;
        hasScope = (item & SCOPE) == SCOPE;
        GravitationHandler.setHasGlobe((item & GRAVITY_GLOBE) == GRAVITY_GLOBE);
        hasMagiluminescence = (item & MAGILUMINESCENCE) == MAGILUMINESCENCE;
    }

    public static void reset() {
        autoAttack = false;
        hasCthulhu = false;
        hasTabi = false;
        hasScope = false;
        hasMagiluminescence = false;
        rightClickSubtractor = 0;
    }
}
