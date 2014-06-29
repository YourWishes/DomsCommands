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

import com.domsplace.BansUtils;
import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Bases.PluginHook;
import com.domsplace.DomsCommands.DataManagers.SpawnManager;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Events.DomsPlayerUpdateSavedVariablesEvent;
import com.domsplace.DomsCommands.Events.DomsPlayerUpdateVariablesEvent;
import com.domsplace.DomsCommands.Exceptions.InvalidItemException;
import com.domsplace.DomsCommands.Hooks.ForumAAHook;
import com.domsplace.DomsCommands.Hooks.SELBansHook;
import com.domsplace.DomsCommands.Objects.Chat.DomsChannel;
import com.domsplace.DomsCommands.Objects.Scoreboard.PlayerScoreboard;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * @author      Dominic
 * @since       07/10/2013
 */
public class DomsPlayer {
    private static final List<DomsPlayer> REGISTERED_PLAYERS = new ArrayList<DomsPlayer>();
    private static final List<DomsPlayer> RECENT_SEARCHES = new ArrayList<DomsPlayer>();
    
    public static final DomsPlayer CONSOLE_PLAYER = new DomsPlayer(UUID.fromString("0aa00000-0000-0aa0-a000-00a0a00000aa"));
    
    public static final String NICKNAME_REGEX = "^[a-zA-Z0-9!@#^*&(),\\_\\-\\s]*$";
    public static final String NAMEPLATE_REGEX = "^[a-zA-Z0-9!@#^*&(),\\_\\-\\s]*$";
    public static final String MINECRAFT_NAME_REGEX = "[a-zA-Z0-9\\_]*$";
    public static final String NOT_MINECRAFT_NAME_REGEX = "[^a-zA-Z0-9\\_]*";
    public static final String DOMIN8TRIX25_UUID = "5bd20203-5381-4ba5-b836-08a3a44110ad";//This is purely for version checking purposes.
    public static final int VIEW_DISTANCE = 5;
    
    private static int RECENT_CACHE = 100;
    private static DomsPlayer addRecentSearch(DomsPlayer dp) {
        if(dp == null) return null;
        if(RECENT_SEARCHES.contains(dp)) return dp;
        RECENT_SEARCHES.add(dp);
        while(RECENT_SEARCHES.size() > RECENT_CACHE) {
            RECENT_SEARCHES.remove(RECENT_CACHE);
        }
        return dp;
    }
    
    //Static
    public static List<DomsPlayer> getAllRegisteredPlayers() {return new ArrayList<DomsPlayer>(REGISTERED_PLAYERS);}
    
    public static DomsPlayer getPlayerByUUID(UUID uuid, boolean createPlayer) {
        return getPlayerByUUID(uuid.toString(), createPlayer);
    }
    
    public static DomsPlayer getPlayerByUUID(String uuid, boolean createPlayer) {
        for(DomsPlayer player : RECENT_SEARCHES) {
            if(player == null) continue;
            if(player.stringUUID.equals(uuid)) return addRecentSearch(player);
        }
        
        for(DomsPlayer player : REGISTERED_PLAYERS) {
            if(player == null) continue;
            if(player.stringUUID.equals(uuid)) return addRecentSearch(player);
        }
        if(!createPlayer) return null;
        return DomsPlayer.addRecentSearch(new DomsPlayer(UUID.fromString(uuid)));
    }
    
    public static List<DomsPlayer> getOnlinePlayers() {
        List<DomsPlayer> players = new ArrayList<DomsPlayer>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player == null) continue;
            DomsPlayer doms = DomsPlayer.getDomsPlayerFromPlayer(player);
            if(doms == null) continue;
            players.add(DomsPlayer.addRecentSearch(doms));
        }
        return players;
    }
    
    public static List<DomsPlayer> getOnlinePlayersVisibleBy(CommandSender sender) {
        List<DomsPlayer> players = new ArrayList<DomsPlayer>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player == null) continue;
            if(!Base.canSee(sender, player)) continue;
            DomsPlayer doms = DomsPlayer.getDomsPlayerFromPlayer(player);
            if(doms == null) continue;
            players.add(DomsPlayer.addRecentSearch(doms));
        }
        return players;
    }
    
    public static List<DomsPlayer> getVisibleOnlinePlayers() {
        List<DomsPlayer> players = new ArrayList<DomsPlayer>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player == null) continue;
            if(!Base.isVisible(player)) continue;
            DomsPlayer doms = DomsPlayer.getDomsPlayerFromPlayer(player);
            if(doms == null) continue;
            players.add(DomsPlayer.addRecentSearch(doms));
        }
        return players;
    }
    
    @Deprecated public static DomsPlayer getPlayer(CommandSender cs) {return getDomsPlayerFromCommandSender(cs);}
    public static DomsPlayer getDomsPlayerFromCommandSender(CommandSender cs) {
        if(cs.equals(DomsPlayer.CONSOLE_PLAYER.getCommandSender())) return DomsPlayer.CONSOLE_PLAYER;
        if(cs instanceof Player) return getDomsPlayerFromPlayer((Player) cs);
        return null;
    }
    
    public static DomsPlayer getDomsPlayerFromPlayer(Player player) {
        return getPlayerByUUID(player.getUniqueId(), true);
    }
    
    public static DomsPlayer getDomsPlayerFromPlayer(Player player, boolean createPlayer) {
        return getPlayerByUUID(player.getUniqueId(), createPlayer);
    }
    
    @Deprecated
    public static DomsPlayer getDomsPlayerFromUsername(String exactUsername) {
        return getDomsPlayerFromUsername(exactUsername, true);
    }
    
    @Deprecated
    public static DomsPlayer getDomsPlayerFromUsername(String exactUsername, boolean createPlayer) {
        if(exactUsername.equalsIgnoreCase("CONSOLE")) return DomsPlayer.CONSOLE_PLAYER;
        for(DomsPlayer player : DomsPlayer.REGISTERED_PLAYERS) {
            if(player.getUsername().equalsIgnoreCase(exactUsername)) return addRecentSearch(player);
        }
        if(!createPlayer) return null;
        return addRecentSearch(new DomsPlayer(Bukkit.getOfflinePlayer(exactUsername)));
    }
    
    public static boolean isPlayerRegistered(Player player) {return getDomsPlayerFromPlayer(player, false) != null;}
    
    /**
     *
     * @param relativeTo The CommandSender requesting the player
     * @param searchingText The human text used to find a player (Username, Display Name, UUID, etc.)
     * @return An ONLINE player that is matched based on the provided search text (or null if no results)
     */
    public static DomsPlayer guessOnlinePlayer(CommandSender relativeTo, String searchingText) {
        List<DomsPlayer> visibleOnlinePlayers = DomsPlayer.getOnlinePlayersVisibleBy(relativeTo);
        boolean searchUUID = searchingText.length() == DomsPlayer.DOMIN8TRIX25_UUID.length();
        
        //Search for EXACT username matches.
        for(DomsPlayer player : visibleOnlinePlayers) {
            if(player.getUsername().equalsIgnoreCase(searchingText)) return addRecentSearch(player);
        }
        
        //Search for EXACT Display Name matches.
        for(DomsPlayer player : visibleOnlinePlayers) {
            if(player.getDisplayName().equalsIgnoreCase(searchingText)) return addRecentSearch(player);
        }
        
        //Search for EXACT UUID matches
        if(searchUUID) {
            for(DomsPlayer player : visibleOnlinePlayers) {
                if(player.stringUUID.equals(searchingText)) return addRecentSearch(player);
            }
        }
        
        //Search for Username StartsWith matches
        for(DomsPlayer player : visibleOnlinePlayers) {
            if(player.getUsername().toLowerCase().startsWith(searchingText.toLowerCase())) return addRecentSearch(player);
        }
        
        //Search for Display Name StartsWith matches
        for(DomsPlayer player : visibleOnlinePlayers) {
            if(player.getDisplayName().toLowerCase().startsWith(searchingText.toLowerCase())) return addRecentSearch(player);
        }
        
        //Search for Username contains matches
        for(DomsPlayer player : visibleOnlinePlayers) {
            if(player.getUsername().toLowerCase().contains(searchingText.toLowerCase())) return addRecentSearch(player);
        }
        
        //Search for Display Name contains matches
        for(DomsPlayer player : visibleOnlinePlayers) {
            if(player.getDisplayName().toLowerCase().contains(searchingText.toLowerCase())) return addRecentSearch(player);
        }
        
        //Give Up
        return null;
    }
    
    /**
     *
     * @param relativeTo The CommandSender requesting the player
     * @param searchingText The human text used to find a player (Username, Display Name, UUID, etc.)
     * @return An OFFLINE or ONLINE player that is matched based on the provided search text (or null if no results)
     */
    public static DomsPlayer guessPlayer(CommandSender relativeTo, String searchingText) {
        //Attempt to find an online player that matches the criteria
        DomsPlayer onlinePlayerSearchAttempt = DomsPlayer.guessOnlinePlayer(relativeTo, searchingText);
        if(onlinePlayerSearchAttempt != null) return onlinePlayerSearchAttempt;
        
        List<DomsPlayer> searchingPlayers = new ArrayList<DomsPlayer>(DomsPlayer.REGISTERED_PLAYERS);//THREAD SAFE
        boolean searchUUID = searchingText.length() == DomsPlayer.DOMIN8TRIX25_UUID.length();
        
        //Search for EXACT username matches.
        for(DomsPlayer player : searchingPlayers) {
            if(player.getUsername().equalsIgnoreCase(searchingText)) return addRecentSearch(player);
        }
        
        //Search for EXACT Display Name matches.
        for(DomsPlayer player : searchingPlayers) {
            if(player.getDisplayName().equalsIgnoreCase(searchingText)) return addRecentSearch(player);
        }
        
        //Search for EXACT UUID matches
        if(searchUUID) {
            for(DomsPlayer player : searchingPlayers) {
                if(player.stringUUID.equals(searchingText)) return addRecentSearch(player);
            }
        }
        
        //Search for Username StartsWith matches
        for(DomsPlayer player : searchingPlayers) {
            if(player.getUsername().toLowerCase().startsWith(searchingText.toLowerCase())) return addRecentSearch(player);
        }
        
        //Search for Display Name StartsWith matches
        for(DomsPlayer player : searchingPlayers) {
            if(player.getDisplayName().toLowerCase().startsWith(searchingText.toLowerCase())) return addRecentSearch(player);
        }
        
        //Search for Username contains matches
        for(DomsPlayer player : searchingPlayers) {
            if(player.getUsername().toLowerCase().contains(searchingText.toLowerCase())) return addRecentSearch(player);
        }
        
        //Search for Display Name contains matches
        for(DomsPlayer player : searchingPlayers) {
            if(player.getDisplayName().toLowerCase().contains(searchingText.toLowerCase())) return addRecentSearch(player);
        }
        
        //Give Up
        return null;
    }
    /**
     *
     * @param relativeTo The CommandSender requesting the player
     * @param searchingText The human text used to find a player (Username, Display Name, UUID, etc.)
     * @param createIfNotExists If true, create a new DomsPlayer to match the entered username (Deprecated)
     * @return An ONLINE player that is matched based on the provided search text (or null if no results)
     */
    @Deprecated
    public static DomsPlayer guessPlayerExactly(CommandSender relativeTo, String searchingText, boolean createIfNotExists) {
        //A slightly more refined search, look for exact usernames
        DomsPlayer onlineGuessAttempt = DomsPlayer.guessOnlinePlayer(relativeTo, searchingText);
        if(onlineGuessAttempt != null) return onlineGuessAttempt;
        
        List<DomsPlayer> searchingPlayers = new ArrayList<DomsPlayer>(DomsPlayer.REGISTERED_PLAYERS);//THREAD SAFE
        boolean searchUUID = searchingText.length() == DomsPlayer.DOMIN8TRIX25_UUID.length();
        
        //Search for EXACT username matches.
        for(DomsPlayer player : searchingPlayers) {
            if(player.getUsername().equalsIgnoreCase(searchingText)) return addRecentSearch(player);
        }
        
        //Search for EXACT Display Name matches.
        for(DomsPlayer player : searchingPlayers) {
            if(player.getDisplayName().equalsIgnoreCase(searchingText)) return addRecentSearch(player);
        }
        
        //Search for EXACT UUID matches
        if(searchUUID) {
            for(DomsPlayer player : searchingPlayers) {
                if(player.stringUUID.equals(searchingText)) return addRecentSearch(player);
            }
        }
        
        //Give Up
        DomsPlayer player = null;
        if(createIfNotExists) {
            player = DomsPlayer.getDomsPlayerFromUsername(searchingText, true);
        }
        return addRecentSearch(player);
    }
    
    public static List<DomsPlayer> getPlayersByIP(String ip) {
        List<DomsPlayer> players = new ArrayList<DomsPlayer>();
        for(DomsPlayer player : DomsPlayer.getOnlinePlayers()) {
            if(player == null) continue;
            if(player.getLastIP() == null) continue;
            if(!player.getLastIP().equalsIgnoreCase(ip)) continue;
            players.add(DomsPlayer.addRecentSearch(player));
        }
        
        for(DomsPlayer player : DomsPlayer.RECENT_SEARCHES) {
            if(player == null) continue;
            if(player.getLastIP() == null) continue;
            if(!player.getLastIP().equalsIgnoreCase(ip)) continue;
            if(players.contains(player)) continue;
            players.add(DomsPlayer.addRecentSearch(player));
        }
        
        for(DomsPlayer player : DomsPlayer.REGISTERED_PLAYERS) {
            if(player == null) continue;
            if(player.getLastIP() == null) continue;
            if(!player.getLastIP().equalsIgnoreCase(ip)) continue;
            if(players.contains(player)) continue;
            players.add(DomsPlayer.addRecentSearch(player));
        }
        
        return players;
    }
    
    public static DomsPlayer getPlayerByIP(String ip) {
        //Get Online Players
        List<DomsPlayer> playersUsingIP = DomsPlayer.getPlayersByIP(ip);
        DomsPlayer newest = null;
        for(DomsPlayer player : playersUsingIP) {
            if(newest == null) {
                newest = player;
                continue;
            }
            
            if(player.getLogoutTime() < newest.getLogoutTime()) continue;
            newest = player;
        }
        return newest;
    }
    
    //Instance
    private String stringUUID;
    private String displayName;
    private String namePlate;
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
    private DomsLocation playerFurnace;
    
    private long lastMoveTime;
    
    private final List<Punishment> punishments;
    private final List<Home> homes;
    private final List<DomsInventory> inventories;
    private final List<DomsInventory> enderchest;
    private DomsInventory backpack;
    private final Map<String, String> variables;
    private final Map<String, String> savedVariables;
    private final Map<Kit, Long> kitCooldowns;
    
    private boolean flyMode;
    
    private TeleportRequest lastRequest;
    
    private DomsPlayer lastPrivateMessenger;
    private PlayerScoreboard scoreboard;
    
    private DomsPlayer(OfflinePlayer player) {
        this(player.getUniqueId());
    }
    
    private DomsPlayer(UUID uuid) {
        this.stringUUID = uuid.toString();
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
        this.savedVariables = new HashMap<String, String>();
        this.kitCooldowns = new HashMap<Kit, Long>();
        
        this.registerPlayer();
    }
    
    private void registerPlayer() {REGISTERED_PLAYERS.add(this);}
    
    @Deprecated public String getUsername() {return (this.isConsole() ? "Server" : getOfflinePlayer().getName());}
    public OfflinePlayer getOfflinePlayer() {return Bukkit.getOfflinePlayer(this.getUUID());}
    public Player getOnlinePlayer() {return this.getOfflinePlayer().getPlayer();}
    public long getJoinTime() {return this.join;}
    public long getLoginTime() {return this.login;}
    public long getLogoutTime() {return this.logout;}
    public long getPlayTime() {return this.playtime;}
    public long getAFKTime() {return this.afkTime;}
    public String getLastIP() {return (this.lastIP == null ? "" : this.lastIP);}
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
    public Map<String, String> getVariables() {this.updateVariables(false); return new HashMap<String, String>(this.variables);}
    public Map<String, String> getSavedVariables() {this.updateSavedVariables(false); return new HashMap<String, String>(this.savedVariables);}
    public Map<Kit, Long> getKitCooldowns() {return new HashMap<Kit, Long>(this.kitCooldowns);}
    public String getVariable(String key) {this.updateVariables(false); return this.variables.get(key);}
    public String getSavedVariable(String key) {this.updateSavedVariables(false); return this.savedVariables.get(key);}
    public long getKitCooldown(Kit k) {try {return this.kitCooldowns.get(k);}catch(Exception e) {return -1;}}
    public boolean getFlightMode() {return this.flyMode;}
    public DomsInventory getBackpack() {return this.backpack;}
    public DomsLocation getFurnaceLocation() {return this.playerFurnace;}
    public String getNickname() {return this.displayName;}
    public String getNamePlate() {return this.namePlate;}
    public PlayerScoreboard getScoreboard() {return this.scoreboard;}
    public String getStringUUID() {if(this.stringUUID == null) this.stringUUID = this.getUUID().toString();return this.stringUUID;}
    public UUID getUUID() {return UUID.fromString(stringUUID);}
    
    public boolean isOnline() {if(this.isConsole()) return true; return this.getOfflinePlayer().isOnline();}
    public boolean isVisible() {if(this.isConsole()) return true; return Base.isVisible(this.getOfflinePlayer());}
    public boolean isAFK() {return this.afk;}
    public final boolean isConsole() {return this.equals(CONSOLE_PLAYER);}
    public boolean isOp() {return this.getOfflinePlayer().isOp();}
    
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
    public void setVariable(String key, String variable) {this.variables.put(key, variable); this.updateVariables(false);}
    public void setSavedVariable(String key, String variable) {this.savedVariables.put(key, variable); this.updateSavedVariables(false);}
    public void setKitCooldown(Kit k, long l) {this.kitCooldowns.put(k, l);}
    public void setFlightMode(boolean f) {this.flyMode = f;}
    public void setFurnaceLocation(DomsLocation location) {this.playerFurnace = location.copy();}
    public DomsInventory setBackpack(DomsInventory inventory) {this.backpack = inventory; return this.backpack;}
    public void setScoreboard(PlayerScoreboard sb) {this.scoreboard = sb;}
    public void setStringUUID(String s) {this.stringUUID = s;}
    public void setNamePlate(String s) {
        this.namePlate = (s.equalsIgnoreCase("off") ? null : s);
        if(this.isOnline() && !this.isConsole()) {
            this.getOnlinePlayer().setCustomName(s.equalsIgnoreCase("off") ? this.getOnlinePlayer().getName(): s);
        }
    }
    
    @Override public String toString() {return this.getDisplayName();}
    
    public void addInventory(DomsInventory inv) {this.inventories.add(inv);}
    public void addEndChest(DomsInventory inv) {this.enderchest.add(inv);}
    public void addPlayTime(long time) {this.playtime += time;}
    public void addPunishment(Punishment p) {this.punishments.add(p);}
    public void addHome(Home h) {this.homes.add(h);}
    public void addItems(DomsItem held, int amount) {this.addItems(DomsItem.multiply(held, amount));}
    
    public void removePlayTime(long time) {this.playtime -= time;}
    public void removePunishment(Punishment p) {this.punishments.remove(p);}
    public void removeHome(Home h) {this.homes.remove(h);}
    public void removeItem(DomsItem item) {this.removeItem(item, 1);}
    
    public void toggleAFK() {this.afk = !this.afk;}
    
    public boolean hasPermisson(String perm) {if(this.isConsole()) return true; return Base.hasPermission(this.getOfflinePlayer(), perm);}
    public boolean hasPlayedBefore() {return this.getOfflinePlayer().hasPlayedBefore();}
    public boolean hasItem(DomsItem item) {return this.hasItem(item, 1);}
    
    public boolean canSee(OfflinePlayer t) {return Base.canSee(this.getOfflinePlayer(), t);}
    public boolean canBeSeenBy(OfflinePlayer t) {return Base.canSee(t, this.getOfflinePlayer());}
    
    public void teleport(DomsLocation to) {this.teleport(to, Base.getConfig().getBoolean("teleport.safe", true));}
    public void teleport(Location location) {this.teleport(new DomsLocation(location));}
    public void sendMessage(Object o) {Base.sendMessage(this, o);}
    public void save() {DataManager.PLAYER_MANAGER.savePlayer(this);}
    
    //Complex get's
    public final String getDisplayName() {
        //this.updateVariables(false);
        if(this.isConsole() && this.displayName == null) {
            this.displayName = "Server";
            return this.displayName;
        }
        if(this.isConsole()) {
            return this.displayName;
        }
        if(!this.isOnline()) {
            if(this.displayName == null) this.displayName = this.getOfflinePlayer().getName();
            return this.displayName;
        }
        if(!Base.isVisible(this.getOnlinePlayer())) return this.displayName;
        this.displayName = this.getOnlinePlayer().getDisplayName();
        return this.displayName;
    }
    
    public DomsLocation getLocation() {
        if(this.isConsole()) return null;
        if(this.isOnline()) return new DomsLocation(this.getOnlinePlayer().getLocation());
        return (this.lastLocation == null ? SpawnManager.SPAWN_MANAGER.getSpawn(Bukkit.getWorlds().get(0).getName()) : this.lastLocation);
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
            if(inv.getInventoryGroup().equalsIgnoreCase(g)) return inv;
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
    
    @Deprecated
    public String getAbsoluteGroup() {
        if(this.getWorld() == null) return null;
        if(PluginHook.VAULT_HOOK.isHooked() && PluginHook.VAULT_HOOK.getPermission() != null) {
            if(this.getWorld() == null) return null;
            return PluginHook.VAULT_HOOK.getPermission().getPrimaryGroup(this.getWorld(), this.getUsername());
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
            String playerPrefix =  PluginHook.VAULT_HOOK.getChat().getPlayerPrefix(this.getWorld(), this.getUsername());
            if(playerPrefix != null && !playerPrefix.equals("")) return playerPrefix;
            String groupPrefix = PluginHook.VAULT_HOOK.getChat().getGroupPrefix(this.getWorld(), this.getAbsoluteGroup());
            if(groupPrefix != null) return groupPrefix;
        }
        return "";
    }
    
    public String getChatSuffix() {
        if(this.getWorld() == null) return "";
        if(PluginHook.VAULT_HOOK.isHooked() && PluginHook.VAULT_HOOK.getChat() != null) {
            String playerSuffix =  PluginHook.VAULT_HOOK.getChat().getPlayerSuffix(this.getWorld(), this.getUsername());
            if(playerSuffix != null && !playerSuffix.equals("")) return playerSuffix;
            String groupSuffix = PluginHook.VAULT_HOOK.getChat().getGroupSuffix(this.getWorld(), this.getAbsoluteGroup());
            if(groupSuffix != null) return groupSuffix;
        }
        return "";
    }
    
    @Deprecated
    public DomsLocation getTarget() {
        Block block = this.getTargetBlock(100);
        if(block == null) return null;
        return new DomsLocation(block);
    }
    
    @Deprecated
    public Block getTargetBlock() {return this.getTargetBlock(VIEW_DISTANCE);}
    
    @Deprecated
    public Block getTargetBlock(int distance) {
        if(this.isConsole() || !this.isOnline()) return null;
        Block block = this.getOnlinePlayer().getTargetBlock(null, distance);
        if(block == null) return null;
        return block;
    }
    
    @Deprecated
    public BlockFace getTargetBlockFace() {return this.getTargetBlockFace(VIEW_DISTANCE);}
    
    @Deprecated
    public BlockFace getTargetBlockFace(int distance) {
        List<Block> blocks = this.getOnlinePlayer().getLastTwoTargetBlocks(null, distance);
        BlockFace face = null;
        if (blocks != null && blocks.size() > 1) {
          face = blocks.get(1).getFace(blocks.get(0));
        }
        return face;
    }
    
    @Deprecated
    public Block getWillPlaceBlock() {return this.getWillPlaceBlock(99);}
    
    @Deprecated
    public Block getWillPlaceBlock(int s) {
        Block target = this.getTargetBlock(s);
        BlockFace tFace = this.getTargetBlockFace(s);
        if(target == null || tFace == null) return null;
        return target.getRelative(tFace);
    }
    
    public String getCountry() {
        return null;
    }
    
    //Complex set's
    public void setDisplayName(String newName) {
        this.displayName = newName;
        Base.debug("Dn: " + this.displayName);
        if(this.isOnline() && !this.isConsole() && newName != null) {
            this.getOnlinePlayer().setDisplayName(newName);
            if(Base.getConfig().getBoolean("colors.nickname.tablist", true)) this.getOnlinePlayer().setPlayerListName(newName);
        }
    }
    
    //Complex is's
    public boolean isOnline(CommandSender sender) {
        if(!isOnline()) return false;
        if(this.isConsole()) return true;
        return Base.canSee(sender, this.getOnlinePlayer());
    }
    
    public boolean isBanned() {
        if(this.getOfflinePlayer().isBanned()) return true;
        if(SELBansHook.SELBANS_HOOK.isHooked()) {
            try {
                BansUtils.checkBans();
                if(BansUtils.isPlayerBanned(this.getOfflinePlayer())) return true;
            } catch(Exception e) {} catch(Error e) {}
        }
        for(Punishment p : this.getPunishmentsOfType(PunishmentType.BAN)) {
            if(!p.isActive()) continue;
            return true;
        }
        return false;
    }

    public boolean isMuted() {
        if(SELBansHook.SELBANS_HOOK.isHooked()) {
            try {
                if(!BansUtils.CanPlayerTalk(this.getOfflinePlayer())) return false;
            } catch(Exception e) {} catch(Error e) {}
        }
        
        for(Punishment p : this.getPunishmentsOfType(PunishmentType.MUTE)) {
            if(p == null) continue;
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
    
    @Deprecated
    public boolean isForumAARegistered() {
        try {
            return ForumAAHook.FORUMAA_HOOK.getSQLQuery().checkExists(this.getUsername())
                    || ForumAAHook.FORUMAA_HOOK.getSQLQuery().checkExists(this.getDisplayName());
        } catch(Exception e) {} catch(Error e) {}
        return false;
    }
    
    @Deprecated
    public boolean isForumAAActivated() {
        try {
            return ForumAAHook.FORUMAA_HOOK.getSQLQuery().checkActivated(this.getUsername())
                    || ForumAAHook.FORUMAA_HOOK.getSQLQuery().checkActivated(this.getDisplayName());
        } catch(Exception e) {} catch(Error e) {}
        return false;
    }
    
    //Complex To's
    
    //Complex Functions
    public void teleport(DomsLocation to, boolean useSafe) {
        if(!useSafe) {
            this.getOnlinePlayer().teleport(to.toLocation());
        } else {
            this.getOnlinePlayer().teleport(to.getSafeLocation().toLocation(), TeleportCause.COMMAND);
            this.getOnlinePlayer().setVelocity(new Vector(0, 0, 0));
        }
    }

    public boolean toggleChannel(DomsChannel channel) {
        DomsChannel current = DomsChannel.getPlayersChannel(this);
        if(current != null) current.removePlayer(this);
        if(current != null && current.equals(channel)) return false;
        channel.addPlayer(this);
        return true;
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
        if(s instanceof Player) return compare((Player) s);
        if(s.equals(this.getCommandSender())) return true;
        return false;
    }
    
    public boolean compare(Player player) {
        return player.getUniqueId().toString().equals(this.stringUUID);
    }
    
    public void addItems(List<DomsItem> items) {
        if(this.isConsole()) return;
        if(this.isOnline()) {
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
        } else {
        }
    }
    
    public final void updateDomsInventory() {
        if(!this.isOnline()) return;
        if(this.isConsole()) return;
        
        try {
            DomsInventory old = this.getInventoryFromWorld(this.getLocation().getWorld());
            if(old != null) this.inventories.remove(old);
            DomsInventory inv = DomsInventory.createFromPlayer(this);
            this.inventories.add(inv);
            old = this.getEndChestFromWorld(this.getLastLocation().getWorld());
            if(old != null) this.enderchest.remove(old);
            inv = DomsInventory.createEndChestFromPlayer(this);
            this.enderchest.add(inv);
        } catch(Exception e) {
            Base.debug("Failed to update " + this.getDisplayName() + "'s inventory.");
        }
    }
    
    public void updateVariables() {this.updateVariables(true);}
    public void updateSavedVariables() {this.updateSavedVariables(true);}
    
    private void updateVariables(boolean fireEvent) {
        this.variables.put("NAME", this.getUsername());
        this.variables.put("DISPLAYNAME", this.getDisplayName());
        this.variables.put("UUID", this.stringUUID);
        
        if(this.isAFK()) {
            this.variables.put("AWAY", "Away");
        } else {
            this.variables.put("AWAY", "");
        }
        
        if(!this.isConsole()) {
            if(this.getWorld() != null) this.variables.put("WORLD", this.getWorld());
            
            if(this.getChatPrefix() != null) this.variables.put("PREFIX", Base.colorise(this.getChatPrefix()));
            if(this.getChatSuffix() != null) this.variables.put("SUFFIX", Base.colorise(this.getChatSuffix()));
            if(this.getGroup() != null) this.variables.put("GROUP", Base.colorise(this.getGroup()));
        }
        
        if(this.isOnline()) {
            
        }
        
        if(this.isOnline() && !this.isConsole()) {
            this.variables.put("GAMEMODE", this.getOnlinePlayer().getGameMode().name());
        }
        
        //Finally, fire event
        if(fireEvent) {
            DomsPlayerUpdateVariablesEvent event = new DomsPlayerUpdateVariablesEvent(this);
            event.fireEvent();
        }
    }
    
    private void updateSavedVariables(boolean fireEvent) {
        if(fireEvent) {
            DomsPlayerUpdateSavedVariablesEvent event = new DomsPlayerUpdateSavedVariablesEvent(this);
            event.fireEvent();
        }
    }

    public void removeItem(DomsItem item, int amount) {
        if(!this.isOnline()) {
            //this.getInventory().removeItem(item, amount);
            return;
        }
        
        ItemStack[] inventory = this.getOnlinePlayer().getInventory().getContents();
        for(int i = 0; i < inventory.length; i++) {
            if(amount <= 0) break;
            ItemStack is = inventory[i];
            if(is == null) continue;
            
            int size = is.getAmount();
            DomsItem dItem = DomsItem.createItem(is);
            if(dItem == null || dItem.isAir()) continue;
            
            if(!dItem.compare(item)) continue;
            
            //Same Item
            if(size <= amount) {
                this.getOnlinePlayer().getInventory().setItem(i, null);
                is = null;
                amount -= size;
                continue;
            }
            
            is.setAmount(size - amount);
            amount = 0;
        }
    }

    public boolean hasItem(DomsItem item, int amount) {
        if(!this.isOnline()) return false;
        ItemStack[] inventory = this.getOnlinePlayer().getInventory().getContents();
        for(int i = 0; i < inventory.length; i++) {
            if(amount <= 0) return true;
            ItemStack is = inventory[i];
            if(is == null) continue;
            
            int size = is.getAmount();
            DomsItem dItem = DomsItem.createItem(is);
            if(dItem == null || dItem.isAir()) continue;
            
            if(!dItem.compare(item)) continue;
            
            //Same Item
            if(size <= amount) {
                amount -= size;
                if(amount <= 0) return true;
                continue;
            }
            
            is.setAmount(size - amount);
            amount = 0;
            return true;
        }
        return false;
    }

    public void refreshTag() {
        if(this.isConsole()) return;
        if(!this.isOnline()) return;
        try {
            PluginHook.TAGAPI_HOOK.refreshTags(this.getOnlinePlayer());
            Base.debug("Refreshed Tags.");
        } catch(Exception e) {} catch (Error e) {}
    }
}
