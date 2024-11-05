package org.confluence.terra_curio.common.item.curio.fishing;

import net.minecraft.world.entity.ai.attributes.Attributes;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.util.TCUtils;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;

public class AnglerEarring extends BaseCurioItem {
    public AnglerEarring() {
        super(getBuilder());
    }

    private static Builder getBuilder() {
        return TCUtils.forConfluence$ModifyExpression(builder("angler_earring").noTooltip().attribute(Attributes.LUCK, 10.0, ADD_VALUE));
    }
}
