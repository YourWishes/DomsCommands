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
import com.domsplace.DomsCommands.Bases.PluginHook;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       17/10/2013
 */
public class NameplateCommand extends BukkitCommand {
    public NameplateCommand() {
        super("nameplate");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, "nickname"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        DomsPlayer target = null;
        String nameplate = null;
        
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
                sendMessage(sender, ChatError + "Nameplate cannot contain spaces.");
                return true;
            }
            
            target = DomsPlayer.guessPlayer(sender, args[0]);
            nameplate = args[1];
        } else {
            if(args.length > 1) {
                if(args.length > 2) {
                    sendMessage(sender, ChatError + "Nameplate cannot contain spaces.");
                    return true;
                }
            
                target = DomsPlayer.guessPlayer(sender, args[0]);
                nameplate = args[1];
            } else {
                if(args.length < 1) {
                    sendMessage(sender, ChatError + "Please enter a nameplate or a player name.");
                    return false;
                }
            
                if(args.length > 1) {
                    sendMessage(sender, ChatError + "Nameplate cannot contain spaces.");
                    return true;
                }
                
                target = DomsPlayer.getPlayer(sender);
                nameplate = args[0];
            }
        }
        
        if(target == null) {
            sendMessage(sender, ChatError + "Couldn't find " + args[0] + ".");
            return true;
        }
        
        if(target.isConsole()) {
            sendMessage(sender, ChatError + "Console can't have a nameplate.");
            return true;
        }
        
        if(nameplate == null) {
            sendMessage(sender, ChatError + "Please enter a nameplate.");
            return false;
        }
        
        if(!nameplate.matches(DomsPlayer.NAMEPLATE_REGEX)) {
            sendMessage(sender, ChatError + "Nameplate contains an invalid symbol.");
            return true;
        }
        
        if(nameplate.equalsIgnoreCase("off")) {
            target.setNamePlate(target.getPlayer());
            sendMessage(sender, ChatDefault + "Turned " + ChatImportant + target.getPlayer() + ChatDefault + "'s nameplate off.");
            return true;
        }
        
        if(!PluginHook.TAGAPI_HOOK.isHooked()) {
            sendMessage(sender, ChatImportant + "TagAPI isn't hooked, nameplates won't display.");
        }
        
        target.setNamePlate(coloriseByPermission(nameplate, DomsPlayer.getPlayer(sender), "DomsCommands.nameplate.colors."));
        sendMessage(sender, "The new nameplate for " + ChatImportant + target.getDisplayName() + ChatDefault + " is " + ChatImportant + target.getNamePlate());
        target.save();
        target.refreshTag();
        return true;
    }
}
