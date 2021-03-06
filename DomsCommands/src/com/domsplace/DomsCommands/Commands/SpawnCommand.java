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
import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.DataManagers.SpawnManager;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       28/10/2013
 */
public class SpawnCommand extends BukkitCommand {
    public SpawnCommand() {
        super("spawn");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        if(args.length > 0) {
            player = DomsPlayer.guessOnlinePlayer(sender, args[0]);
        }
        
        if(player == null || !player.isOnline(sender) || player.isConsole()) {
            sendMessage(sender, ChatError + "Couldn't find player.");
            return true;
        }
        
        if(!player.compare(sender) && !hasPermission(sender, "DomsCommands.spawn.others")) {
            return this.noPermission(sender, cmd, label, args);
        }
        
        player.teleport(SpawnManager.SPAWN_MANAGER.getSpawn(player.getWorld()));
        sendMessage(player, "Teleporting you to spawn.");
        if(!player.compare(sender)) {
            sendMessage(sender, "Teleporting " + ChatImportant + 
                    player.getDisplayName() + ChatDefault + " to spawn of world "
                    + ChatImportant + player.getWorld() + ChatDefault + ".");
        }
        return true;
    }
}
