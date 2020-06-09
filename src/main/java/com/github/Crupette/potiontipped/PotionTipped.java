package com.github.Crupette.potiontipped;

import com.github.Crupette.potiontipped.recipe.PotionTippedRecipeSerializers;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionTipped implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "potiontipped";
    public static final String MOD_NAME = "Potion Tipped";

    public static final ItemGroup TIPPED_GROUP = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "tipped"),
            () -> new ItemStack(Items.POTION));

    public static final Map<Identifier, Item> TIPPED_TOOLS = new HashMap<>();

    @Override
    public void onInitialize() {
        PotionTippedRecipeSerializers.init();
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}