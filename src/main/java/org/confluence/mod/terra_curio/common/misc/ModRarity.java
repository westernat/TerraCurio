package org.confluence.mod.terra_curio.common.misc;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;

import java.awt.*;
import java.util.function.IntFunction;

public enum ModRarity implements StringRepresentable, IExtensibleEnum {
    /*

    public static final Rarity CYAN = Rarity.create("cyan", style -> style.withColor(0x05C8FF));
    public static final Rarity RED = Rarity.create("red", style -> style.withColor(0xFF2864));
    public static final Rarity PURPLE = Rarity.create("purple", style -> style.withColor(0xB428FF));

    public static final Rarity EXPERT = Rarity.create("expert", style -> style.withColor(0x000000));
    public static final Rarity MASTER = Rarity.create("master", style -> style.withColor(0x000000));
    public static final Rarity QUEST = Rarity.create("quest", style -> style.withColor(0xFFAF00));

    public interface Special {
        MutableComponent withColor(String descriptionId);
    }

    public interface Expert extends Special {
        @Override
        default MutableComponent withColor(String descriptionId) {
            return Component.translatable(descriptionId).withStyle(style -> style.withColor(AnimateColor.getExpertColor()));
        }
    }

    public interface Master extends Special {
        @Override
        default MutableComponent withColor(String descriptionId) {
            return Component.translatable(descriptionId).withStyle(style -> style.withColor(AnimateColor.getMasterColor()));
        }
    }
    */
    RED_BOLD_UNDERLINE(0,"red_bold",Style.EMPTY.withColor(Color.red.getRGB()).withBold(true).withUnderlined(true)),

    WHITE(1, "white", Color.WHITE),
    BLUE(2, "blue", Color.BLUE),
    GREEN(3, "green", Color.GREEN),
    YELLOW(4,"yellow",Color.YELLOW),
    ORANGE(5,"orange",Color.orange),
    PINK(6,"pink",Color.pink),
    GRAY(7, "gray", Color.GRAY),
    LIGHT_PURPLE(8,"light_purple",0xD2A0FF),
    LIME(9,"lime",0x96FF0A),

    LIGHT_RED(10,"light_red",0xFF9696);






    private final int id;
    private final String name;
    public  final Style style;

    ModRarity(int id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.style = Style.EMPTY.withColor(color.getRGB());
    }
    ModRarity(int id, String name, int colorRGB) {
        this.id = id;
        this.name = name;
        this.style = Style.EMPTY.withColor(colorRGB);
    }
    ModRarity(int id, String name, Style style) {
        this.id = id;
        this.name = name;
        this.style = style;
    }

    public int color() {
        if(style.getColor()==null)return 0;
        return this.style.getColor().getValue();
    }






    public String getSerializedName() {return this.name; }
    public static final Codec<ModRarity> CODEC = StringRepresentable.fromValues(ModRarity::values);
    public static final IntFunction<ModRarity> BY_ID = ByIdMap.continuous((p_335877_) -> p_335877_.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, ModRarity> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, (p_335484_) -> p_335484_.id);
}
