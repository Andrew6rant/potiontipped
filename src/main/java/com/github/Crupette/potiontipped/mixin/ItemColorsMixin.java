package com.github.Crupette.potiontipped.mixin;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.item.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ItemColors.class)
public abstract class ItemColorsMixin {

    @Inject(
            method = "create(Lnet/minecraft/client/color/block/BlockColors;)Lnet/minecraft/client/color/item/ItemColors;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/color/item/ItemColors;register(Lnet/minecraft/client/color/item/ItemColorProvider;[Lnet/minecraft/item/ItemConvertible;)V",
                    ordinal = 3),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void addPotionBucketColors(BlockColors blockColors, CallbackInfoReturnable<ItemColors> ci,
                                              ItemColors itemColors){
        for(Item item : PotionTipped.TIPPED_TOOLS.values()){
            if(item instanceof TippedMiningToolItem || item instanceof TippedSwordItem){
                itemColors.register(((stack, tintIndex) -> {
                    switch (((TippedTool)stack.getItem()).getType()){
                        case BOTH:
                        case HEAD:
                            switch (tintIndex){
                                case 2:
                                    return PotionUtil.getColor(PotionUtil.getPotion(stack.getOrCreateSubTag("head")));
                                case 1:
                                    return PotionUtil.getColor(PotionUtil.getPotion(stack.getOrCreateSubTag("handle")));
                                default:
                                    return -1;
                            }
                        case HANDLE:
                            return tintIndex == 1 ? PotionUtil.getColor(PotionUtil.getPotion(stack.getOrCreateSubTag("handle"))) : -1;
                        default:
                            return -1;
                    }
                }), item);
            }
        }
    }
}
