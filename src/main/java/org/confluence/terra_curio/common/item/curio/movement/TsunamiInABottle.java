package org.confluence.terra_curio.common.item.curio.movement;

import org.confluence.terra_curio.common.item.curio.BaseCurioItem;

public class TsunamiInABottle extends BaseCurioItem {
    public TsunamiInABottle(Builder builder) {
        super(builder);
    }

//    @Override
//    protected void particleTick(LivingEntity living, ParticleEmitter emitter, Identifier particle) {
//        super.particleTick(living, emitter, particle);
//        if (PlayerJumpHandler.isOnTsunamiJump) {
//            emitter.active = true;
//            PlayerJumpHandler.isOnTsunamiJump = false;
//        } else {
//            emitter.active = false;
//        }
//
//        if (emitter.active) {
//            if (!emitter.isLocalSpace()) {
//                emitter.parentSpace = new Matrix4f();
//            }
//            if (IEntity.of(living).terra_curio$isShouldRot()) {
//                emitter.parentSpace.setTranslation(0, living.getBbHeight(), 0);
//            } else {
//                emitter.parentSpace.setTranslation(0, 0, 0);
//            }
//        }
//    }
}
