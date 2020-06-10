package com.github.Crupette.potiontipped.client;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.client.resources.ResourcePackCreator;
import com.github.Crupette.potiontipped.client.resources.TippedToolResourceBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PotionTippedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ResourcePackCreator creator = new ResourcePackCreator(PotionTipped.MOD_ID);

        for(Map.Entry<Identifier, Item> entry : PotionTipped.TIPPED_TOOLS.entrySet()){
            Identifier itemId = Registry.ITEM.getId(entry.getValue());
            creator.addResource("assets/potiontipped/models/item/", itemId.getPath() + ".json", new TippedToolResourceBuilder(creator, entry).build());
        }
    }
}
