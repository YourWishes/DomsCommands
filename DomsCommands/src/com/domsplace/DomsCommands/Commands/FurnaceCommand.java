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
import com.domsplace.DomsCommands.Objects.DomsLocation;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       30/10/2013
 */
public class FurnaceCommand extends BukkitCommand {
    public FurnaceCommand() {
        super("furnace");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        if(player == null || !player.isOnline() || player.isConsole()) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        if(args.length > 0 && args[0].equalsIgnoreCase("set")) {
            Block target = player.getTargetBlock();
            if(target == null || target.getType().equals(Material.AIR)) {
                sendMessage(sender, ChatError + "You must be looking a block.");
                return true;
            }
            
            if(!(target.getState() instanceof Furnace)) {
                sendMessage(sender, ChatError + "You must be looking at a furnace.");
                return true;
            }
            
            player.setFurnaceLocation(new DomsLocation(target));
            sendMessage(sender, "Set Furnace. Open it with \"/{0}\" at any time.", label);
            return true;
        }
        
        //Get Furnace
        DomsLocation furnace = player.getFurnaceLocation();
        if(furnace == null) {
            sendMessage(sender, ChatError + "No furnace set, run \"/{0} set\" first.", label);
            return true;
        }
        
        if(!furnace.isWorldLoaded() || furnace.getBlock() == null || !(furnace.getBlock().getState() instanceof Furnace)) {
            sendMessage(sender, ChatError + "Your furnace is no longer available.");
            return true;
        }
        
        Furnace f = (Furnace) furnace.getBlock().getState();
        player.getOnlinePlayer().openInventory(f.getInventory());
        sendMessage(sender, "Opening your furnace.");
        return true;
    }
}
