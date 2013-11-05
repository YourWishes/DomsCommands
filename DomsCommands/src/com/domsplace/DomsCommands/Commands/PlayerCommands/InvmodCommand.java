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
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       27/10/2013
 */
public class InvmodCommand extends BukkitCommand {
    public InvmodCommand() {
        super("invmod");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Enter a player name.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.guessOnlinePlayer(sender, args[0]);
        if(player == null || !player.isOnline(sender) || player.isConsole()) {
            sendMessage(sender, ChatError + "Player is not online.");
            return true;
        }
        
        DomsPlayer send = DomsPlayer.getPlayer(sender);
        if(send.equals(player)) {
            sendMessage(sender, ChatError + "Can't mod your own Inventory.");
            return true;
        }
        
        if(player.hasPermisson("DomsCommands.invmod.exempt")) {
            sendMessage(sender, ChatError + "Can't modify this player's Inventory.");
            return true;
        }
        
        if(!player.getWorld().equals(send.getWorld())) {
            sendMessage(sender, ChatError + "Must be in same world as player.");
            return true;
        }
        
        send.getOnlinePlayer().openInventory(player.getOnlinePlayer().getInventory());
        sendMessage(sender, "Opening " + ChatImportant + player.getDisplayName() + ChatDefault + ".");
        return true;
    }
}
