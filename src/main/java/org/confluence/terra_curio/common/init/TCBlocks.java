package org.confluence.terra_curio.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.block.WorkshopBlock;

public final class TCBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, TerraCurio.MODID);

    public static final RegistryObject<WorkshopBlock> WORKSHOP = BLOCKS.register("workshop", () -> new WorkshopBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));
}
