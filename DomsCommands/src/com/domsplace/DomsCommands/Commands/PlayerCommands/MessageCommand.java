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
 * @since       17/10/2013
 */
public class MessageCommand extends BukkitCommand {
    public MessageCommand() {
        super("tell");
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
        
        DomsPlayer talker = DomsPlayer.getPlayer(sender);
        DomsPlayer target = DomsPlayer.guessOnlinePlayer(sender, args[0]);
        
        if(talker.isMuted()) {
            sendMessage(sender, ChatError + "You can't message, you're muted.");
            return true;
        }
        
        if(talker.equals(target)) {
            sendMessage(sender, ChatError + "You can't message yourself.");
            return true;
        }
        
        if(target == null || !target.isOnline(sender)) {
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
        
        message = coloriseByPermission(message, DomsPlayer.getPlayer(sender), "DomsCommands.tell.colors.");
        
        talker.setLastMessenger(target);
        target.setLastMessenger(talker);
        
        talker.sendMessage(ChatDefault + "[" + ChatImportant + "You" + ChatDefault + " -> " + ChatImportant + target.getDisplayName() + ChatDefault + "] " + message);
        target.sendMessage(ChatDefault + "[" + ChatImportant + talker.getDisplayName() + ChatDefault + " -> " + ChatImportant + "You" + ChatDefault + "] " + message);
        
        return true;
    }
}
