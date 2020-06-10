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
            if (!headStack.isEmpty()) {
                CompoundTag headTag = new CompoundTag();
                headTag.putString("Potion", Registry.POTION.getId(PotionUtil.getPotion(headStack)).toString());
                potionsTag.put("head", headTag);
            }
            if (!handleStack.isEmpty()) {
                CompoundTag handleTag = new CompoundTag();
                handleTag.putString("Potion", Registry.POTION.getId(PotionUtil.getPotion(handleStack)).toString());
                potionsTag.put("handle", handleTag);
            }

            ItemStack newToolStack = null;
            Identifier toolId = Registry.ITEM.getId(toolStack.getItem());
            TippedItemUtil.TippedType type = TippedItemUtil.TippedType.NONE;
            if(toolId.getNamespace().equals(PotionTipped.MOD_ID)){
                toolId = Registry.ITEM.getId(((TippedTool)toolStack.getItem()).getParent());
                if(headStack.isEmpty()){
                    potionsTag.put("head", toolStack.getSubTag("head"));
                    type = TippedItemUtil.getTippedType(type.getValue() | TippedItemUtil.TippedType.HEAD.getValue());
                }
                if(handleStack.isEmpty()){
                    potionsTag.put("handle", toolStack.getSubTag("handle"));
                    type = TippedItemUtil.getTippedType(type.getValue() | TippedItemUtil.TippedType.HANDLE.getValue());
                }
            }
            if(!headStack.isEmpty())
                type = TippedItemUtil.getTippedType(type.getValue() | TippedItemUtil.TippedType.HEAD.getValue());

            if(!handleStack.isEmpty())
                type = TippedItemUtil.getTippedType(type.getValue() | TippedItemUtil.TippedType.HANDLE.getValue());

            System.out.println(TippedItemUtil.getSuffixFromType(type) + " : " + type.getValue());
            newToolStack = new ItemStack(PotionTipped.TIPPED_TOOLS.get(new Identifier(toolId.getNamespace(), toolId.getPath() + "-" + TippedItemUtil.getSuffixFromType(type))));

            newToolStack.setTag(potionsTag);
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
