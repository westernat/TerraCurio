package org.confluence.mod.terra_curio.common.capability;

import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class CuriosAbility {
    public Set<String> attackAbility = new HashSet<>();
    public Set<String> defenseAbility = new HashSet<>();

    public CompoundTag saveNBTData(){
        CompoundTag nbt = new CompoundTag();
        nbt.putString("attackAbility", attackAbility.toString());
        nbt.putString("defenseAbility", defenseAbility.toString());
        return nbt;
    }

    public void loadNBTData(CompoundTag nbt){
            attackAbility = new HashSet<>(Arrays.asList(nbt.getString("attackAbility").split(",")));
            defenseAbility = new HashSet<>(Arrays.asList(nbt.getString("defenseAbility").split(",")));
    }
}
