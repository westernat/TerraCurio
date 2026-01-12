package org.confluence.terra_curio.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terra_curio.misc.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static org.confluence.terra_curio.TerraCurio.MODID;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper helper) {
        super(output, lookup, MODID, helper);
    }

    @Override
    public void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ModTags.FLOWER_BOOTS_AVAILABLE).add(
            Blocks.GRASS_BLOCK
        );
    }
}
