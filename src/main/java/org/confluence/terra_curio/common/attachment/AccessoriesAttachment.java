package org.confluence.terra_curio.common.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.component.primitive.FloatValue;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import static org.confluence.terra_curio.common.component.AccessoriesComponent.*;

public class AccessoriesAttachment implements INBTSerializable<CompoundTag> {
    private boolean fireAttack;
    private boolean brainOfConfusion;
    private boolean hivePack;
    private float fishingPower;

    public AccessoriesAttachment() {
        setToDefaultValue();
    }

    private void setToDefaultValue() {
        this.fireAttack = false;
        this.brainOfConfusion = false;
        this.hivePack = false;
        this.fishingPower = 0.0F;
    }

    public void flushAbility(LivingEntity living) {
        CuriosApi.getCuriosInventory(living).ifPresent(handler -> {
            setToDefaultValue();
            float fishingPower = 0.0F;
            for (ICurioStacksHandler curioStacksHandler : handler.getCurios().values()) {
                IDynamicStackHandler stackHandler = curioStacksHandler.getStacks();
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    AccessoriesComponent component;
                    if (stack.isEmpty() || (component = stack.get(TCDataComponentTypes.ACCESSORIES)) == null) continue;
                    if (component.contains(FIRE_ATTACK)) this.fireAttack = true;
                    if (component.contains(BRAIN)) this.brainOfConfusion = true;
                    if (component.contains(HIVE)) this.hivePack = true;
                    FloatValue value;
                    if ((value = component.get(FISHING_POWER)) != null) {
                        fishingPower = value.combine(fishingPower, FISHING_POWER.rule());
                    }
                }
            }
            this.fishingPower = fishingPower;
        });
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("fireAttack", fireAttack);
        nbt.putBoolean("brainOfConfusion", brainOfConfusion);
        nbt.putBoolean("hivePack", hivePack);
        nbt.putFloat("fishingPower", fishingPower);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        this.fireAttack = nbt.getBoolean("fireAttack");
        this.brainOfConfusion = nbt.getBoolean("brainOfConfusion");
        this.hivePack = nbt.getBoolean("hivePack");
        this.fishingPower = nbt.getFloat("fishingPower");
    }
}
