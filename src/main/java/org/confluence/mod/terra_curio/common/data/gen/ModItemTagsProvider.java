package org.confluence.mod.terra_curio.common.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.init.ModItems;
import org.confluence.mod.terra_curio.common.init.ModTags;
import org.confluence.mod.terra_curio.common.item.curio.BaseCurioItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> b, @Nullable ExistingFileHelper helper) {
        super(output, provider, b, TerraCurio.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        IntrinsicTagAppender<Item> appender = tag(ModTags.CURIO);
        ModItems.ITEMS.getEntries().forEach(item -> {
            if (item.get() instanceof BaseCurioItem) {
                appender.add(item.get());
            }
        });
    }
}
