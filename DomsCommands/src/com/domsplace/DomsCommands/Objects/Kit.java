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
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dominic Masters
 */
public class Kit {
    private static final List<Kit> REGISTERED_KITS = new ArrayList<Kit>();
    
    public static List<Kit> getKits() {return new ArrayList<Kit>(REGISTERED_KITS);}
    
    public static Kit getKit(String name) {
        for(Kit k : REGISTERED_KITS) {
            if(k.getName().equalsIgnoreCase(name)) return k;
        }
        return null;
    }

    public static List<Kit> getKits(CommandSender sender) {
        List<Kit> kits = new ArrayList<Kit>();
        for(Kit k : REGISTERED_KITS) {
            if(!Base.hasPermission(sender, k.getPermission())) continue;
            kits.add(k);
        }
        
        return kits;
    }
    
    private String name;
    private long cooldown = -1;
    private List<DomsItem> items;
    
    public Kit(String name, List<DomsItem> items) {
        this.name = name;
        this.items = items;
        this.register();
    }
    
    public Kit(String name) {this(name, new ArrayList<DomsItem>());}
    
    public String getName() {return this.name;}
    public List<DomsItem> getItems() {return new ArrayList<DomsItem>(items);}
    public long getCooldown() {return this.cooldown;}
    public String getPermission() {return "DomsCommands.kit." + this.getName().toLowerCase();}
    
    public void setCooldown(long cooldown) {this.cooldown = cooldown;}
    
    public final void deRegister() {REGISTERED_KITS.remove(this);}
    public final void register() {REGISTERED_KITS.add(this);}

    public void addItems(List<DomsItem> items) {this.items.addAll(items);}
}
