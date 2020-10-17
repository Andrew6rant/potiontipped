package com.github.Crupette.potiontipped.recipe;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.item.TippedTool;
import com.github.Crupette.potiontipped.util.TippedItemUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class TippedToolRecipe extends SpecialCraftingRecipe {
    public TippedToolRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        if(inv.getWidth() == 3 && inv.getHeight() == 3){
            for(int row = 0; row < inv.getWidth(); row++){
                ItemStack headStack = inv.getStack(row);
                ItemStack toolStack = inv.getStack(row + inv.getWidth());
                ItemStack handleStack = inv.getStack(row + inv.getWidth() * 2);

                if(toolStack.isEmpty()) continue;
                if(headStack.isEmpty() && handleStack.isEmpty()) {
                    continue;
                }
                if(!headStack.isEmpty() && !(headStack.getItem() instanceof PotionItem)) {
                    return false;
                }
                if(!handleStack.isEmpty() && !(handleStack.getItem() instanceof PotionItem)) {
                    return false;
                }

                if(!headStack.isEmpty() && (PotionUtil.getPotion(headStack).hasInstantEffect() || PotionUtil.getPotion(headStack).getEffects().isEmpty())) {
                    return false;
                }
                if(!handleStack.isEmpty() && (PotionUtil.getPotion(handleStack).hasInstantEffect() || PotionUtil.getPotion(handleStack).getEffects().isEmpty())) {
                    return false;
                }
                if(toolStack.getItem() instanceof MiningToolItem || toolStack.getItem() instanceof SwordItem){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        for(int row = 0; row < inv.getWidth(); row++) {
            ItemStack headStack = inv.getStack(row);
            ItemStack toolStack = inv.getStack(row + inv.getWidth());
            ItemStack handleStack = inv.getStack(row + inv.getWidth() * 2);

            if (toolStack.isEmpty()) continue;
            if (headStack.isEmpty() && handleStack.isEmpty()) continue;

            CompoundTag potionsTag = new CompoundTag();

            ItemStack newToolStack;
            Identifier toolId = Registry.ITEM.getId(toolStack.getItem());
            TippedItemUtil.TippedSide type = TippedItemUtil.TippedSide.NONE;

            if(toolId.getNamespace().equals(PotionTipped.MOD_ID)){
                toolId = Registry.ITEM.getId(((TippedTool)toolStack.getItem()).getParent());
                TippedTool tool = ((TippedTool)toolStack.getItem());
                System.out.println("Merging types " + type + " and " + tool.getType());
                type = TippedItemUtil.getTippedType(type.getValue() | tool.getType().getValue());

                if(toolStack.getOrCreateTag().contains("head")) potionsTag.put("head", toolStack.getOrCreateSubTag("head").copy());
                if(toolStack.getOrCreateTag().contains("handle")) potionsTag.put("handle", toolStack.getOrCreateSubTag("handle").copy());
            }

            if (!headStack.isEmpty()) {
                type = TippedItemUtil.getTippedType(type.getValue() | TippedItemUtil.TippedSide.HEAD.getValue());
                CompoundTag headTag = new CompoundTag();

                ListTag potionEffectsTag = new ListTag();
                PotionUtil.getPotion(headStack).getEffects().forEach(effect -> potionEffectsTag.add(effect.toTag(new CompoundTag())));
                headTag.put("CustomPotionEffects", potionEffectsTag);
                headTag.putInt("Uses", 8);
                potionsTag.put("head", headTag);
            }
            if (!handleStack.isEmpty()) {
                type = TippedItemUtil.getTippedType(type.getValue() | TippedItemUtil.TippedSide.HANDLE.getValue());
                CompoundTag handleTag = new CompoundTag();

                ListTag potionEffectsTag = new ListTag();
                PotionUtil.getPotion(handleStack).getEffects().forEach(effect -> potionEffectsTag.add(effect.toTag(new CompoundTag())));
                handleTag.put("CustomPotionEffects", potionEffectsTag);

                potionsTag.put("handle", handleTag);
            }

            newToolStack = new ItemStack(PotionTipped.TIPPED_TOOLS.get(new Identifier(toolId.getNamespace(), toolId.getPath() + "-" + TippedItemUtil.getSuffixFromType(type))));

            newToolStack.setTag(toolStack.getOrCreateTag().copy());
            newToolStack.getOrCreateTag().put("head", potionsTag.get("head"));
            newToolStack.getOrCreateTag().put("handle", potionsTag.get("handle"));

            return newToolStack;
        }
        return ItemStack.EMPTY;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PotionTippedRecipeSerializers.TIPPED_TOOL;
    }
}
