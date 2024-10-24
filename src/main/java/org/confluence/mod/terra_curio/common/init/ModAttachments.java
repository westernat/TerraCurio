package org.confluence.mod.terra_curio.common.init;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.attachment.AccessoriesAbility;

import java.util.function.Supplier;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TerraCurio.MODID);

    public static final Supplier<AttachmentType<AccessoriesAbility>> ACCESSORIES_ABILITY = ModAttachments.TYPES.register("accessories_ability", () -> AttachmentType.serializable(AccessoriesAbility::new).copyOnDeath().build());
}
