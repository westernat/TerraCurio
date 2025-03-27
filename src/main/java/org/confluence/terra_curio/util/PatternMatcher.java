package org.confluence.terra_curio.util;

import net.minecraft.world.item.crafting.RecipeInput;
import org.joml.Vector2i;

import java.util.List;

public class PatternMatcher {
    public static Vector2i findPatternTopLeft(List<String> pattern, int patternWidth, int patternHeight) {
        int x = patternWidth - 1, y = patternHeight - 1;
        for (int i = 0; i < patternHeight; i++) {
            String s = pattern.get(i);
            for (int j = 0; j < patternWidth; j++) {
                if (s.charAt(j) != ' ') {
                    if (x > j) x = j;
                    if (y > i) y = i;
                }
            }
        }
        return new Vector2i(x, y);
    }

    public static Vector2i findContainerTopLeft(RecipeInput container, int recipeWidth, int recipeHeight) {
        int x = recipeWidth - 1, y = recipeHeight - 1;
        for (int i = 0; i < recipeHeight; i++) {
            int dy = i * recipeWidth;
            for (int j = 0; j < recipeWidth; j++) {
                if (!container.getItem(j + dy).isEmpty()) {
                    if (x > j) x = j;
                    if (y > i) y = i;
                }
            }
        }
        return new Vector2i(x, y);
    }
}
