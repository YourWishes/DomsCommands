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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       17/10/2013
 */
public class AwayCommand extends BukkitCommand {
    public AwayCommand() {
        super("away");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can run this command.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        
        if(!player.isVisible()) {
            sendMessage(sender, ChatError + "You're hidden... not doing that.");
            return true;
        }
        
        player.toggleAFK();
        if(player.isAFK()) {
            player.setAFKTime(getNow());
            broadcast(getConfigManager().format(player, getConfig().getString("away.message", "")));
        } else {
            broadcast(getConfigManager().format(player, getConfig().getString("away.messageback", "")));
        }
        return true;
    }
}
