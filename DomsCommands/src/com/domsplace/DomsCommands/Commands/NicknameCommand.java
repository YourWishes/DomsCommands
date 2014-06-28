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
public class NicknameCommand extends BukkitCommand {
    public NicknameCommand() {
        super("nickname");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, "nickname"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        DomsPlayer target = null;
        String nickname = null;
        
        if(!isPlayer(sender)) {
            if(args.length < 1) {
                sendMessage(sender, ChatError + "Please enter a player name.");
                return false;
            }
            
            if(args.length < 2) {
                sendMessage(sender, ChatError + "Please enter a nickname.");
                return false;
            }
            
            if(args.length > 2) {
                sendMessage(sender, ChatError + "Nickname cannot contain spaces.");
                return true;
            }
            
            target = DomsPlayer.guessPlayer(sender, args[0]);
            nickname = args[1];
        } else {
            if(args.length > 1) {
                if(args.length > 2) {
                    sendMessage(sender, ChatError + "Nickname cannot contain spaces.");
                    return true;
                }
            
                target = DomsPlayer.guessPlayer(sender, args[0]);
                nickname = args[1];
            } else {
                if(args.length < 1) {
                    sendMessage(sender, ChatError + "Please enter a nickname or a player name.");
                    return false;
                }
            
                if(args.length > 1) {
                    sendMessage(sender, ChatError + "Nickname cannot contain spaces.");
                    return true;
                }
                
                target = DomsPlayer.getPlayer(sender);
                nickname = args[0];
            }
        }
        
        if(target == null) {
            sendMessage(sender, ChatError + "Couldn't find " + args[0] + ".");
            return true;
        }
        
        if(nickname == null) {
            sendMessage(sender, ChatError + "Please enter a nickname.");
            return false;
        }
        
        if(!nickname.matches(DomsPlayer.NICKNAME_REGEX)) {
            sendMessage(sender, ChatError + "Nickname contains an invalid symbol.");
            return true;
        }
        
        if(nickname.equalsIgnoreCase("off")) {
            target.setDisplayName(target.getUsername());
            sendMessage(sender, ChatDefault + "Turned " + ChatImportant + target.getUsername() + ChatDefault + "'s nickname off.");
            return true;
        }
        
        target.setDisplayName(coloriseByPermission(nickname, DomsPlayer.getPlayer(sender), "DomsCommands.nickname.colors."));
        sendMessage(sender, "The new nickname for " + ChatImportant + target.getUsername() + ChatDefault + " is " + ChatImportant + target.getDisplayName());
        target.save();
        return true;
    }
}
