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

package com.domsplace.DomsCommands.Listeners;

import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.DataManagers.SpawnManager;
import com.domsplace.DomsCommands.Events.PlayerFirstJoinedEvent;
import com.domsplace.DomsCommands.Events.PlayerLeaveGameEvent;
import com.domsplace.DomsCommands.Events.PlayerPostFirstJoinEvent;
import com.domsplace.DomsCommands.Objects.DomsLocation;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Kit;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author      Dominic
 * @since       07/10/2013
 */
public class PlayerRegisterListener extends DomsListener {
    public PlayerRegisterListener() {
        super();
        for(Player p : Bukkit.getOnlinePlayers()) {
            DomsPlayer player = DomsPlayer.getPlayer(p);
        }
    }
    
    @EventHandler(priority=EventPriority.LOWEST)
    public void registerOnJoin(PlayerJoinEvent e) {
        if(!DomsPlayer.isPlayerRegistered(e.getPlayer()) || !DomsPlayer.getPlayer(e.getPlayer()).hasPlayedBefore()) {
            //Create Player
            DomsPlayer player = DomsPlayer.getPlayer(e.getPlayer());
            
            //Store Changes
            player.setJoinTime(player.getOfflinePlayer().getFirstPlayed() == 0 ? getNow() : player.getOfflinePlayer().getFirstPlayed());
            player.setLoginTime(player.getOfflinePlayer().getFirstPlayed() == 0 ? getNow() : player.getOfflinePlayer().getFirstPlayed());
            player.setLogoutTime(player.getOfflinePlayer().getLastPlayed()== 0 ? getNow() : player.getOfflinePlayer().getLastPlayed());
            player.setLastIP(e.getPlayer().getAddress().getAddress().getHostAddress());
            player.setLastLocation(new DomsLocation(e.getPlayer().getLocation()));
            player.setLastMoveTime(getNow());
            player.setFlightMode(e.getPlayer().getAllowFlight());
            player.setAFKTime(getNow());
            
            if(player.getNamePlate() != null && !player.getNamePlate().equalsIgnoreCase("off") && !player.getNamePlate().replaceAll(" ", "").equals("")) {
                player.setNamePlate(player.getNamePlate());
            }
            
            try {player.teleport(SpawnManager.SPAWN_MANAGER.getSpawn(player.getWorld()), false);} catch(Exception ex) {}
        
            try {player.getInventory().setToPlayer();} catch(Exception ex) {}
            try {player.getEnderChest().setToInventory(e.getPlayer().getEnderChest());} catch(Exception ex) {}
            
            //Award Kit
            if(getConfig().contains("joinkit")) {
                Kit k = Kit.getKit(getConfig().getString("joinkit"));
                if(k != null) player.addItems(k.getItems());
                player.updateDomsInventory();
            }
            
            //Fire Event
            PlayerFirstJoinedEvent event = new PlayerFirstJoinedEvent(player);
            event.fireEvent();
        }
        DomsPlayer player = DomsPlayer.getPlayer(e.getPlayer());
        if(player.getNickname() != null && !player.getNickname().equalsIgnoreCase("off")) player.setDisplayName(player.getNickname());
        
        DomsPlayer.REGISTERED_ONLINE_PLAYERS.put(player.getPlayer(), player);
        
        //Store Changes
        player.setLoginTime(getNow());
        player.setLastIP(e.getPlayer().getAddress().getAddress().getHostAddress());
        player.setLastLocation(new DomsLocation(e.getPlayer().getLocation()));
        player.setLastMoveTime(getNow());
        player.setAFK(false);
        player.setAFKTime(getNow());
        
        if(player.getNamePlate() != null && !player.getNamePlate().equalsIgnoreCase("off") && !player.getNamePlate().replaceAll(" ", "").equals("")) {
            player.setNamePlate(player.getNamePlate());
        }
        
        try {player.getInventory().setToPlayer();} catch(Exception ex) {}
        try {player.getEnderChest().setToInventory(e.getPlayer().getEnderChest());} catch(Exception ex) {}
        
        player.getOnlinePlayer().setAllowFlight(player.getFlightMode());
        
        //Fire event
        try {
            PlayerPostFirstJoinEvent event = new PlayerPostFirstJoinEvent(player);
            event.fireEvent();
            debug("Fired Event");
            DataManager.PLAYER_MANAGER.savePlayer(player);
            player.updateSavedVariables();
        } catch(Exception ex) {
            error("Failed to Save new player..", ex);
        }
        debug("Saved.");
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void saveMoveChanges(PlayerMoveEvent e) {
        DomsPlayer player = DomsPlayer.getPlayer(e.getPlayer());
        if(player == null) return;
        player.setLastLocation(new DomsLocation(e.getTo()));
        player.setLastMoveTime(getNow());
    }
    
    @EventHandler
    public void saveDataBeforeLogout(PlayerLeaveGameEvent e) {
        DomsPlayer player = DomsPlayer.getPlayer(e.getPlayer());
        player.setLastIP(e.getPlayer().getAddress().getAddress().getHostAddress());
        player.setLogoutTime(getNow());
        player.setLastLocation(player.getLocation());
        player.getDisplayName();
        player.setLastTeleportRequest(null);
        player.updateDomsInventory();
        player.isFlightMode();
        
        //Close Anyone invmoding this players inventory
        List<HumanEntity> ents = new ArrayList<HumanEntity>(player.getOnlinePlayer().getInventory().getViewers());
        for(HumanEntity h : ents) {
            h.closeInventory();
        }
        
        DataManager.PLAYER_MANAGER.savePlayer(player);
    }
}
