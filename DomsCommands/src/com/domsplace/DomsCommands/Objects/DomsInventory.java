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

import com.domsplace.DomsCommands.Bases.Base;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Dominic Masters
 */
public class DomsInventory {
    public static DomsInventory createFromPlayer(DomsPlayer player) {
        String group = getInventoryGroupFromWorld(player.getLocation().getWorld());
        Inventory inv = player.getOnlinePlayer().getInventory();
        
        DomsInventory di = createFromInventory(group, inv, player);
        
        try {di.setHelmet(DomsInventoryItem.createInventoryItem(DomsItem.itemStackToDomsItems(player.getOnlinePlayer().getInventory().getHelmet())));} catch(Exception e) {}
        try {di.setChestPlate(DomsInventoryItem.createInventoryItem(DomsItem.itemStackToDomsItems(player.getOnlinePlayer().getInventory().getChestplate())));} catch(Exception e) {}
        try {di.setLeggings(DomsInventoryItem.createInventoryItem(DomsItem.itemStackToDomsItems(player.getOnlinePlayer().getInventory().getLeggings())));} catch(Exception e) {}
        try {di.setBoots(DomsInventoryItem.createInventoryItem(DomsItem.itemStackToDomsItems(player.getOnlinePlayer().getInventory().getBoots())));} catch(Exception e) {}
        
        di.setExp(player.getOnlinePlayer().getExp());
        di.setExpLevel(player.getOnlinePlayer().getLevel());
        
        return di;
    }
    
    public static DomsInventory createFromInventory(String group, Inventory inv, DomsPlayer player) {
        DomsInventory di = new DomsInventory(player, group);
        for(int i = 0; i < inv.getSize(); i++) {
            try {
                ItemStack is = inv.getItem(i);
                List<DomsItem> items = DomsItem.itemStackToDomsItems(is);
                DomsInventoryItem invItem = DomsInventoryItem.createInventoryItem(items);
                di.setItem(i, invItem);
            } catch(Exception e) {}
        }
        
        return di;
    }
    
    public static String getInventoryGroupFromWorld(String world) {
        MemorySection ms = (MemorySection) Base.getConfig().get("inventory.groups");
        for(String s : ms.getKeys(false)) {
            List<String> worlds = Base.getConfig().getStringList("inventory.groups." + s);
            for(String w : worlds) {
                if(w.equalsIgnoreCase(world)) return s;
            }
        }
        
        return "default";
    }

    public static DomsInventory createEndChestFromPlayer(DomsPlayer player) {
        return createFromInventory(getInventoryGroupFromWorld(player.getLocation().getWorld()), player.getOnlinePlayer().getEnderChest(), player);
    }
    
    //Instance
    private final Map<Integer, DomsInventoryItem> slots;
    
    private DomsInventoryItem helmet;
    private DomsInventoryItem chestplate;
    private DomsInventoryItem leggings;
    private DomsInventoryItem boots;
    
    private final DomsPlayer player;
    private String inventoryGroup;
    private int level = 0;
    private float xp = -1;
    
    public DomsInventory(DomsPlayer player, String world) {
        this.player = player;
        this.inventoryGroup = world;
        this.slots = new HashMap<Integer, DomsInventoryItem>();
    }
    
    public DomsInventoryItem getItem(int slot) {
        try {
            return slots.get(slot);
        } catch(Exception e) {return null;}
    }
    
    public boolean removeItem(int slot) {
        try {this.slots.remove(slot);return true;}catch(Exception e){return false;}
    }
    
    public void setItem(int slot, DomsInventoryItem item) {
        this.removeItem(slot);
        slots.put(slot, item);
    }
    
    public Map<Integer, DomsInventoryItem> getItems() {return new HashMap<Integer, DomsInventoryItem>(slots);}
    public DomsPlayer getPlayer() {return this.player;}
    public String getInventoryGroup() {return this.inventoryGroup;}
    public DomsInventoryItem getHelmet() {return this.helmet;}
    public DomsInventoryItem getChestPlate() {return this.chestplate;}
    public DomsInventoryItem getLeggings() {return this.leggings;}
    public DomsInventoryItem getBoots() {return this.boots;}
    public int getExpLevel() {return this.level;}
    public float getExp() {return this.xp;}
    
    public void setInventoryGroup(String world) {this.inventoryGroup = world;}
    public void setHelmet(DomsInventoryItem item) {this.helmet = item;}
    public void setChestPlate(DomsInventoryItem item) {this.chestplate = item;}
    public void setLeggings(DomsInventoryItem item) {this.leggings = item;}
    public void setBoots(DomsInventoryItem item) {this.boots = item;}
    public void setExpLevel(int l) {this.level = l;}
    public void setExp(float f) {this.xp = f;}

    public void setToPlayer() {
        if(!this.player.isOnline()) return;
        Inventory inv = this.player.getOnlinePlayer().getInventory();
        
        //Clear
        this.player.getOnlinePlayer().getEquipment().clear();
        this.setToInventory(inv);
        
        try {this.player.getOnlinePlayer().getEquipment().setHelmet(this.helmet.toItemStack());} catch(Exception e) {}
        try {this.player.getOnlinePlayer().getEquipment().setChestplate(this.chestplate.toItemStack());} catch(Exception e) {}
        try {this.player.getOnlinePlayer().getEquipment().setLeggings(this.leggings.toItemStack());} catch(Exception e) {}
        try {this.player.getOnlinePlayer().getEquipment().setBoots(this.boots.toItemStack());} catch(Exception e) {}
        
        this.player.getOnlinePlayer().setExp(this.xp);
        this.player.getOnlinePlayer().setLevel(this.level);
    }
    
    public void setToInventory(Inventory inv) {
        inv.clear();
        for(Integer i : this.slots.keySet()) {
            inv.setItem(i, this.slots.get(i).toItemStack());
        }
    }
}
