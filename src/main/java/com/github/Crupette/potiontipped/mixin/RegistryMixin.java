package com.github.Crupette.potiontipped.mixin;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.item.*;
import net.minecraft.item.Item;
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
        if(entry instanceof MiningToolItem && !(entry instanceof TippedMiningToolItem)){
            TippedToolRegistry.registerMiningTool(id, (Item)entry);
        }
        if(entry instanceof SwordItem && !(entry instanceof TippedSwordItem)){
            TippedToolRegistry.registerSwordTool(id, (Item)entry);
        }
    }
}
