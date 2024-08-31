package org.confluence.mod.terra_curio.common.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.init.ModTags;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper helper) {
        super(output, lookup, TerraCurio.MOD_ID, helper);
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ModTags.FLOWER_BOOTS_AVAILABLE).add(
            Blocks.GRASS_BLOCK
        );
    }
}
