package com.github.Crupette.potiontipped.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TippedItemUtil {
    public enum TippedType {
        NONE, HEAD, HANDLE, BOTH
    }

    @Environment(EnvType.CLIENT)
    public static void appendTooltips(ItemStack stack, List<Text> tooltip){
        Potion headPotion = PotionUtil.getPotion(stack.getOrCreateSubTag("head"));
        Potion handlePotion = PotionUtil.getPotion(stack.getOrCreateSubTag("handle"));

        appendPotionToTooltip(headPotion, tooltip, "head");
        appendPotionToTooltip(handlePotion, tooltip, "handle");
    }

    @Environment(EnvType.CLIENT)
    public static void appendPotionToTooltip(Potion potion, List<Text> tooltip, String type){
        if(potion != Potions.EMPTY){
            tooltip.add(new TranslatableText("tooltip.potiontipped." + type).formatted(Formatting.WHITE));
            for(StatusEffectInstance effectInstance : potion.getEffects()){
                MutableText mutableText = new TranslatableText(effectInstance.getTranslationKey());
                StatusEffect statusEffect = effectInstance.getEffectType();

                StatusEffectInstance adjusted = new StatusEffectInstance(
                        effectInstance.getEffectType(), effectInstance.getDuration() / 10, effectInstance.getAmplifier());

                if(effectInstance.getAmplifier() > 0){
                    mutableText.append(" ").append(new TranslatableText("potion.potency." + effectInstance.getAmplifier()));
                }

                if(adjusted.getDuration() > 20 && !type.equals("handle")){
                    mutableText.append(" (").append(StatusEffectUtil.durationToString(adjusted, 1.F)).append(")");
                }
                tooltip.add(mutableText.formatted(statusEffect.getType().getFormatting()));
            }
        }
    }

    public static void expandStacks(Item item, Potion basePotion, DefaultedList<ItemStack> stacks, TippedType type){
        Identifier baseIdentifier = Registry.POTION.getId(basePotion);
        ItemStack stack = new ItemStack(item);
        if(type == TippedType.HEAD){
            stack.getOrCreateSubTag("head").putString("Potion", baseIdentifier.toString());
            stacks.add(stack);
        }
        if(type == TippedType.HANDLE){
            stack.getOrCreateSubTag("handle").putString("Potion", baseIdentifier.toString());
            stacks.add(stack);
        }
        if(type == TippedType.BOTH){
            Registry.POTION.forEach((potion -> {
                Identifier potionIdentifier = Registry.POTION.getId(potion);
                if(!potion.getEffects().isEmpty() && !potion.hasInstantEffect()){
                    ItemStack newStack = new ItemStack(item);
                    newStack.getOrCreateSubTag("head").putString("Potion", baseIdentifier.toString());
                    newStack.getOrCreateSubTag("handle").putString("Potion", potionIdentifier.toString());
                    stacks.add(newStack);
                }
            }));
        }
    }

    public static void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker){
        Potion headPotion = PotionUtil.getPotion(stack.getOrCreateSubTag("head"));

        if(headPotion != Potions.EMPTY){
            for(StatusEffectInstance effectInstance : PotionUtil.getPotionEffects(headPotion, Collections.emptyList())){
                StatusEffectInstance adjusted = new StatusEffectInstance(
                        effectInstance.getEffectType(), effectInstance.getDuration() / 10, effectInstance.getAmplifier());

                if(headPotion.hasInstantEffect()){
                    adjusted.getEffectType().applyInstantEffect(attacker, attacker, target, adjusted.getAmplifier(), 1.D);
                }else{
                    target.addStatusEffect(adjusted);
                }
            }
        }
    }

    public static void inventoryTick(ItemStack stack, LivingEntity holder){
        Potion handlePotion = PotionUtil.getPotion(stack.getOrCreateSubTag("handle"));

        if(handlePotion != Potions.EMPTY){
            for(StatusEffectInstance effectInstance : PotionUtil.getPotionEffects(handlePotion, Collections.emptyList())){
                StatusEffectInstance adjusted = new StatusEffectInstance(
                        effectInstance.getEffectType(), 4, effectInstance.getAmplifier());

                if(!handlePotion.hasInstantEffect()){
                    holder.addStatusEffect(adjusted);
                }
            }
        }
    }
}
