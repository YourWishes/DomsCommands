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

package com.domsplace.DomsCommands.Commands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsItem;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

/**
 * @author      Dominic
 * @since       30/10/2013
 */
public class HelmetCommand extends BukkitCommand {
    public HelmetCommand() {
        super("helmet");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        if(player == null || !player.isOnline() || player.isConsole()) {
            sendMessage(sender, ChatError + "Couldn't find player.");
            return true;
        }
        
        DomsItem current = DomsItem.createItem(player.getOnlinePlayer().getEquipment().getHelmet());
        if(current != null && !current.isAir()) {
            sendMessage(sender, ChatError + "Please remove your helmet first.");
            return true;
        }
        
        DomsItem held = DomsItem.createItem(player.getOnlinePlayer().getItemInHand());
        if(held == null || held.isAir()) {
            sendMessage(sender, ChatError + "You must be holding an item.");
            return true;
        }
        
        player.getOnlinePlayer().getEquipment().setHelmet(player.getOnlinePlayer().getItemInHand());
        player.getOnlinePlayer().setItemInHand(new ItemStack(Material.AIR));
        
        sendMessage(sender, "Set " + ChatImportant + held.toHumanString() + ChatDefault + " as your helmet.");
        return true;
    }
}
