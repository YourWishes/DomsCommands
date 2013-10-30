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
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author      Dominic
 * @since       30/10/2013
 */
public class DumpCommand extends BukkitCommand {
    public DumpCommand() {
        super("dump");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        if(player == null || !player.isOnline() || player.isConsole()) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        //Make sure Player has a chest
        DomsItem item = new DomsItem(Material.CHEST);
        if(!player.hasItem(item)) {
            sendMessage(sender, ChatError + "You need to have a chest!");
            return true;
        }
        
        //Get Target Block
        Block b = player.getWillPlaceBlock();
        if(b == null) {
            sendMessage(sender, "You must look at a block, and the block you're trying to place must be available.");
            return true;
        }
        
        if(!b.getType().equals(Material.AIR)) {
            sendMessage(sender, ChatError + "Target block must be air.");
            return true;
        }
        
        //TODO check for double, triple chests etc
        b.setType(Material.CHEST);
        Chest c = (Chest) b.getState();
        
        //Remove the chest
        player.removeItem(item);
        
        Inventory inv = c.getBlockInventory();
        Inventory pinv = player.getOnlinePlayer().getInventory();
        ItemStack[] contents = pinv.getContents();
        
        for(ItemStack is : contents) {
            if(DomsItem.isInventoryFull(inv)) break;
            DomsItem i = DomsItem.createItem(is);
            if(i == null || i.isAir()) continue;
            player.removeItem(i, is.getAmount());
            try{inv.addItem(i.getItemStack(is.getAmount()));}catch(Exception e){}
        }
        sendMessage(sender, "Dumped items into chest.");
        return true;
    }
}
