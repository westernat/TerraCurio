package org.confluence.terra_curio.client.model.accessory;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.DataTicket;

public class NormalWingsGeoModel extends AccessoryGeoModel {
    public static final DataTicket<State> STATE = new DataTicket<>("state", State.class);
    protected static final ResourceLocation DEFAULT_ANIMATION = TerraCurio.asResource("animations/accessory/normal_wings.animation.json");

    public NormalWingsGeoModel(ResourceLocation id) {
        super(id);
    }

    @Override
    public ResourceLocation getAnimationResource(AccessoryGeoModel animatable) {
        return DEFAULT_ANIMATION;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        RawAnimation idle = RawAnimation.begin().thenLoop("idle");
        RawAnimation fly = RawAnimation.begin().thenLoop("fly");
        RawAnimation glide = RawAnimation.begin().thenLoop("glide");
        controllers.add(new AnimationController<>(this, state -> {
            State s = state.getData(STATE);
            if (s != null && s != State.IDLING) {
                return s == State.FLYING ? state.setAndContinue(fly) : state.setAndContinue(glide);
            }
            return state.setAndContinue(idle);
        }));
    }

    public enum State {
        IDLING, FLYING, GLIDING;

        public static State local() {
            return PlayerJumpHandler.isOnFlight() ? State.FLYING : (PlayerJumpHandler.isOnGlide() ? State.GLIDING : State.IDLING);
        }

        public static State remote(double dy) {
            if (dy > 0) return FLYING;
            if (dy < 0) return GLIDING;
            return IDLING;
        }
    }
}
