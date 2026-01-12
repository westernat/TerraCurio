package org.confluence.terra_curio.item.curio.informational;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.confluence.terra_curio.misc.ModConfigs;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public interface IMetalDetector {
    static Component getInfo(Player localPlayer) {
        AtomicReference<Component> atomic = new AtomicReference<>(Component.translatable("info.terra_curio.metal_detector.none"));
        Object2IntMap<BlockState> indexed = new Object2IntOpenHashMap<>();
        Set<BlockState> tested = new HashSet<>();
        localPlayer.level().getBlockStates(new AABB(localPlayer.getOnPos()).inflate(15.5)).forEach(blockState -> {
            if (tested.contains(blockState)) return;
            for (int i = 0; i < ModConfigs.rareBlocks.size(); i++) {
                if (ModConfigs.rareBlocks.get(i).test(blockState)) {
                    indexed.put(blockState, i);
                }
            }
            tested.add(blockState);
        });
        indexed.object2IntEntrySet().stream().min(Comparator.comparingInt(Object2IntMap.Entry::getIntValue))
                .ifPresent(entry -> atomic.set(Component.translatable("info.terra_curio.metal_detector", entry.getKey().getBlock().getName())));
        return atomic.get();
    }

    Component TOOLTIP = Component.translatable("curios.tooltip.metal_detector");
    byte INDEX = 4;
}
