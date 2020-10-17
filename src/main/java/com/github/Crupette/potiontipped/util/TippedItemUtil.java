package com.github.Crupette.potiontipped.util;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.item.TippedTool;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.potion.Potion;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class TippedItemUtil {

    public enum TippedSide {
        NONE(0),
        HEAD(1),
        HANDLE(2),
        BOTH(3);

        private final int value;
        TippedSide(int value){
            this.value = value;
        }

        public int getValue() { return value; }
    }

    public static TippedSide getTippedType(int i) {
        switch (i){
            case 1: return TippedSide.HEAD;
            case 2: return TippedSide.HANDLE;
            case 3: return TippedSide.BOTH;
            default: return TippedSide.NONE;
        }
    }

    public static TippedSide getOpposite(TippedSide type){
        switch(type){
            case NONE: return TippedSide.BOTH;
            case HEAD: return TippedSide.HANDLE;
            case HANDLE: return TippedSide.HEAD;
            default: return TippedSide.NONE;
        }
    }

    public static String getSuffixFromType(TippedSide type){
        switch (type){
            case HEAD: return "head";
            case HANDLE: return "handle";
            case BOTH: return "both";
            default: return "unknown";
        }
    }

    public static Identifier getTippedIdentifier(Item parent, TippedSide type){
        Identifier parentId = Registry.ITEM.getId(parent);
        return PotionTipped.id(parentId.getNamespace() + "-" + parentId.getPath() + "-" + getSuffixFromType(type));
    }

    @Environment(EnvType.CLIENT)
    public static void appendTooltips(ItemStack stack, List<Text> tooltip){
        if(stack.getSubTag("head") != null)
            appendTipToTooltip(stack.getOrCreateSubTag("head"), tooltip, TippedSide.HEAD);

        if(stack.getSubTag("handle") != null)
            appendTipToTooltip(stack.getOrCreateSubTag("handle"), tooltip, TippedSide.HANDLE);
    }

    @Environment(EnvType.CLIENT)
    public static void appendTipToTooltip(CompoundTag base, List<Text> tooltip, TippedSide type){
        ListTag effectListTags = base.getList("CustomPotionEffects", 10);
        if(effectListTags.size() != 0)
            tooltip.add(new TranslatableText("tooltip.potiontipped." + getSuffixFromType(type)).formatted(Formatting.WHITE));

        effectListTags.forEach(effectTag -> {
            StatusEffectInstance effectInstance = StatusEffectInstance.fromTag((CompoundTag) effectTag);

            MutableText mutableText = new TranslatableText(effectInstance.getTranslationKey());
            StatusEffect statusEffect = effectInstance.getEffectType();

            if(effectInstance.getAmplifier() > 0)
                mutableText.append(" ").append(new TranslatableText("potion.potency." + effectInstance.getAmplifier()));

            mutableText.append(" (").append(StatusEffectUtil.durationToString(effectInstance, 1.F)).append(")");
            tooltip.add(mutableText.formatted(statusEffect.getType().getFormatting()));
        });

        if(base.contains("Uses")){
            MutableText mutableText = new TranslatableText("tooltip.potiontipped.uses");

            mutableText.append(Integer.toString(base.getInt("Uses")));
            tooltip.add(mutableText);
        }
    }

    public static void expandStacks(Item item, Potion basePotion, DefaultedList<ItemStack> stacks, TippedSide type){
        ItemStack stack = new ItemStack(item);
        if(type != TippedSide.BOTH){
            tipItemstack(stack, basePotion, type);
            stacks.add(stack);
        }else {
            Registry.POTION.forEach((potion -> {
                ItemStack newStack = new ItemStack(item);
                tipItemstack(newStack, basePotion, TippedSide.HEAD);
                if(!potion.getEffects().isEmpty() && !potion.hasInstantEffect()){
                    tipItemstack(newStack, potion, TippedSide.HANDLE);
                    stacks.add(newStack);
                }
            }));
        }
    }

    public static void tipItemstack(ItemStack itemStack, Potion potion, TippedSide tip){
        ListTag tipEffects = new ListTag();
        potion.getEffects().forEach(effect -> tipEffects.add(effect.toTag(new CompoundTag())));
        itemStack.getOrCreateSubTag(getSuffixFromType(tip)).put("CustomPotionEffects", tipEffects);
        if(tip == TippedSide.HEAD){
            itemStack.getOrCreateSubTag(getSuffixFromType(tip)).putInt("Uses", 1);
        }
    }

    public static ItemStack detipItemstack(ItemStack itemStack, TippedSide tip){
        itemStack.removeSubTag(getSuffixFromType(tip));
        if(itemStack.getOrCreateSubTag(getSuffixFromType(getOpposite(tip))).isEmpty()){
            //Switch to normal item
            CompoundTag compoundTag = itemStack.getTag();
            Item parent = ((TippedTool)itemStack.getItem()).getParent();

            itemStack = new ItemStack(parent);
            itemStack.setTag(compoundTag);

            System.out.println("Fixing item");
        }else{
            CompoundTag compoundTag = itemStack.getTag();
            Identifier newId = getTippedIdentifier(((TippedTool)itemStack.getItem()).getParent(), getOpposite(tip));
            Item newTipItem = Registry.ITEM.get(newId);

            itemStack = new ItemStack(newTipItem);
            itemStack.setTag(compoundTag);

            System.out.println("Fixing item");
        }
        return itemStack;
    }

    public static void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker){
        if(!stack.getOrCreateTag().contains("head")) return;
        if(!stack.getOrCreateSubTag("head").contains("CustomPotionEffects")) return;

        ListTag headEffects = stack.getOrCreateSubTag("head").getList("CustomPotionEffects", 10);
        headEffects.forEach(effectTag -> {
            StatusEffectInstance effectInstance = StatusEffectInstance.fromTag((CompoundTag) effectTag);

            if(effectInstance.getEffectType().isInstant()){
                effectInstance.getEffectType().applyInstantEffect(attacker, attacker, target, effectInstance.getAmplifier(), 1.D);
            }else{
                StatusEffectInstance adjustedInstance = new StatusEffectInstance(
                        effectInstance.getEffectType(), effectInstance.getDuration() / 10, effectInstance.getAmplifier());
                target.addStatusEffect(adjustedInstance);
            }
        });

        int usesLeft = stack.getOrCreateSubTag("head").getInt("Uses");
        if(--usesLeft == 0){
            attacker.setStackInHand(attacker.getActiveHand(), detipItemstack(stack, TippedSide.HEAD));
        }else{
            stack.getOrCreateSubTag("head").putInt("Uses", usesLeft);
        }
    }

    public static void inventoryTick(ItemStack stack, LivingEntity holder, int slot){
        if(!stack.getOrCreateTag().contains("handle")) return;
        if(!stack.getOrCreateSubTag("handle").contains("CustomPotionEffects")) return;

        ListTag handleEffects = stack.getOrCreateSubTag("handle").getList("CustomPotionEffects", 10);
        List<Tag> toRemove = new ArrayList<>();
        handleEffects.forEach(effectTag -> {
            StatusEffectInstance effectInstance = StatusEffectInstance.fromTag((CompoundTag) effectTag);

            if(!effectInstance.getEffectType().isInstant()){
                StatusEffectInstance adjustedInstance = new StatusEffectInstance(
                        effectInstance.getEffectType(), 100, effectInstance.getAmplifier());
                holder.addStatusEffect(adjustedInstance);
            }

            int durationLeft = effectInstance.getDuration();
            if(--durationLeft == 0){
                toRemove.add(effectTag);
            }else{
                ((CompoundTag) effectTag).putInt("Duration", durationLeft);
            }
        });

        toRemove.forEach(handleEffects::remove);
        if(handleEffects.isEmpty()){
            if(holder instanceof PlayerEntity){
                ((PlayerEntity)holder).inventory.setStack(slot, detipItemstack(stack, TippedSide.HANDLE));
            }else {
                ((Inventory) holder).setStack(slot, detipItemstack(stack, TippedSide.HANDLE));
            }
        }
        /*Potion handlePotion = PotionUtil.getPotion(stack.getOrCreateSubTag("handle"));

        if(handlePotion != Potions.EMPTY){
            for(StatusEffectInstance effectInstance : PotionUtil.getPotionEffects(handlePotion, Collections.emptyList())){
                StatusEffectInstance adjusted = new StatusEffectInstance(
                        effectInstance.getEffectType(), 5, effectInstance.getAmplifier());

                if(!handlePotion.hasInstantEffect()){
                    holder.getStatusEffects();
                    holder.addStatusEffect(adjusted);
                }
            }
        }*/
    }
}
