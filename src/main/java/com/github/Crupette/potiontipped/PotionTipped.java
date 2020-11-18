package com.github.Crupette.potiontipped;

import com.github.Crupette.potiontipped.item.TippedMiningToolItem;
import com.github.Crupette.potiontipped.item.TippedSwordItem;
import com.github.Crupette.potiontipped.item.TippedToolRegistry;
import com.github.Crupette.potiontipped.recipe.PotionTippedRecipeSerializers;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        PotionTippedRecipeSerializers.init();

        Registry.ITEM.forEach(item -> {
            Identifier identifier = Registry.ITEM.getId(item);
            if(item instanceof MiningToolItem && !(item instanceof TippedMiningToolItem)){
                TippedToolRegistry.registerMiningTool(identifier, item);
            }
            if(item instanceof SwordItem && !(item instanceof TippedSwordItem)){
                TippedToolRegistry.registerSwordTool(identifier, item);
            }
        });

        RegistryEntryAddedCallback.event(Registry.ITEM).register((i, identifier, item) -> {
            if(item instanceof MiningToolItem && !(item instanceof TippedMiningToolItem)){
                TippedToolRegistry.registerMiningTool(identifier, item);
            }
            if(item instanceof SwordItem && !(item instanceof TippedSwordItem)){
                TippedToolRegistry.registerSwordTool(identifier, item);
            }
        });
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}