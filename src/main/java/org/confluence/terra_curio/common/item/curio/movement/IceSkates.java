package org.confluence.terra_curio.common.item.curio.movement;

import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.util.TCUtils;

public class IceSkates extends BaseCurioItem {
    public IceSkates() {
        super(getBuilder());
    }

    private static Builder getBuilder() {
        return TCUtils.forConfluence$ModifyExpression(builder("ice_skates").accessories(AccessoriesComponent.units(ValueType.ICE$SPEED)));
    }
}
