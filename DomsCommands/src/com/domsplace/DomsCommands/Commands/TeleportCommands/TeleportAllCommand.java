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

package com.domsplace.DomsCommands.Commands.TeleportCommands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class TeleportAllCommand extends BukkitCommand {
    public TeleportAllCommand() {
        super("teleportall");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return true;
        }
        
        DomsPlayer target;
        
        if(args.length == 0) {
            target = DomsPlayer.getPlayer(sender);
        } else {
            target = DomsPlayer.guessOnlinePlayer(sender, args[0]);
            if(target == null || !target.isOnline(sender) || target.isConsole()) {
                sendMessage(sender, ChatError + args[0] + " isn't online.");
                return true;
            }
        }
        
        if(target == null) return true;
        for(Player p : Bukkit.getOnlinePlayers()) {
            DomsPlayer pl = DomsPlayer.getPlayer(p);
            if(pl == null) continue;
            if(target.equals(pl)) continue;
            pl.setBackLocation(pl.getLocation());
            pl.teleport(target.getLocation());
            sendMessage(pl, "Teleporting you to " + ChatImportant + target.getDisplayName());
        }
        
        sendMessage(sender, "Teleported everyone to " + ChatImportant + target.getDisplayName());
        return true;
    }
}
