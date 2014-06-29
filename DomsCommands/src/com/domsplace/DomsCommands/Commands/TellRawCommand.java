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
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       17/10/2013
 */
public class TellRawCommand extends BukkitCommand {
    public TellRawCommand() {
        super("tellraw");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, "message"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return false;
        }
        
        if(args.length < 2) {
            sendMessage(sender, ChatError + "Please enter a message.");
            return false;
        }
        
        DomsPlayer target = DomsPlayer.guessOnlinePlayer(sender, args[0]);
        
        if(target == null || !target.isOnline(sender) || target.isConsole()) {
            sendMessage(sender, ChatError + "Target isn't online.");
            return true;
        }
        
        String message = "";
        for(int i = 1; i < args.length; i++) {
            message += args[i];
            if(i < args.length - 1) {
                message += " ";
            }
        }
        
        message = colorise(message);
        sendRawMessage(target.getOnlinePlayer(), message);
        sendMessage(sender, "Sent raw message.");
        return true;
    }
}