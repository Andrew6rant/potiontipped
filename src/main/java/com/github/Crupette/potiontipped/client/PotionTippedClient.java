package com.github.Crupette.potiontipped.client;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.item.TippedMiningToolItem;
import com.github.Crupette.potiontipped.item.TippedSwordItem;
import com.github.Crupette.potiontipped.item.TippedTool;
import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Environment(EnvType.CLIENT)
public class PotionTippedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        for(Map.Entry<Identifier, Item> entry : PotionTipped.TIPPED_TOOLS.entrySet()){
            Identifier itemName = PotionTipped.id(entry.getKey().toString().replace(':', '-'));
            Item item = entry.getValue();

            addToolToResourcePack(item, itemName);
        }

        RegistryEntryAddedCallback.event(Registry.ITEM).register((i, identifier, item) -> {
            if(item instanceof TippedSwordItem || item instanceof TippedMiningToolItem){
                addToolToResourcePack(item, identifier);
            }
        });
    }

    private void addToolToResourcePack(Item item, Identifier itemName){
        Artifice.registerAssets(PotionTipped.MOD_ID + "_" + itemName.getPath(), pack -> {
            TippedTool tippedTool = ((TippedTool)item);
            Identifier base = Registry.ITEM.getId(tippedTool.getParent());

            boolean head =   itemName.getPath().contains("-head")   | itemName.getPath().contains("-both");
            boolean handle = itemName.getPath().contains("-handle") | itemName.getPath().contains("-both");

            AtomicInteger nextlayer = new AtomicInteger(1);
            pack.addItemModel(itemName, model -> {
                model = model.parent(new Identifier(base.getNamespace(), "item/" + base.getPath()));
                if(handle) model = model.texture("layer" + nextlayer.getAndIncrement(), PotionTipped.id("item/handle"));
                if(head) model = model.texture("layer" + nextlayer.getAndIncrement(), PotionTipped.id(getHeadString(tippedTool)));
            });
        });
    }

    private String getHeadString(TippedTool tippedTool) {
        Item parentItem = tippedTool.getParent();
        if(parentItem instanceof AxeItem){
            return "item/axe";
        }
        if(parentItem instanceof PickaxeItem){
            return "item/pickaxe";
        }
        if(parentItem instanceof ShovelItem){
            return "item/shovel";
        }
        if(parentItem instanceof HoeItem){
            return "item/hoe";
        }
        if(parentItem instanceof SwordItem){
            return "item/sword";
        }
        return "item/unknown";
    }
}
