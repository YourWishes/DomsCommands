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

package com.domsplace.DomsCommands.Listeners;

import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Objects.DomsInventory;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Dominic Masters
 */
public class DomsInventoryListener extends DomsListener {
    @EventHandler(priority=EventPriority.HIGHEST)
    public void changeInventoryOnWorldChange(PlayerTeleportEvent e) {
        try {if(DomsInventory.getInventoryGroupFromWorld(e.getFrom().getWorld().getName()).equals(
                DomsInventory.getInventoryGroupFromWorld(e.getTo().getWorld().getName())
        )) return;} catch(Exception ex) {}
        
        DomsPlayer player = DomsPlayer.getDomsPlayerFromPlayer(e.getPlayer());
        player.updateDomsInventory();
        
        DomsInventory inv = player.getInventoryFromWorld(e.getTo().getWorld().getName());
        if(inv == null) {
            return;
        }
        
        inv.setToPlayer();
        
        inv = player.getEndChestFromWorld(e.getTo().getWorld().getName());
        if(inv == null) {
            return;
        }
        
        inv.setToInventory(e.getPlayer().getEnderChest());
    }
    
    @EventHandler(priority=EventPriority.HIGHEST)
    public void switchInventoryOnRespawn(PlayerRespawnEvent e) {
        try {if(DomsInventory.getInventoryGroupFromWorld(e.getPlayer().getLocation().getWorld().getName()).equals(
                DomsInventory.getInventoryGroupFromWorld(e.getRespawnLocation().getWorld().getName())
        )) return;} catch(Exception ex) {}
        
        DomsPlayer player = DomsPlayer.getDomsPlayerFromPlayer(e.getPlayer());
        player.updateDomsInventory();
        
        DomsInventory inv = player.getInventoryFromWorld(e.getRespawnLocation().getWorld().getName());
        if(inv == null) {
            return;
        }
        
        inv.setToPlayer();
        
        inv = player.getEndChestFromWorld(e.getRespawnLocation().getWorld().getName());
        if(inv == null) {
            return;
        }
        
        inv.setToInventory(e.getPlayer().getEnderChest());
    }
}
