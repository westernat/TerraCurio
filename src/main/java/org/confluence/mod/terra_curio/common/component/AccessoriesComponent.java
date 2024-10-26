package org.confluence.mod.terra_curio.common.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.confluence.mod.terra_curio.TerraCurio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public record AccessoriesComponent(Set<ResourceLocation> types) implements DataComponentType<AccessoriesComponent> {
    public static final Codec<AccessoriesComponent> CODEC = Codec.list(ResourceLocation.CODEC).xmap(
            l -> new AccessoriesComponent(new HashSet<>(l)),
            c -> new ArrayList<>(c.types)
    );
    public static final StreamCodec<FriendlyByteBuf, AccessoriesComponent> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(FriendlyByteBuf buffer, AccessoriesComponent value) {
            buffer.writeJsonWithCodec(CODEC, value);
        }

        @Override
        @NotNull
        public AccessoriesComponent decode(FriendlyByteBuf buffer) {
            return buffer.readJsonWithCodec(CODEC);
        }
    };

    public static final ResourceLocation FULL_INFORMATION = TerraCurio.asResource("full_information"),
            HOUR_WATCH = TerraCurio.asResource("hour_watch"),
            HALF_HOUR_WATCH = TerraCurio.asResource("half_hour_watch"),
            MINUTE_WATCH = TerraCurio.asResource("minute_watch"),
            WEATHER_RADIO = TerraCurio.asResource("weather_radio"),
            SEXTANT = TerraCurio.asResource("sextant"),
            FISHERMANS_POCKET_GUIDE = TerraCurio.asResource("fishermans_pocket_guide"),
            METAL_DETECTOR = TerraCurio.asResource("metal_detector"),
            LIFE_FORM_ANALYZER = TerraCurio.asResource("life_form_analyzer"),
            RADAR = TerraCurio.asResource("radar"),
            TALLY_COUNTER = TerraCurio.asResource("tally_counter"),
            DPS_METER = TerraCurio.asResource("dps_meter"),
            STOPWATCH = TerraCurio.asResource("stopwatch"),
            COMPASS = TerraCurio.asResource("compass"),
            DEPTH_METER = TerraCurio.asResource("depth_meter");
    public static final ResourceLocation STEP_STOOL = TerraCurio.asResource("step_stool");
    public static final ResourceLocation BASE_SPEED_BOOTS = TerraCurio.asResource("base_speed_boots");

    @Override
    public @Nullable Codec<AccessoriesComponent> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<FriendlyByteBuf, AccessoriesComponent> streamCodec() {
        return STREAM_CODEC;
    }
}
