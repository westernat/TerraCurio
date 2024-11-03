package org.confluence.terra_curio.common.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.confluence.terra_curio.api.event.FishingPowerModificationEvent;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.component.primitive.PrimitiveValue;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.confluence.terra_curio.common.item.curio.combat.PanicNecklace;
import org.confluence.terra_curio.util.MobEntityTypesTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.confluence.terra_curio.common.component.AccessoriesComponent.*;

public class AccessoriesAttachment implements INBTSerializable<CompoundTag> {
    private boolean fireAttack;
    private boolean brainOfConfusion;
    private boolean hivePack;
    private boolean starClock;
    private boolean honeyComb;
    private boolean magicQuiver;
    private boolean igniteArrow;
    private boolean frozenTurtleShell;
    private boolean panicNecklace;
    private float fishingPower;
    private float invulnerableTicksMultiplier;
    private float injuryFree;
    private final Set<EntityType<?>> ignores = new HashSet<>();

    public AccessoriesAttachment() {
        setToDefaultValue();
    }

    public void setToDefaultValue() {
        this.fireAttack = false;
        this.brainOfConfusion = false;
        this.hivePack = false;
        this.starClock = false;
        this.honeyComb = false;
        this.magicQuiver = false;
        this.igniteArrow = false;
        this.frozenTurtleShell = false;
        this.panicNecklace = false;
        this.fishingPower = 0.0F;
        this.invulnerableTicksMultiplier = 1.0F;
        this.injuryFree = 0.0F;
        this.ignores.clear();
    }

    public boolean isFireAttack() {
        return fireAttack;
    }

    public boolean isBrainOfConfusion() {
        return brainOfConfusion;
    }

    public boolean isHivePack() {
        return hivePack;
    }

    public boolean isStarClock() {
        return starClock;
    }

    public boolean isHoneyComb() {
        return honeyComb;
    }

    public boolean isMagicQuiver() {
        return magicQuiver;
    }

    public boolean isIgniteArrow() {
        return igniteArrow;
    }

    public boolean isFrozenTurtleShell() {
        return frozenTurtleShell;
    }

    public boolean isPanicNecklace() {
        return panicNecklace;
    }

    public float getFishingPower() {
        return fishingPower;
    }

    public float getInvulnerableTicksMultiplier() {
        return invulnerableTicksMultiplier;
    }

    public float getInjuryFree() {
        return injuryFree;
    }

    public Set<EntityType<?>> getIgnores() {
        return ignores;
    }

    public void flushAbility(LivingEntity living) {
        setToDefaultValue();
        CuriosApi.getCuriosInventory(living).ifPresent(handler -> {
            float fishingPower = this.fishingPower;
            float invulnerableTicksMultiplier = this.invulnerableTicksMultiplier;
            float injuryFree = this.injuryFree;
            List<EntityType<?>> ignores = new ArrayList<>();
            for (ICurioStacksHandler curioStacksHandler : handler.getCurios().values()) {
                IDynamicStackHandler stackHandler = curioStacksHandler.getStacks();
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    AccessoriesComponent component;
                    if (stack.isEmpty() || (component = stack.get(TCDataComponentTypes.ACCESSORIES)) == null) continue;
                    Item item = stack.getItem();

                    if (!fireAttack && component.contains(FIRE$ATTACK)) this.fireAttack = true;
                    if (!brainOfConfusion && component.contains(BRAIN$OF$CONFUSION)) this.brainOfConfusion = true;
                    if (!hivePack && component.contains(HIVE$PACK)) this.hivePack = true;
                    if (!starClock && component.contains(STAR$CLOCK)) this.starClock = true;
                    if (!honeyComb && component.contains(HONEY$COMB)) this.honeyComb = true;
                    if (!magicQuiver && component.contains(MAGIC$QUIVER)) this.magicQuiver = true;
                    if (!igniteArrow && component.contains(IGNITE$ARROW)) this.igniteArrow = true;
                    if (!frozenTurtleShell && component.contains(FROZEN$TURTLE$SHELL)) this.frozenTurtleShell = true;
                    if (!panicNecklace && item instanceof PanicNecklace) this.panicNecklace = true;
                    fishingPower = combineValue(component, fishingPower, FISHING$POWER);
                    invulnerableTicksMultiplier = combineValue(component, invulnerableTicksMultiplier, INVULNERABLE$TICKS$MULTIPLIER);
                    injuryFree = combineValue(component, injuryFree, INJURY$FREE);
                    ignores = combineValue(component, ignores, MOB$IGNORE);
                }
            }
            this.fishingPower = NeoForge.EVENT_BUS.post(new FishingPowerModificationEvent(living, fishingPower)).getNeoValue();
            this.invulnerableTicksMultiplier = invulnerableTicksMultiplier;
            this.injuryFree = injuryFree;
            this.ignores.addAll(ignores);
            if (!ignores.isEmpty()) {
                living.level().getEntities(new MobEntityTypesTest(ignores), new AABB(living.getOnPos()).inflate(31.5), mob -> true).forEach(mob -> {
                    if (mob.getTarget() == living) mob.setTarget(null);
                });
            }
        });
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("fireAttack", fireAttack);
        nbt.putBoolean("brainOfConfusion", brainOfConfusion);
        nbt.putBoolean("hivePack", hivePack);
        nbt.putBoolean("starClock", starClock);
        nbt.putBoolean("honeyComb", honeyComb);
        nbt.putBoolean("magicQuiver", magicQuiver);
        nbt.putBoolean("moltenQuiver", igniteArrow);
        nbt.putBoolean("frozenTurtleShell", frozenTurtleShell);
        nbt.putBoolean("panicNecklace", panicNecklace);
        nbt.putFloat("fishingPower", fishingPower);
        nbt.putFloat("injuryFree", injuryFree);
        nbt.putFloat("invulnerableTicksMultiplier", invulnerableTicksMultiplier);
        ListTag listTag = new ListTag();
        for (EntityType<?> ignore : ignores) {
            CompoundTag type = new CompoundTag();
            type.putString("EntityType", BuiltInRegistries.ENTITY_TYPE.getKey(ignore).toString());
            listTag.add(type);
        }
        nbt.put("ignores", listTag);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        this.fireAttack = nbt.getBoolean("fireAttack");
        this.brainOfConfusion = nbt.getBoolean("brainOfConfusion");
        this.hivePack = nbt.getBoolean("hivePack");
        this.starClock = nbt.getBoolean("starClock");
        this.honeyComb = nbt.getBoolean("honeyComb");
        this.magicQuiver = nbt.getBoolean("magicQuiver");
        this.igniteArrow = nbt.getBoolean("moltenQuiver");
        this.frozenTurtleShell = nbt.getBoolean("frozenTurtleShell");
        this.panicNecklace = nbt.getBoolean("panicNecklace");
        this.fishingPower = nbt.getFloat("fishingPower");
        this.injuryFree = nbt.getFloat("injuryFree");
        this.invulnerableTicksMultiplier = nbt.getFloat("invulnerableTicksMultiplier");
        this.ignores.clear();
        ListTag listTag = nbt.getList("ignores", ListTag.TAG_COMPOUND);
        for (Tag tag : listTag) {
            String type = ((CompoundTag) tag).getString("EntityType");
            ignores.add(BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(type)));
        }
    }

    private static <T, V extends PrimitiveValue<T>> T combineValue(AccessoriesComponent component, T value, Type<T, V> type) {
        V primitiveValue = component.get(type);
        return primitiveValue == null ? value : primitiveValue.combine(value, type.rule());
    }
}
