package com.github.Crupette.potiontipped.item;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.util.TippedItemUtil;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

public class TippedToolRegistry {

    public static void registerMiningTool(Identifier id, Item entry){
        PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-head"), Registry.register(
                Registry.ITEM,
                new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-head"),
                new TippedMiningToolItem((MiningToolItem) entry, TippedItemUtil.TippedType.HEAD)));
        PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-handle"), Registry.register(
                Registry.ITEM,
                new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-handle"),
                new TippedMiningToolItem((MiningToolItem) entry, TippedItemUtil.TippedType.HANDLE)));
        PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-both"), Registry.register(
                Registry.ITEM,
                new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-both"),
                new TippedMiningToolItem((MiningToolItem) entry, TippedItemUtil.TippedType.BOTH)));
        PotionTipped.log(Level.INFO, "Registered potion tipped variants for " + id);
    }

    public static void registerSwordTool(Identifier id, Item entry){
        PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-head"), Registry.register(
                Registry.ITEM,
                new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-head"),
                new TippedSwordItem((SwordItem) entry, TippedItemUtil.TippedType.HEAD)));
        PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-handle"), Registry.register(
                Registry.ITEM,
                new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-handle"),
                new TippedSwordItem((SwordItem) entry, TippedItemUtil.TippedType.HANDLE)));
        PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-both"), Registry.register(
                Registry.ITEM,
                new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-both"),
                new TippedSwordItem((SwordItem) entry, TippedItemUtil.TippedType.BOTH)));
        PotionTipped.log(Level.INFO, "Registered potion tipped variants for " + id);
    }
}
