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

package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.PluginHook;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Exceptions.InvalidItemException;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author      Dominic
 * @since       07/10/2013
 */
public class DomsPlayer {
    private static final Map<String, DomsPlayer> REGISTERED_PLAYERS = new HashMap<String, DomsPlayer>();
    
    public static final DomsPlayer CONSOLE_PLAYER = new DomsPlayer("CONSOLE");
    
    public static final String NICKNAME_REGEX = "^[a-zA-Z0-9!@#^*&(),\\_\\-\\s]*$";
    
    //Static
    public static DomsPlayer guessPlayer(CommandSender sender, String guess) {return guessPlayer(sender, guess, false);}
    public static DomsPlayer guessPlayer(CommandSender sender, String guess, boolean createIfNotExists) {
        Player tryPlayer = Base.getPlayer(sender, guess);
        if(tryPlayer == null) {
            return guessPlayer(guess, createIfNotExists);
        } else {
            return getPlayer(tryPlayer);
        }
    }
    
    public static DomsPlayer guessPlayer(String guess) {return guessPlayer(guess, false);}
    public static DomsPlayer guessPlayer(String guess, boolean createIfNotExists) {
        DomsPlayer p = null;
        for(DomsPlayer plyr : REGISTERED_PLAYERS.values()) {
            if(!plyr.getPlayer().toLowerCase().contains(guess.toLowerCase())) continue;
            p = plyr;
            break;
        }
        
        if(p == null) {
            for(DomsPlayer plyr : REGISTERED_PLAYERS.values()) {
                if(!plyr.getDisplayName().toLowerCase().contains(guess.toLowerCase())) continue;
                p = plyr;
                break;
            }
        }
        
        if(createIfNotExists && p == null) {return getPlayer(guess);}
        return p;
    }
    
    public static List<DomsPlayer> getOnlinePlayers() {
        List<DomsPlayer> list = new ArrayList<DomsPlayer>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            list.add(DomsPlayer.getPlayer(p));
        }
        return list;
    }

    public static List<DomsPlayer> getOnlinePlayers(CommandSender sender) {
        List<DomsPlayer> list = new ArrayList<DomsPlayer>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            DomsPlayer player = DomsPlayer.getPlayer(p);
            if(player.isConsole()) continue;
            if(!player.isOnline()) continue;
            if(!player.isOnline(sender)) continue;
            list.add(player);
        }
        return list;
    }

    public static DomsPlayer guessExactPlayer(CommandSender sender, String guess, boolean createIfNotExists) {
        DomsPlayer p = null;
        for(DomsPlayer plyr : REGISTERED_PLAYERS.values()) {
            if(!plyr.getPlayer().equalsIgnoreCase(guess.toLowerCase())) continue;
            p = plyr;
            break;
        }
        
        if(p == null) {
            for(DomsPlayer plyr : REGISTERED_PLAYERS.values()) {
                if(!plyr.getDisplayName().equalsIgnoreCase(guess.toLowerCase())) continue;
                p = plyr;
                break;
            }
        }
        
        if(createIfNotExists && p == null) {return getPlayer(guess);}
        return p;
    }
    
    public static List<DomsPlayer> getVisibleOnlinePlayers() {
        List<DomsPlayer> list = new ArrayList<DomsPlayer>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            DomsPlayer pl = DomsPlayer.getPlayer(p);
            if(!pl.isVisible()) continue;
            list.add(pl);
        }
        return list;
    }
    
    public static List<DomsPlayer> getRegisteredPlayers() {return new ArrayList<DomsPlayer>(REGISTERED_PLAYERS.values());}
    
    public static DomsPlayer getPlayer(CommandSender p) {return getPlayer(p.getName());}
    public static DomsPlayer getPlayer(Player p) {return getPlayer(p.getName());}
    public static DomsPlayer getPlayer(OfflinePlayer player) {return getPlayer(player.getName());}
    public static DomsPlayer getPlayer(String player) {
        if(isPlayerRegistered(player)) return REGISTERED_PLAYERS.get(player);
        return new DomsPlayer(player);
    }
    public static DomsPlayer getPlayerByIP(String string) {
        for(DomsPlayer player : REGISTERED_PLAYERS.values()) {
            if(player == null) continue;
            if(player.getLastIP() == null) continue;
            if(player.getLastIP().equalsIgnoreCase(string)) return player;
        }
        
        return null;
    }
    
    public static boolean isPlayerRegistered(Player player) {return isPlayerRegistered(player.getName());}
    public static boolean isPlayerRegistered(String player) {return REGISTERED_PLAYERS.containsKey(player);}

    public static List<DomsPlayer> getPlayersByIP(String lastIP) {
        List<DomsPlayer> players = new ArrayList<DomsPlayer>();
        for(DomsPlayer p : REGISTERED_PLAYERS.values()) {
            if(p == null) continue;
            if(p.getLastIP() == null) continue;
            if(p.getLastIP().equals(lastIP)) players.add(p);
        }
        return players;
    }
    
    //Instance
    private final String player;
    private String displayName;
    private File playerFile;
    
    private String lastIP;
    
    private long afkTime;
    private boolean afk;
    
    private long join;
    private long login;
    private long logout;
    private long playtime;
    
    private DomsLocation backLocation;
    
    private DomsLocation lastLocation;
    private long lastMoveTime;
    
    private final List<Punishment> punishments;
    private final List<Home> homes;
    private final List<DomsInventory> inventories;
    private final List<DomsInventory> enderchest;
    private Inventory backpack;
    private Map<String, String> variables;
    private Map<Kit, Long> kitCooldowns;
    
    private boolean flyMode;
    
    private TeleportRequest lastRequest;
    
    private DomsPlayer lastPrivateMessenger;
    
    private DomsPlayer(String player) {
        this.player = player;
        if(this.isConsole()) this.displayName = "&dServer";
        this.displayName = this.getDisplayName();
        this.punishments = new ArrayList<Punishment>();
        this.homes = new ArrayList<Home>();
        this.inventories = new ArrayList<DomsInventory>();
        this.enderchest = new ArrayList<DomsInventory>();
        this.afk = false;
        this.afkTime = Base.getNow();
        this.lastMoveTime = Base.getNow();
        this.variables = new HashMap<String, String>();
        this.kitCooldowns = new HashMap<Kit, Long>();
        
        this.registerPlayer();
        Base.debug("Regitered new DomsPlayer for " + this.player);
    }
    
    private void registerPlayer() {REGISTERED_PLAYERS.put(player, this);}
    
    public String getPlayer() {return this.player;}
    public OfflinePlayer getOfflinePlayer() {return Bukkit.getOfflinePlayer(player);}
    public Player getOnlinePlayer() {return this.getOfflinePlayer().getPlayer();}
    public long getJoinTime() {return this.join;}
    public long getLoginTime() {return this.login;}
    public long getLogoutTime() {return this.logout;}
    public long getPlayTime() {return this.playtime;}
    public long getAFKTime() {return this.afkTime;}
    public String getLastIP() {return this.lastIP;}
    public long getLastMoveTime() {return this.lastMoveTime;}
    public TeleportRequest getLastTeleporRequest() {return this.lastRequest;}
    public DomsLocation getBackLocation() {return this.backLocation;}
    public DomsLocation getLastLocation() {return this.lastLocation;}
    public List<Punishment> getPunishments() {return new ArrayList<Punishment>(this.punishments);}
    public List<Home> getHomes() {return new ArrayList<Home>(this.homes);}
    public List<DomsInventory> getInventories() {return new ArrayList<DomsInventory>(this.inventories);}
    public List<DomsInventory> getEnderChests() {return new ArrayList<DomsInventory>(this.enderchest);}
    public DomsPlayer getLastMessenger() {return this.lastPrivateMessenger;}
    public File getPlayerFile() {return this.playerFile;}
    public DomsInventory getInventory() {return this.getInventoryFromWorld(this.getWorld());}
    public DomsInventory getEnderChest() {return this.getEndChestFromWorld(this.getWorld());}
    public String getWorld() {if(this.getLocation() == null) return null; return this.getLocation().getWorld();}
    public DomsChannel getChannel() {return DomsChannel.getPlayersChannel(this);}
    public Map<String, String> getVariables() {this.updateVariables(); return new HashMap<String, String>(this.variables);}
    public Map<Kit, Long> getKitCooldowns() {return new HashMap<Kit, Long>(this.kitCooldowns);}
    public String getVariable(String key) {this.updateVariables(); return this.variables.get(key);}
    public long getKitCooldown(Kit k) {try {return this.kitCooldowns.get(k);}catch(Exception e) {return -1;}}
    public boolean getFlightMode() {return this.flyMode;}
    public Inventory getBackpack() {return this.backpack;}
    
    public boolean isOnline() {if(this.isConsole()) return true; return this.getOfflinePlayer().isOnline();}
    public boolean isVisible() {if(this.isConsole()) return true; return Base.isVisible(this.getOfflinePlayer());}
    public boolean isAFK() {return this.afk;}
    public boolean isConsole() {return this.equals(CONSOLE_PLAYER);}
    
    public void setJoinTime(long time) {this.join = time;}
    public void setLoginTime(long time) {this.login = time;}
    public void setLogoutTime(long time) {this.logout = time;}
    public void setPlayTime(long time) {this.playtime = time;}
    public void setLastIP(String ip) {this.lastIP = ip;}
    public void setLastLocation(DomsLocation location) {this.lastLocation = location;}
    public void setLastMoveTime(long time) {this.lastMoveTime = time;}
    public void setLastTeleportRequest(TeleportRequest request) {this.lastRequest = request;}
    public void setBackLocation(DomsLocation location) {this.backLocation = location;}
    public void setAFK(boolean i) {this.afk = i;}
    public void setAFKTime(long now) {this.afkTime = now;}
    public void setLastMessenger(DomsPlayer player) {this.lastPrivateMessenger = player;}
    public void setPlayerFile(File file) {this.playerFile = file;}
    public void setVariable(String key, String variable) {this.variables.put(key, variable); this.updateVariables();}
    public void setKitCooldown(Kit k, long l) {this.kitCooldowns.put(k, l);}
    public void setFlightMode(boolean f) {this.flyMode = f;}
    public Inventory setBackpack(Inventory inventory) {this.backpack = inventory; return this.backpack;}
    
    @Override public String toString() {return this.getDisplayName();}
    
    public void addInventory(DomsInventory inv) {this.inventories.add(inv);}
    public void addEndChest(DomsInventory inv) {this.enderchest.add(inv);}
    public void addPlayTime(long time) {this.playtime += time;}
    public void addPunishment(Punishment p) {this.punishments.add(p);}
    public void addHome(Home h) {this.homes.add(h);}
    
    public void removePlayTime(long time) {this.playtime -= time;}
    public void removePunishment(Punishment p) {this.punishments.remove(p);}
    public void removeHome(Home h) {this.homes.remove(h);}
    
    public void toggleAFK() {this.afk = !this.afk;}
    
    public boolean hasPermisson(String perm) {return Base.hasPermission(this.getOfflinePlayer(), perm);}
    public boolean hasPlayedBefore() {return this.getOfflinePlayer().hasPlayedBefore();}
    
    public boolean canSee(OfflinePlayer t) {return Base.canSee(this.getOfflinePlayer(), t);}
    public boolean canBeSeenBy(OfflinePlayer t) {return Base.canSee(t, this.getOfflinePlayer());}
    
    public void teleport(DomsLocation to) {this.teleport(to, Base.getConfig().getBoolean("teleport.safe", true));}
    public void sendMessage(Object o) {Base.sendMessage(this, o);}
    
    //Complex get's
    public final String getDisplayName() {
        this.updateVariables();
        if(this.isConsole() && this.displayName == null) {
            this.displayName = "Server";
            return this.displayName;
        }
        if(this.isConsole()) {
            return this.displayName;
        }
        if(!this.isOnline()) {
            if(this.displayName == null) this.displayName = this.getPlayer();
            return this.displayName;
        }
        if(!Base.isVisible(this.getOnlinePlayer())) return this.displayName;
        this.displayName = this.getOnlinePlayer().getDisplayName();
        return this.displayName;
    }
    
    public DomsLocation getLocation() {
        if(this.isConsole()) return null;
        if(this.isOnline()) return new DomsLocation(this.getOnlinePlayer().getLocation());
        return this.lastLocation;
    }

    public List<Punishment> getPunishmentsOfType(PunishmentType type) {
        List<Punishment> puns = new ArrayList<Punishment>();
        for(Punishment p : this.punishments) {
            if(!p.getType().equals(type)) continue;
            puns.add(p);
        }
        return puns;
    }

    public CommandSender getCommandSender() {
        if(this.isConsole()) return Bukkit.getConsoleSender();
        return this.getOnlinePlayer();
    }
    
    public Home getHome(String s) {
        for(Home h : this.homes) {
            if(h.getName().equalsIgnoreCase(s)) return h;
        }
        return null;
    }

    public Punishment getMostRecentPunishmentOfType(PunishmentType type) {
        Punishment recent = null;
        for(Punishment p : this.getPunishmentsOfType(type)) {
            if(recent == null) {
                recent = p;
                continue;
            }
            
            if(p.getDate() <= recent.getDate()) continue;
            recent = p;
        }
        return recent;
    }

    public String getLastPunishmentReason(PunishmentType BAN) {
        Punishment p = this.getMostRecentPunishmentOfType(BAN);
        if(p != null) return p.getReason();
        return Punishment.DEFAULT_REASON;
    }
    
    public DomsInventory getInventoryFromGroup(String g) {
        for(DomsInventory inv : this.inventories) {
            if(inv.getInventoryGroup().equals(g)) return inv;
        }
        return null;
    }
    
    public DomsInventory getInventoryFromWorld(String w) {return this.getInventoryFromGroup(DomsInventory.getInventoryGroupFromWorld(w));}
    
    public DomsInventory getEndChestFromGroup(String g) {
        for(DomsInventory inv : this.enderchest) {
            if(inv.getInventoryGroup().equals(g)) return inv;
        }
        return null;
    }
    
    public DomsInventory getEndChestFromWorld(String w) {return this.getEndChestFromGroup(DomsInventory.getInventoryGroupFromWorld(w));}
    
    public String getAbsoluteGroup() {
        if(this.getWorld() == null) return null;
        if(PluginHook.VAULT_HOOK.isHooked() && PluginHook.VAULT_HOOK.getPermission() != null) {
            if(this.getWorld() == null) return null;
            return PluginHook.VAULT_HOOK.getPermission().getPrimaryGroup(this.getWorld(), this.player);
        }
        return null;
    }
    
    public String getGroup() {
        if(this.getAbsoluteGroup() != null) return this.getAbsoluteGroup();
        if(this.isConsole()) return "CONSOLE";
        return this.getOfflinePlayer().isOp() ? "OP" : "NOT_OP";
    }
    
    public String getChatPrefix() {
        if(this.getWorld() == null) return "";
        if(PluginHook.VAULT_HOOK.isHooked() && PluginHook.VAULT_HOOK.getChat() != null) {
            String playerPrefix =  PluginHook.VAULT_HOOK.getChat().getPlayerPrefix(this.getWorld(), this.player);
            if(playerPrefix != null && !playerPrefix.equals("")) return playerPrefix;
            String groupPrefix = PluginHook.VAULT_HOOK.getChat().getGroupPrefix(this.getWorld(), this.getAbsoluteGroup());
            if(groupPrefix != null) return groupPrefix;
        }
        return "";
    }
    
    public String getChatSuffix() {
        if(this.getWorld() == null) return "";
        if(PluginHook.VAULT_HOOK.isHooked() && PluginHook.VAULT_HOOK.getChat() != null) {
            String playerSuffix =  PluginHook.VAULT_HOOK.getChat().getPlayerSuffix(this.getWorld(), this.player);
            if(playerSuffix != null && !playerSuffix.equals("")) return playerSuffix;
            String groupSuffix = PluginHook.VAULT_HOOK.getChat().getGroupSuffix(this.getWorld(), this.getAbsoluteGroup());
            if(groupSuffix != null) return groupSuffix;
        }
        return "";
    }
    
    public Block getTargetBlock() {return this.getTargetBlock(99);}
    
    public Block getTargetBlock(int distance) {
        if(this.isConsole() || !this.isOnline()) return null;
        List<Block> blocks = this.getOnlinePlayer().getLineOfSight(null, distance);
        if(blocks == null) return null;
        if(blocks.size() < 1) return null;
        return blocks.get(blocks.size()-1);
    }
    
    //Complex set's
    public void setDisplayName(String newName) {
        this.displayName = newName;
        if(this.isOnline() && !this.isConsole() && newName != null) this.getOnlinePlayer().setDisplayName(newName);
    }
    
    //Complex is's
    public boolean isOnline(CommandSender sender) {
        if(!isOnline()) return false;
        if(this.isConsole()) return true;
        return Base.canSee(sender, this.getOnlinePlayer());
    }
    
    public boolean isBanned() {
        if(this.getOfflinePlayer().isBanned()) return true;
        for(Punishment p : this.getPunishmentsOfType(PunishmentType.BAN)) {
            if(!p.isActive()) continue;
            return true;
        }
        return false;
    }

    public boolean isMuted() {
        for(Punishment p : this.getPunishmentsOfType(PunishmentType.MUTE)) {
            if(!p.isActive()) continue;
            return true;
        }
        return false;
    }
    
    public boolean isFlightMode() {
        if(this.isOnline() && !this.isConsole()) {
            this.flyMode = this.getOnlinePlayer().getAllowFlight();
        }
        return this.flyMode;
    }
    
    //Complex To's
    
    //Complex Functions
    public void teleport(DomsLocation to, boolean useSafe) {
        if(!useSafe) this.getOnlinePlayer().teleport(to.toLocation());
        else this.getOnlinePlayer().teleport(to.getSafeLocation().toLocation(), TeleportCause.COMMAND);
    }
    
    public void kickPlayer(String r) {
        if(this.isConsole()) return;
        if(!this.isOnline()) return;
        this.getOnlinePlayer().kickPlayer(r);
    }

    public void toggleGameMode() {
        if(this.getOnlinePlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            this.getOnlinePlayer().setGameMode(GameMode.CREATIVE);
        } else {
            this.getOnlinePlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    public boolean compare(CommandSender s) {
        return s.getName().equalsIgnoreCase(this.player);
    }
    
    public void addItems(List<DomsItem> items) {
        if(this.isConsole() || !this.isOnline()) return;
        try {
            List<ItemStack> itemStacks = DomsItem.toItemStackArray(items);
            for(ItemStack is : itemStacks) {
                if(is == null) continue;
                if(is.getType() == null) continue;
                if(is.getType().equals(Material.AIR)) continue;
                if(DomsItem.isInventoryFull(this.getOnlinePlayer().getInventory())) {
                    this.getOnlinePlayer().getWorld().dropItemNaturally(this.getLocation().toLocation(), is);
                } else {
                    this.getOnlinePlayer().getInventory().addItem(is);
                }
            }
        } catch(InvalidItemException e) {
        }
    }
    
    public final void updateDomsInventory() {
        if(!this.isOnline()) return;
        if(this.isConsole()) return;
        
        DomsInventory old = this.getInventoryFromWorld(this.getLocation().getWorld());
        if(old != null) this.inventories.remove(old);
        DomsInventory inv = DomsInventory.createFromPlayer(this);
        this.inventories.add(inv);
        old = this.getEndChestFromWorld(this.getLastLocation().getWorld());
        if(old != null) this.enderchest.remove(old);
        inv = DomsInventory.createEndChestFromPlayer(this);
        this.enderchest.add(inv);
    }
    
    private void updateVariables() {
        if(this.player == null || this.displayName == null) return;
        this.variables.put("NAME", this.player);
        this.variables.put("DISPLAYNAME", this.displayName);
        
        if(this.afk) {
            this.variables.put("AWAY", "Away");
        } else {
            this.variables.put("AWAY", "");
        }
        
        if(!this.isConsole()) {
            if(this.getWorld() != null) this.variables.put("WORLD", this.getWorld());
            
            if(this.getChatPrefix() != null) this.variables.put("PREFIX", this.getChatPrefix());
            if(this.getChatSuffix() != null) this.variables.put("SUFFIX", this.getChatSuffix());
            if(this.getGroup() != null) this.variables.put("GROUP", this.getGroup());
            
            if(PluginHook.VILLAGES_HOOK.isHooked()) {
                String s = com.domsplace.Villages.Bases.Base.Wilderness;
                Village v = Village.getPlayersVillage(Resident.getResident(this.getOfflinePlayer()));
                if(v != null) s = v.getName();
                
                this.variables.put("VILLAGE", s);
            }
        }
        
        if(this.isOnline()) {
            
        }
        
        if(this.isOnline() && !this.isConsole()) {
            this.variables.put("GAMEMODE", this.getOnlinePlayer().getGameMode().name());
        }
    }
}
