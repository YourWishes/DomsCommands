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
import com.domsplace.DomsCommands.Objects.Kit;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Exceptions.SaveResult;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
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
        for(DomsPlayer plyr : DomsPlayer.getAllRegisteredPlayers()) {
            SaveResult result = savePlayer(plyr);
            if(result.equals(SaveResult.RESULT_SAVED)) continue;
            error("Failed to save " + plyr.getUsername() + ", " + result.getResult());
        }
    }
    
    public void loadAllPlayers() {
        for(File f : this.getPlayersDirectory().listFiles()) {
            DomsPlayer player = loadPlayer(f);
            if(player != null) continue;
        }
    }
    
    public DomsPlayer loadPlayer(File file) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        //if(!yml.contains("name")) return null;
        
        DomsPlayer player = null;
        if(yml.contains("UUID")) {
            player = DomsPlayer.getPlayerByUUID(yml.getString("UUID"), true);
        } else if(yml.contains("name")) {
            player = DomsPlayer.getDomsPlayerFromUsername(yml.getString("name"), true);
        } else {
            return null;
        }
        
        player.setPlayerFile(file);

        //if(player.isOnline()) DomsPlayer.REGISTERED_ONLINE_PLAYERS.put(player.getPlayer(), player);

        //Clear Old Data
        for(Punishment p : player.getPunishments()) {
            player.removePunishment(p);
        }

        for(Home home : player.getHomes()) {
            player.removeHome(home);
        }

        //Set new Data
        if(yml.contains("nick") && !yml.getString("nick").equalsIgnoreCase("off")) {
            player.setDisplayName(yml.getString("nick"));
        }

        player.setPlayTime(yml.getLong("playtime", 0L));
        player.setJoinTime(yml.getLong("joined", getNow()));
        player.setLoginTime(yml.getLong("login", getNow()));
        player.setLogoutTime(yml.getLong("logout", getNow()));

        if(yml.contains("fly")) player.setFlightMode(yml.getBoolean("fly"));

        if(yml.contains("punishments")) {
            MemorySection punishments = (MemorySection) yml.get("punishments");
            for(String s : punishments.getKeys(false)) {
                String key = "punishments." + s + ".";
                String type = yml.getString(key + "type");
                PunishmentType t = PunishmentType.getType(type);
                if(t == null) {
                    log("Error loading punishment \"" + s + "\".");
                    continue;
                }
                Punishment p = new Punishment(player, t);

                String reason = yml.getString(key + "reason");
                p.setReason(reason);

                if(yml.contains(key + "banner")) {
                    String banner = yml.getString(key + "banner");
                    p.setBanner(banner);
                }

                if(yml.contains(key + "pardoned")) p.isPardoned(yml.getBoolean(key + "pardoned", false));

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
                if(yml.contains(key + "xplevel")) inv.setExpLevel(yml.getInt(key + "xplevel"));

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

        if(yml.contains("backpack")) {
            MemorySection invs = (MemorySection) yml.get("backpack");
            DomsInventory bp = new DomsInventory(player, "");
            for(String s : invs.getKeys(false)) {
                int slot = getInt(s.replaceAll("slot", ""));
                DomsInventoryItem item = DomsInventoryItem.createFromString(yml.getString("backpack." + s));
                bp.setItem(slot, item);
            }

            player.setBackpack(bp);
        }

        if(yml.contains("furnace")) player.setFurnaceLocation(DomsLocation.guessLocation(yml.getString("furnace")));

        if(yml.contains("kitcooldowns")) {
            for(String s : ((MemorySection) yml.get("kitcooldowns")).getKeys(false)) {
                try {
                    Kit k = Kit.getKit(s);
                    player.setKitCooldown(k, yml.getLong("kitcooldowns." + s));
                } catch(Exception e) {}
            }
        }

        if(yml.contains("ip")) {
            player.setLastIP(yml.getString("ip", ""));
        }

        if(yml.contains("location")) {
            player.setLastLocation(DomsLocation.guessLocation(yml.getString("location")));
        }
        
        //Store Saved Variables
        if(yml.contains("variables")) {
            try {
                for(String s : ((MemorySection) yml.get("variables")).getKeys(false)) {
                    try {
                        player.setSavedVariable(s, yml.getString("variables." + s));
                    } catch(Exception e) {}
                }
            } catch(Exception e) {}
        }
        
        return player;
    }
    
    public SaveResult savePlayer(DomsPlayer player) {
        player.updateDomsInventory();
        
        File f = player.getPlayerFile();
        
        if(f != null && f.exists()) {
            f = this.getDeprecatedDefaultPlayerFile(player);
            if(f != null && f.exists()) {
                log("Found Pre-1.7.9 Player File " + player.getUsername() + " (UUID: " + player.getUUID().toString() + ")");
                f.delete();
                f = null;
                log("...Done Converting!");
            }
        }
        
        if(f == null) {
            player.setPlayerFile(this.getDefaultPlayerFile(player));
        }
        
        f = player.getPlayerFile();

        try {
            if(!f.exists()) f.createNewFile();
        } catch(IOException e) {
            return SaveResult.FILE_CREATE_FAILED;
        }

        YamlConfiguration yml = new YamlConfiguration();

        yml.set("name", player.getUsername());
        if(player.getStringUUID() != null) yml.set("UUID", player.getStringUUID());
        if(!player.getDisplayName().equals(player.getUsername()) && !player.getDisplayName().equalsIgnoreCase("off")) {
            yml.set("nick", player.getDisplayName());
        }
        
        if(player.getNamePlate() != null && !player.getNamePlate().equals(player.getUsername()) && !player.getNamePlate().equalsIgnoreCase("off")) {
            yml.set("plate", player.getNamePlate());
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

        yml.set("fly", player.isFlightMode());

        int id = 0;
        for(Punishment p : player.getPunishments()) {
            id++;
            String key = "punishments.p" + id + ".";

            yml.set(key + "type", p.getType().getType());
            yml.set(key + "reason", p.getReason());
            if(p.getBanner() != null) {
                yml.set(key + "banner", p.getBanner());
            }

            if(p.isPardoned()) yml.set(key + "pardoned", p.isPardoned());

            yml.set(key + "date", p.getDate());
            yml.set(key + "end", p.getEndDate());
            if(p.getLocation() != null) {
                yml.set(key + "location", p.getLocation().toString());
            }
        }

        if(!player.isConsole()) {
            for(Home h : player.getHomes()) {
                String key = "home." + h.getName() + ".";
                yml.set(key + "name", h.getName());
                yml.set(key + "location", h.getLocation().toString());
            }

            for(DomsInventory inv : player.getInventories()) {
                try {
                    String key = "inventories." + inv.getInventoryGroup() + ".";
                    yml.set(key + "group", inv.getInventoryGroup());

                    if(inv.getHelmet() != null) {yml.set(key + "helmet", inv.getHelmet().toString());}
                    if(inv.getChestPlate() != null) {yml.set(key + "chestplate", inv.getChestPlate().toString());}
                    if(inv.getLeggings() != null) {yml.set(key + "leggings", inv.getLeggings().toString());}
                    if(inv.getBoots() != null) {yml.set(key + "boots", inv.getBoots().toString());}

                    Map<Integer, DomsInventoryItem> items = inv.getItems();
                    for(Integer i : items.keySet()) {
                        try {
                            if(items.get(i) == null || items.get(i).getItem() == null || items.get(i).getItem().isAir()) continue;
                            yml.set(key + "items.slot" + i, items.get(i).toString());
                        } catch(Throwable t) {}
                    }

                    if(inv.getExp() > 0) {yml.set(key + "xp", Float.toString(inv.getExp()));}
                    if(inv.getExpLevel() > 0) {yml.set(key + "xplevel", inv.getExpLevel());}

                } catch(Exception e) {
                    log("Failed to save " + player.getUsername() + "'s Inventory \"" + inv.getInventoryGroup() + "\".");
                }
            }

            for(DomsInventory inv : player.getEnderChests()) {
                try {
                    String key = "endchests." + inv.getInventoryGroup() + ".";
                    yml.set(key + "group", inv.getInventoryGroup());

                    Map<Integer, DomsInventoryItem> items = inv.getItems();
                    for(Integer i : items.keySet()) {
                        try {
                            if(items.get(i) == null || items.get(i).getItem() == null || items.get(i).getItem().isAir()) continue;
                            yml.set(key + "items.slot" + i, items.get(i).toString());
                        } catch(Throwable t) {}
                    }
                } catch(Exception e) {
                    log("Failed to save " + player.getUsername() + "'s Enderchest \"" + inv.getInventoryGroup() + "\".");
                }
            }

            for(Kit k : Kit.getKits()) {
                long l = player.getKitCooldown(k);
                if(l <= 0) continue;
                yml.set("kitcooldowns." + k.getName(), l);
            }

            if(player.getBackpack() != null) {
                try {
                    DomsInventory inv = player.getBackpack();
                    inv.updateFromInventory();
                    Map<Integer, DomsInventoryItem> items = inv.getItems();
                    for(Integer i : items.keySet()) {
                        try {
                            if(items.get(i) == null || items.get(i).getItem() == null || items.get(i).getItem().isAir()) continue;
                            yml.set("backpack.slot" +  i, items.get(i).toString());
                        } catch(Throwable t) {}
                    }
                } catch(Exception e) {
                    log("Failed to save " + player.getUsername() + "'s Backpack \"" + player.getBackpack().getInventoryGroup() + "\".");
                }
            }
            if(player.getFurnaceLocation() != null) {
                yml.set("furnace", player.getFurnaceLocation().toString());
            }
        }
        
        //Save Variables
        Map<String, String> vars = player.getSavedVariables();
        for(String key : vars.keySet()) {
            yml.set("variables." + key, vars.get(key));
        }

        try {
            yml.save(f);
        } catch(IOException e) {
            return SaveResult.YAML_SAVE_FAILED;
        }
        return SaveResult.RESULT_SAVED;
    }
    
    public File getPlayersDirectory() {return new File(getDataFolder(), "players");}
    public File getDefaultPlayerFile(DomsPlayer player) {return new File(getPlayersDirectory(), player.getUUID()+ ".yml");}
    @Deprecated public File getDeprecatedDefaultPlayerFile(DomsPlayer player) {return new File(getPlayersDirectory(), player.getUsername() +  ".yml");}
}
