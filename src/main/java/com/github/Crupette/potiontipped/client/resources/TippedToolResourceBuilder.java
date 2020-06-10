package com.github.Crupette.potiontipped.client.resources;

import com.github.Crupette.potiontipped.item.TippedTool;
import com.github.Crupette.potiontipped.util.TippedItemUtil;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.system.CallbackI;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TippedToolResourceBuilder {
    private final ResourcePackCreator creator;
    private final Map.Entry<Identifier, Item> tool;

    public TippedToolResourceBuilder(ResourcePackCreator creator, Map.Entry<Identifier, Item> tool){
        this.creator = creator;
        this.tool = tool;
    }

    private String getHeadString(Item parent){
        if(parent instanceof AxeItem){
            return "axe";
        }else
        if(parent instanceof PickaxeItem){
            return "pickaxe";
        }else
        if(parent instanceof ShovelItem){
            return "shovel";
        }else
        if(parent instanceof HoeItem){
            return "hoe";
        }else
        if(parent instanceof SwordItem){
            return "sword";
        }else
        return "axe";
    }

    public String build(){
        Item item = tool.getValue();
        Item parent = ((TippedTool)item).getParent();
        Identifier id = tool.getKey();
        StringBuilder ret = new StringBuilder();
        ret.append("{ \"parent\": \"");

        Identifier base = Registry.ITEM.getId(parent);
        int layer = 1;
        ret.append(base.getNamespace()).append(":").append("item/").append(base.getPath()).append("\", \"textures\": { ");
        TippedItemUtil.TippedType type = ((TippedTool)item).getType();

        switch (type){
            case BOTH:
                ret.append("\"layer2\": \"potiontipped:item/handle\", ");
            case HEAD:
                ret.append("\"layer1\": \"potiontipped:item/").append(getHeadString(parent)).append("\" ");
                break;
            case HANDLE:
                ret.append("\"layer1\": \"potiontipped:item/handle\" ");
                break;
        }

        ret.append("} }");

        return ret.toString();
    }
}
