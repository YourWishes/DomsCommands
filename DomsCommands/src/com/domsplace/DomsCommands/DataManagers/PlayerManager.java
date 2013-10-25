/*
 * Copyright 2013 Dominic.
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

package com.domsplace.DomsCommands.DataManagers;

import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Enums.ManagerType;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsInventory;
import com.domsplace.DomsCommands.Objects.DomsInventoryItem;
import com.domsplace.DomsCommands.Objects.DomsLocation;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Home;
import com.domsplace.DomsCommands.Objects.Punishment;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class PlayerManager extends DataManager {
    public PlayerManager() {
        super(ManagerType.PLAYER);
    }
    
    @Override
    public void tryLoad() throws IOException {
        if(!this.getPlayersDirectory().exists()) this.getPlayersDirectory().mkdir();
        loadAllPlayers();
    }
    
    @Override
    public void trySave() throws IOException {
        if(!this.getPlayersDirectory().exists()) this.getPlayersDirectory().mkdir();
        for(DomsPlayer plyr : DomsPlayer.getRegisteredPlayers()) {
            savePlayer(plyr);
        }
    }
    
    public void loadAllPlayers() {
        for(File f : this.getPlayersDirectory().listFiles()) {
            DomsPlayer player = loadPlayer(f);
            if(player != null) continue;
        }
    }
    
    public DomsPlayer loadPlayer(File file) {
        try {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            
            DomsPlayer player = DomsPlayer.getPlayer(yml.getString("name"));
            player.setPlayerFile(file);
            
            if(yml.contains("nick")) {
                player.setDisplayName(yml.getString("nick"));
            }
            
            player.setPlayTime(yml.getLong("playtime", 0L));
            player.setJoinTime(yml.getLong("joined", getNow()));
            player.setJoinTime(yml.getLong("login", getNow()));
            player.setJoinTime(yml.getLong("logout", getNow()));
            
            if(yml.contains("punishments")) {
                MemorySection punishments = (MemorySection) yml.get("punishments");
                for(String s : punishments.getKeys(false)) {
                    String key = "punishments." + s + ".";
                    String type = yml.getString(key + "type");
                    PunishmentType t = PunishmentType.getType(type);
                    Punishment p = new Punishment(player, t);
                    
                    String reason = yml.getString(key + "reason");
                    p.setReason(reason);
                    
                    if(yml.contains(key + "banner")) {
                        String banner = yml.getString(key + "banner");
                        p.setBanner(banner);
                    }
                    
                    p.setDate(yml.getLong(key + "date"));
                    p.setEndDate(yml.getLong(key + "end"));
                    
                    if(yml.contains(key + "location")) {
                        p.setLocation(DomsLocation.guessLocation(yml.getString(key + "location")));
                    }
                    
                    player.addPunishment(p);
                }
            }
            
            if(yml.contains("home")) {
                MemorySection homes = (MemorySection) yml.get("home");
                for(String s : homes.getKeys(false)) {
                    String key = "home." + s + ".";
                    Home h = new Home(yml.getString(key + "name"), DomsLocation.guessLocation(yml.getString(key + "location")), player);
                    player.addHome(h);
                }
            }
            
            if(yml.contains("inventories")) {
                MemorySection invs = (MemorySection) yml.get("inventories");
                for(String s : invs.getKeys(false)) {
                    String key = "inventories." + s + ".";
                    DomsInventory inv = new DomsInventory(player, yml.getString(key + "group"));
                    
                    if(yml.contains(key + "xp")) inv.setExp(Float.parseFloat(yml.getString(key + "xp")));
                    
                    if(yml.contains(key + "helmet")) inv.setHelmet(DomsInventoryItem.createFromString(yml.getString(key + "helmet")));
                    if(yml.contains(key + "chestplate")) inv.setChestPlate(DomsInventoryItem.createFromString(yml.getString(key + "chestplate")));
                    if(yml.contains(key + "leggings")) inv.setLeggings(DomsInventoryItem.createFromString(yml.getString(key + "leggings")));
                    if(yml.contains(key + "boots")) inv.setBoots(DomsInventoryItem.createFromString(yml.getString(key + "boots")));
                    
                    if(yml.contains(key + "items")) {
                        MemorySection items = (MemorySection) yml.get(key + "items");
                        for(String i : items.getKeys(false)) {
                            int slot = getInt(i.replaceAll("slot", ""));
                            DomsInventoryItem item = DomsInventoryItem.createFromString(yml.getString(key + "items." + i));
                            inv.setItem(slot, item);
                        }
                    }
                    
                    player.addInventory(inv);
                }
            }
            
            if(yml.contains("endchests")) {
                MemorySection invs = (MemorySection) yml.get("endchests");
                for(String s : invs.getKeys(false)) {
                    String key = "endchests." + s + ".";
                    DomsInventory inv = new DomsInventory(player, yml.getString(key + "group"));
                    
                    if(yml.contains(key + "items")) {
                        MemorySection items = (MemorySection) yml.get(key + "items");
                        for(String i : items.getKeys(false)) {
                            int slot = getInt(i.replaceAll("slot", ""));
                            DomsInventoryItem item = DomsInventoryItem.createFromString(yml.getString(key + "items." + i));
                            inv.setItem(slot, item);
                        }
                    }
                    
                    player.addEndChest(inv);
                }
            }
            
            if(yml.contains("ip")) {
                player.setLastIP(yml.getString("ip", ""));
            }
            
            if(yml.contains("location")) {
                player.setLastLocation(DomsLocation.guessLocation(yml.getString("location")));
            }
            
            return player;
        } catch(Exception e) {
            error("Failed to load Player from File \"" + file.getName() + "\".", e);
            return null;
        }
    }
    
    public void savePlayer(DomsPlayer player) {
        player.updateDomsInventory();
        try {
            File f = player.getPlayerFile();
            if(f == null) player.setPlayerFile(this.getDefaultPlayerFile(player));
            f = player.getPlayerFile();
            
            if(!f.exists()) f.createNewFile();
            
            YamlConfiguration yml = new YamlConfiguration();
            
            yml.set("name", player.getPlayer());
            if(!player.getDisplayName().equals(player.getPlayer())) {
                yml.set("nick", player.getDisplayName());
            }
            
            if(player.getPlayTime() > 0) {
                yml.set("playtime", player.getPlayTime());
            }
            
            if(player.getJoinTime() > 0) {
                yml.set("joined", player.getJoinTime());
            }
            
            if(player.getLoginTime() > 0) {
                yml.set("login", player.getLoginTime());
            }
            
            if(player.getLogoutTime() > 0) {
                yml.set("logout", player.getLogoutTime());
            }
            
            if(player.getLastIP() != null) {
                yml.set("ip", player.getLastIP());
            }
            
            if(player.getLocation() != null) {
                yml.set("location", player.getLocation().toString());
            }
            
            int id = 0;
            for(Punishment p : player.getPunishments()) {
                id++;
                String key = "punishments.p" + id + ".";
                
                yml.set(key + "type", p.getType().getType());
                yml.set(key + "reason", p.getReason());
                if(p.getBanner() != null) {
                    yml.set(key + "banner", p.getBanner());
                }
                
                yml.set(key + "date", p.getDate());
                yml.set(key + "end", p.getEndDate());
                if(p.getLocation() != null) {
                    yml.set(key + "location", p.getLocation());
                }
            }
            
            if(!player.isConsole()) {
                for(Home h : player.getHomes()) {
                    String key = "home." + h.getName() + ".";
                    yml.set(key + "name", h.getName());
                    yml.set(key + "location", h.getLocation().toString());
                }

                for(DomsInventory inv : player.getInventories()) {
                    String key = "inventories." + inv.getInventoryGroup() + ".";
                    yml.set(key + "group", inv.getInventoryGroup());

                    if(inv.getHelmet() != null) {yml.set(key + "helmet", inv.getHelmet().toString());}
                    if(inv.getChestPlate() != null) {yml.set(key + "chestplate", inv.getChestPlate().toString());}
                    if(inv.getLeggings() != null) {yml.set(key + "leggings", inv.getLeggings().toString());}
                    if(inv.getBoots() != null) {yml.set(key + "boots", inv.getBoots().toString());}

                    Map<Integer, DomsInventoryItem> items = inv.getItems();
                    for(Integer i : items.keySet()) {
                        yml.set(key + "items.slot" + i, items.get(i).toString());
                    }

                    if(inv.getExp() > 0) {yml.set(key + "xp", Float.toString(inv.getExp()));}
                }

                for(DomsInventory inv : player.getEnderChests()) {
                    String key = "endchests." + inv.getInventoryGroup() + ".";
                    yml.set(key + "group", inv.getInventoryGroup());

                    Map<Integer, DomsInventoryItem> items = inv.getItems();
                    for(Integer i : items.keySet()) {
                        yml.set(key + "items.slot" + i, items.get(i).toString());
                    }
                }
            }
            
            yml.save(f);
        } catch(Exception e) {
            error("Failed to save " + player.getPlayer() + "'s data.", e);
        }
    }
    
    public File getPlayersDirectory() {return new File(getDataFolder(), "players");}
    public File getDefaultPlayerFile(DomsPlayer player) {return new File(getPlayersDirectory(), player.getPlayer() + ".yml");}
}
