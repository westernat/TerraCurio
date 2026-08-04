package org.confluence.terra_curio.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.confluence.terra_curio.TerraCurio;

public class StepStoolModel extends EntityModel<EntityRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(TerraCurio.asResource("step_stool"), "main");

    public StepStoolModel(ModelPart root) {
        super(root.getChild("bb_main"));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 2.0F, 16.0F, CubeDeformation.NONE)
                .texOffs(26, 18).addBox(4.0F, -14.0F, 4.0F, 2.0F, 14.0F, 2.0F, CubeDeformation.NONE)
                .texOffs(18, 18).addBox(-6.0F, -14.0F, 4.0F, 2.0F, 14.0F, 2.0F, CubeDeformation.NONE)
                .texOffs(8, 0).addBox(4.0F, -14.0F, -6.0F, 2.0F, 14.0F, 2.0F, CubeDeformation.NONE)
                .texOffs(0, 0).addBox(-6.0F, -14.0F, -6.0F, 2.0F, 14.0F, 2.0F, CubeDeformation.NONE)
                .texOffs(0, 31).addBox(-4.0F, -11.0F, 4.5F, 8.0F, 2.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(0, 28).addBox(-4.0F, -11.0F, -5.5F, 8.0F, 2.0F, 1.0F, CubeDeformation.NONE)
                .texOffs(0, 18).addBox(4.5F, -11.0F, -4.0F, 1.0F, 2.0F, 8.0F, CubeDeformation.NONE)
                .texOffs(0, 18).addBox(-5.5F, -11.0F, -4.0F, 1.0F, 2.0F, 8.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}