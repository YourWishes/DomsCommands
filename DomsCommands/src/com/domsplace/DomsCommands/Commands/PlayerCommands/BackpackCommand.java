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

package com.domsplace.DomsCommands.Commands.PlayerCommands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

/**
 * @author      Dominic
 * @since       27/10/2013
 */
public class BackpackCommand extends BukkitCommand {
    public BackpackCommand() {
        super("backpack");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        DomsPlayer p = DomsPlayer.getPlayer(sender);
        Inventory i = p.getBackpack();
        if(i == null) i = p.setBackpack(Bukkit.createInventory(p.getOnlinePlayer(), 54, p.getPlayer() + "'s Backpack."));
        
        p.getOnlinePlayer().openInventory(i);
        sendMessage(sender, "Opening your backpack.");
        return true;
    }
}
