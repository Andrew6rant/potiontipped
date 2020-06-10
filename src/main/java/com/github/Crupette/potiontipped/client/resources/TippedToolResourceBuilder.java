package com.github.Crupette.potiontipped.client.resources;

import com.github.Crupette.potiontipped.item.TippedTool;
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

    public String build(){
        Item item = tool.getValue();
        Item parent = ((TippedTool)item).getParent();
        Identifier id = tool.getKey();
        StringBuilder ret = new StringBuilder();
        ret.append("{ \"parent\": \"");

        Identifier base = Registry.ITEM.getId(parent);
        ret.append(base.getNamespace()).append(":").append("item/").append(base.getPath()).append("\", \"textures\": { ");
        ret.append("\"layer1\": \"");
        switch(((TippedTool)item).getType()){
            case BOTH:
                ret.append("potiontipped:item/handle\", \"layer2\": \"");
            case HEAD:
                String append = "";
                if(parent instanceof AxeItem){
                    append = "axe";
                }else if(parent instanceof PickaxeItem){
                    append = "pickaxe";
                }else if(parent instanceof ShovelItem){
                    append = "shovel";
                }else if(parent instanceof HoeItem){
                    append = "hoe";
                }else if(parent instanceof SwordItem){
                    append = "sword";
                }else {
                    append = "pickaxe";
                }
                ret.append("potiontipped:item/").append(append).append("\"");
                break;
            case HANDLE:
                ret.append("potiontipped:item/handle\" ");
                break;
        }
        ret.append("} }");

        return ret.toString();
    }
}
