package com.github.Crupette.potiontipped.recipe;

import com.github.Crupette.potiontipped.PotionTipped;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PotionTippedRecipeSerializers {

    public static SpecialRecipeSerializer<TippedToolRecipe> TIPPED_TOOL;

    public static void init(){
        TIPPED_TOOL = Registry.register(
                Registry.RECIPE_SERIALIZER, new Identifier(PotionTipped.MOD_ID, "crafting_special_tippedtool"), new SpecialRecipeSerializer<>(TippedToolRecipe::new));
    }
}
