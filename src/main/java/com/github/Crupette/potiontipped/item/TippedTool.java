package com.github.Crupette.potiontipped.item;

import com.github.Crupette.potiontipped.util.TippedItemUtil;
import net.minecraft.item.Item;

public interface TippedTool {
    Item getParent();
    TippedItemUtil.TippedType getType();
}
