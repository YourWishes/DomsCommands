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

package com.domsplace.DomsCommands.Commands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dominic Masters
 */
public class ClearInventoryCommand extends BukkitCommand {
    public ClearInventoryCommand() {
        super("clearinventory");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return false;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        
        if(args.length > 0) {
            player = DomsPlayer.guessOnlinePlayer(sender, args[0]);
        }
        
        if(player == null || !player.isOnline() || player.isConsole()) {
            sendMessage(sender, ChatError + "Target isn't online.");
            return true;
        }
        
        
        if(player.compare(sender)) {
            player.getOnlinePlayer().getInventory().clear();
            sendMessage(sender, "Cleared your inventory.");
            return true;
        }
        
        if(!hasPermission(sender, "DomsCommands.clearinventory.others")) {
            return this.noPermission(sender, cmd, label, args);
        }
        
        player.getOnlinePlayer().getInventory().clear();
        sendMessage(sender, "Cleared " + ChatImportant + player.getDisplayName() + ChatDefault + "'s Inventory.");
        sendMessage(player, "Your inventory has been cleared.");
        return true;
    }
}
