package org.confluence.terra_curio.common.init;

import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.attachment.AccessoriesAttachment;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.registries.PortAttachmentRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.registries.PortRegistryEntry;

public final class TCAttachments {
    public static void init() {}

    public static final PortAttachmentRegistration TYPES = PortRegisterHandler.attachment(TerraCurio.MODID);

    public static final PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<AccessoriesAttachment>> ACCESSORIES = TCAttachments.TYPES.register("accessories", () -> PortAttachmentType.serializable(AccessoriesAttachment::new).copyOnDeath().build());
}
