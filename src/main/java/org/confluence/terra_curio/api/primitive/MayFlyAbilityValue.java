package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record MayFlyAbilityValue(Storage storage) implements PrimitiveValue<MayFlyAbilityValue.Storage> {
    public static final Codec<MayFlyAbilityValue> CODEC = Storage.CODEC.xmap(MayFlyAbilityValue::new, MayFlyAbilityValue::get);
    public static final CombineRule<Storage, MayFlyAbilityValue> COMBINE_RULE = CombineRule.register((a, b) -> new Storage(
            Math.max(a.flySpeed, b.flySpeed),
            Math.max(a.flyTicks, b.flyTicks),
            a.couldGlide || b.couldGlide,
            a.horizontalFlight || b.horizontalFlight
    ), "may_fly_ability");

    @Override
    public Storage get() {
        return storage;
    }

    @Override
    public Codec<MayFlyAbilityValue> codec() {
        return CODEC;
    }

    public record Storage(float flySpeed, int flyTicks, boolean couldGlide, boolean horizontalFlight) {
        public static final Codec<Storage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.POSITIVE_FLOAT.fieldOf("fly_speed").forGetter(Storage::flySpeed),
                ExtraCodecs.POSITIVE_INT.fieldOf("fly_ticks").forGetter(Storage::flyTicks),
                Codec.BOOL.fieldOf("could_glide").orElse(false).forGetter(Storage::couldGlide),
                Codec.BOOL.fieldOf("horizontal_flight").orElse(false).forGetter(Storage::horizontalFlight)
        ).apply(instance, Storage::new));
    }
}
