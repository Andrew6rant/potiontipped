package com.github.Crupette.potiontipped.mixin;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.item.*;
import net.minecraft.item.AxeItem;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Registry.class)
public abstract class RegistryMixin {

    @Inject(
            method = "register(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;",
            at = @At("HEAD"))
    private static <V, T extends V> void register(Registry<V> registry, Identifier id, T entry, CallbackInfoReturnable<T> ci){
        if(entry instanceof MiningToolItem && !(entry instanceof HeadTippedMiningToolItem) && !(entry instanceof HandleTippedMiningToolItem) && !(entry instanceof BothTippedMiningToolItem)){
            PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-head"), Registry.register(
                    Registry.ITEM,
                    new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-head"),
                    new HeadTippedMiningToolItem((MiningToolItem) entry)));
            PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-handle"), Registry.register(
                    Registry.ITEM,
                    new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-handle"),
                    new HandleTippedMiningToolItem((MiningToolItem) entry)));
            PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-both"), Registry.register(
                    Registry.ITEM,
                    new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-both"),
                    new BothTippedMiningToolItem((MiningToolItem) entry)));
            PotionTipped.log(Level.INFO, "Registered potion tipped variants for " + id);
        }
        if(entry instanceof SwordItem && !(entry instanceof HeadTippedSwordItem) && !(entry instanceof HandleTippedSwordItem) && !(entry instanceof BothTippedSwordItem)){
            PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-head"), Registry.register(
                    Registry.ITEM,
                    new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-head"),
                    new HeadTippedSwordItem((SwordItem) entry)));
            PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-handle"), Registry.register(
                    Registry.ITEM,
                    new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-handle"),
                    new HandleTippedSwordItem((SwordItem) entry)));
            PotionTipped.TIPPED_TOOLS.put(new Identifier(id.getNamespace(), id.getPath() + "-both"), Registry.register(
                    Registry.ITEM,
                    new Identifier(PotionTipped.MOD_ID, id.getNamespace() + "-" + id.getPath() + "-both"),
                    new BothTippedSwordItem((SwordItem) entry)));
            PotionTipped.log(Level.INFO, "Registered potion tipped variants for " + id);
        }
    }
}
