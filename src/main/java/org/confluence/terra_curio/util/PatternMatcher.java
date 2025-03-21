package org.confluence.terra_curio.util;

import net.minecraft.world.item.crafting.RecipeInput;
import org.joml.Vector2i;

import java.util.List;

public class PatternMatcher {
    public static Vector2i findPatternTopLeft(List<String> pattern, int patternWidth, int patternHeight) {
        int x = -1, y = -1;
        for (int j = 0; j < patternHeight; j++) {
            String row = pattern.get(j);
            for (int i = 0; i < patternWidth; i++) {
                if (row.charAt(i) != ' ') {
                    y = j;
                    x = i;
                    break;
                }
            }
            if (y != -1) {
                break;
            }
        }
        if (x == -1) x = 0;
        if (y == -1) y = 0;
        return new Vector2i(x, y);
    }

    public static Vector2i findContainerTopLeft(RecipeInput container, int recipeWidth, int recipeHeight) {
        int x = -1, y = -1;
        for (int j = 0; j < recipeHeight; j++) {
            for (int i = 0; i < recipeWidth; i++) {
                if (!container.getItem(i + j * recipeWidth).isEmpty()) {
                    y = j;
                    x = i;
                    break;
                }
            }
            if (y != -1) {
                break;
            }
        }
        if (x == -1) x = 0;
        if (y == -1) y = 0;
        return new Vector2i(x, y);
    }
}
