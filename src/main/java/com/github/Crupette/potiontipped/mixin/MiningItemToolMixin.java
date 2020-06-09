package com.github.Crupette.potiontipped.mixin;

import com.github.Crupette.potiontipped.item.MiningToolExposer;
import net.minecraft.block.Block;
import net.minecraft.item.MiningToolItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(MiningToolItem.class)
public abstract class MiningItemToolMixin implements MiningToolExposer {

    @Shadow @Final private Set<Block> effectiveBlocks;

    @Override
    public Set<Block> getEffectiveBlocks() {
        return this.effectiveBlocks;
    }
}
