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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       07/10/2013
 */
public class DomsPlayer {
    private static final Map<String, DomsPlayer> REGISTERED_PLAYERS = new HashMap<String, DomsPlayer>();
    
    public static DomsPlayer guessPlayer(CommandSender sender, String guess) {
        Player tryPlayer = Base.getPlayer(sender, guess);
        if(tryPlayer == null) {
            return guessPlayer(guess);
        } else {
            return getPlayer(tryPlayer);
        }
    }
    
    public static DomsPlayer guessPlayer(String guess) {
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
        
        return p;
    }
    
    public static List<DomsPlayer> getRegisteredPlayers() {return new ArrayList<DomsPlayer>(REGISTERED_PLAYERS.values());}
    
    public static DomsPlayer getPlayer(CommandSender p) {return getPlayer(p.getName());}
    public static DomsPlayer getPlayer(Player p) {return getPlayer(p.getName());}
    public static DomsPlayer getPlayer(OfflinePlayer player) {return getPlayer(player.getName());}
    public static DomsPlayer getPlayer(String player) {
        if(isPlayerRegistered(player)) return REGISTERED_PLAYERS.get(player);
        
        return new DomsPlayer(player);
    }
    
    public static boolean isPlayerRegistered(Player player) {return isPlayerRegistered(player.getName());}
    public static boolean isPlayerRegistered(String player) {return REGISTERED_PLAYERS.containsKey(player);}
    
    //Instance
    private String player;
    private String displayName;
    
    private String lastIP;
    
    private boolean afk;
    
    private long join;
    private long login;
    private long logout;
    private long playtime;
    
    private DomsLocation backLocation;
    
    private DomsLocation lastLocation;
    private long lastMoveTime;
    
    private List<Punishment> punishments;
    
    private TeleportRequest lastRequest;
    
    private DomsPlayer(String player) {
        this.player = player;
        this.displayName = this.getDisplayName();
        this.punishments = new ArrayList<Punishment>();
        
        this.registerPlayer();
    }
    
    private void registerPlayer() {REGISTERED_PLAYERS.put(player, this);}
    
    public String getPlayer() {return this.player;}
    public OfflinePlayer getOfflinePlayer() {return Bukkit.getOfflinePlayer(player);}
    public Player getOnlinePlayer() {return this.getOfflinePlayer().getPlayer();}
    public long getJoinTime() {return this.join;}
    public long getLoginTime() {return this.login;}
    public long getLogoutTime() {return this.logout;}
    public long getPlayTime() {return this.playtime;}
    public String getLastIP() {return this.lastIP;}
    public long getLastMoveTime() {return this.lastMoveTime;}
    public TeleportRequest getLastTeleporRequest() {return this.lastRequest;}
    public DomsLocation getBackLocation() {return this.backLocation;}
    public DomsLocation getLastLocation() {return this.lastLocation;}
    public List<Punishment> getPunishments() {return new ArrayList<Punishment>(this.punishments);}
    
    public boolean isOnline() {return this.getOfflinePlayer().isOnline();}
    public boolean isVisible() {return Base.isVisible(this.getOfflinePlayer());}
    public boolean isAFK() {return this.afk;}
    
    public void setJoinTime(long time) {this.join = time;}
    public void setLoginTime(long time) {this.login = time;}
    public void setLogoutTime(long time) {this.logout = time;}
    public void setPlayTime(long time) {this.playtime = time;}
    public void setLastIP(String ip) {this.lastIP = ip;}
    public void setLastLocation(DomsLocation location) {this.lastLocation = location;}
    public void setLastMoveTime(long time) {this.lastMoveTime = time;}
    public void setLastTeleportRequest(TeleportRequest request) {this.lastRequest = request;}
    public void setBackLocation(DomsLocation location) {this.backLocation = location;}
    
    @Override public String toString() {return this.getDisplayName();}
    
    public void addPlayTime(long time) {this.playtime += time;}
    public void addPunishment(Punishment p) {this.punishments.add(p);}
    
    public void removePlayTime(long time) {this.playtime -= time;}
    public void removePunishment(Punishment p) {this.punishments.remove(p);}
    
    public boolean canSee(OfflinePlayer t) {return Base.canSee(this.getOfflinePlayer(), t);}
    public boolean canBeSeenBy(OfflinePlayer t) {return Base.canSee(t, this.getOfflinePlayer());}
    public void teleport(DomsLocation to) {this.teleport(to, Base.getConfig().getBoolean("teleport.safe", true));}
    
    //Complex get's
    public final String getDisplayName() {
        if(!this.isOnline()) return this.displayName;
        if(!Base.isVisible(this.getOnlinePlayer())) return this.displayName;
        this.displayName = this.getOnlinePlayer().getDisplayName();
        return this.displayName;
    }
    public DomsLocation getLocation() {
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
    
    //Complex is's
    public boolean isOnline(CommandSender sender) {
        if(!isOnline()) return false;
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
}
