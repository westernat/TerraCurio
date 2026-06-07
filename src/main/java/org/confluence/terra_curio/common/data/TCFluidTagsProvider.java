package org.confluence.terra_curio.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.init.TCTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TCFluidTagsProvider extends FluidTagsProvider {
    public TCFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper helper) {
        super(output, lookup, TerraCurio.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TCTags.WATER_LIKE_WALK).add(Fluids.WATER);
        tag(TCTags.LAVA_LIKE_WALK).add(Fluids.LAVA);
    }
}
