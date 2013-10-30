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
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       27/10/2013
 */
public class PositionCommand extends BukkitCommand {
    public PositionCommand() {
        super("position");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        if(args.length > 0) {
            player = DomsPlayer.guessPlayer(sender, args[0]);
        }
        
        if(player == null || !player.isOnline() || player.isConsole()) {
            sendMessage(sender, ChatError + "Couldn't find player.");
            return true;
        }
        
        if(!DomsPlayer.getPlayer(sender).equals(player) && !hasPermission(sender, "DomsCommands.position.others")) {
            return this.noPermission(sender, cmd, label, args);
        }
        
        DomsLocation loc = player.getLocation();
        sendMessage(sender, new String[] {
            ChatImportant + player.getDisplayName() + ChatImportant + "'s position:",
            loc.toHumanString()
        });
        return true;
    }
}
