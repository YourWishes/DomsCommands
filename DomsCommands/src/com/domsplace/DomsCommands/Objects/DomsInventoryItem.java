/*
 * Copyright 2013 Dominic Masters.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domsplace.DomsCommands.Objects;

import java.util.List;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Dominic Masters
 */
public class DomsInventoryItem {
    public static DomsInventoryItem createInventoryItem(List<DomsItem> items) {
        return new DomsInventoryItem(items.size(), items.get(0));
    }

    public static DomsInventoryItem createFromString(String string) {
        try {
            List<DomsItem> stack = DomsItem.createItems(string);
            return createFromDomsItemList(stack);
        } catch(Exception e) {
            return null;
        }
    }
    
    public static DomsInventoryItem createFromDomsItemList(List<DomsItem> items) {
        if(items == null || items.size() < 1) return null;
        return new DomsInventoryItem(items.size(), items.get(0));
    }
    
    //Instance
    private int size;
    private DomsItem item;
    
    public DomsInventoryItem (int size, DomsItem item) {
        this.size = size;
        this.item = item;
    }
    
    public DomsInventoryItem(ItemStack is) {
        this(is.getAmount(), DomsItem.createItem(is));
    }
    
    public int getSize() {return this.size;}
    public DomsItem getItem() {return this.item;}
    
    @Override public String toString() {return "{size:\"" + size + "\"}," + item.toString();}
    public ItemStack toItemStack() {try {return this.item.getItemStack(this.size);}catch(Exception e) {return null;}}
}
