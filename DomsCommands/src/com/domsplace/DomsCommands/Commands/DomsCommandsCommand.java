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

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       07/10/2013
 */
public class DomsCommandsCommand extends BukkitCommand {
    public DomsCommandsCommand() {
        super("DomsCommands");
        this.addSubCommandOption(new SubCommandOption("reload"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("reload")) {
                if(!hasPermission(sender, "DomsCommands.reload")) return this.noPermission(sender, cmd, label, args);
                //Reload Configuration
                sender.sendMessage(ChatDefault + "Reloading Configuration...");
                if(DataManager.loadAll()) {
                    sender.sendMessage(ChatImportant + "Reloaded Configuration!");
                } else {
                    sender.sendMessage(ChatError + "Failed to Reload Configuration! Check console for errors.");
                }
                return true;
            }
            
            sender.sendMessage(ChatError + "Unknown Argument " + args[0] + ".");
            return true;
        }
        
        Base.sendMessage(sender, new String[] {
            ChatImportant + " = " + ChatDefault + getPlugin().getName() + ChatImportant + " = ",
            ChatDefault + "\tProgrammed By: " + ChatColor.LIGHT_PURPLE + "Dom",
            ChatDefault + "\tWebsite: " + ChatColor.GREEN + "http://domsplace.com/"
        });
        return true;
    }
}
