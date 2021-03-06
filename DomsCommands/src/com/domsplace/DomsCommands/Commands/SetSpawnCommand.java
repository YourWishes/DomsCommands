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
import com.domsplace.DomsCommands.DataManagers.SpawnManager;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       7/11/2013
 */
public class SetSpawnCommand extends BukkitCommand {
    public SetSpawnCommand() {
        super("setspawn");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        SpawnManager.SPAWN_MANAGER.setSpawn(player.getLocation(), player.getWorld());
        sendMessage(sender, ChatDefault + "Set the spawn point to " +
                ChatImportant + player.getLocation().toHumanString().replaceAll("in", ChatDefault + "in" + ChatImportant));
        return true;
    }
}
