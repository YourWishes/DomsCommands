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
public class ReplyCommand extends BukkitCommand {
    public ReplyCommand() {
        super("reply");
        this.addSubCommandOption(new SubCommandOption("message"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        DomsPlayer talker = DomsPlayer.getPlayer(sender);
        DomsPlayer replier = talker.getLastMessenger();
        
        if(talker.isMuted()) {
            sendMessage(sender, ChatError + "You can't message, you're muted.");
            return true;
        }
        
        if(replier == null) {
            sendMessage(sender, ChatError + "You have no one to reply to.");
            return true;
        }
        
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a message.");
            return false;
        }
        
        if(talker.equals(replier)) {
            sendMessage(sender, ChatError + "You can't message yourself.");
            return true;
        }
        
        if(!replier.isOnline(sender)) {
            sendMessage(sender, ChatError + replier.getDisplayName() + ChatError + " isn't online.");
            return true;
        }
        
        String message = "";
        for(int i = 0; i < args.length; i++) {
            message += args[i];
            if(i < args.length - 1) {
                message += " ";
            }
        }
        
        message = coloriseByPermission(message, DomsPlayer.getPlayer(sender), "DomsCommands.tell.colors.");
        
        talker.setLastMessenger(replier);
        replier.setLastMessenger(talker);
        
        talker.sendMessage(ChatDefault + "[" + ChatImportant + "You" + ChatDefault + " -> " + ChatImportant + replier.getDisplayName() + ChatDefault + "] " + message);
        replier.sendMessage(ChatDefault + "[" + ChatImportant + talker.getDisplayName() + ChatDefault + " -> " + ChatImportant + "You" + ChatDefault + "] " + message);
        
        return true;
    }
}
