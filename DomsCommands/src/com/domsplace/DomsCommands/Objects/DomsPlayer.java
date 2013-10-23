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
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Exceptions.InvalidItemException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
    
    //Instance
    private String player;
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
    
    private List<Punishment> punishments;
    private List<Home> homes;
    
    private TeleportRequest lastRequest;
    
    private DomsPlayer lastPrivateMessenger;
    
    private DomsPlayer(String player) {
        this.player = player;
        this.displayName = this.getDisplayName();
        this.punishments = new ArrayList<Punishment>();
        this.homes = new ArrayList<Home>();
        this.afk = false;
        this.afkTime = Base.getNow();
        this.lastMoveTime = Base.getNow();
        
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
    public DomsPlayer getLastMessenger() {return this.lastPrivateMessenger;}
    public File getPlayerFile() {return this.playerFile;}
    
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
    
    @Override public String toString() {return this.getDisplayName();}
    
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
    
    //Complex set's
    public void setDisplayName(String newName) {
        this.displayName = newName;
        if(this.isOnline() && !this.isConsole()) this.getOnlinePlayer().setDisplayName(newName);
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
    
    //Complex To's
    
    //Complex Functions
    public void teleport(DomsLocation to, boolean useSafe) {
        if(!useSafe) this.getOnlinePlayer().teleport(to.toLocation());
        else this.getOnlinePlayer().teleport(to.getSafeLocation().toLocation());
    }
    
    public void kickPlayer(String r) {
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
}
