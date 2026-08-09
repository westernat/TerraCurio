package org.confluence.terra_curio.client.model.accessory;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_curio.client.handler.PlayerJumpHandler;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.dataticket.DataTicket;

public class NormalWingsGeoModel extends AccessoryGeoModel {
    public static final DataTicket<State> STATE = new DataTicket<>("state", State.class);

    protected final ResourceLocation animation;

    public NormalWingsGeoModel(ResourceLocation id) {
        super(id);
        this.animation = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "animations/accessory/" + id.getPath() + ".animation.json");
    }

    @Override
    public ResourceLocation getAnimationResource(AccessoryGeoModel animatable) {
        return animation;
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

        public static State current() {
            return PlayerJumpHandler.isOnFlight() ? State.FLYING : (PlayerJumpHandler.isOnGlide() ? State.GLIDING : State.IDLING);
        }
    }
}
