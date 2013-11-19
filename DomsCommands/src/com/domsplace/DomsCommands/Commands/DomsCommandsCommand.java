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

import com.domsplace.DomsCommands.Bases.*;
import com.domsplace.DomsCommands.Objects.*;
import com.domsplace.DomsCommands.Threads.*;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
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
        this.addSubCommandOption(new SubCommandOption("save"));
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
            
            if(args[0].equalsIgnoreCase("save")) {
                sendMessage(sender, "Saving...");
                if(DataManager.saveAll()) {
                    sendMessage(sender, ChatImportant + "Saved!");
                } else {
                    sendMessage(sender, ChatError + "Failed to save data! Check console for errors.");
                }
                
                return true;
            }
            
            if(args[0].equalsIgnoreCase("debug")) {
                Base.DebugMode = !Base.DebugMode;
                debug("Debug Mode Enabled!");
                sendMessage(sender, "Turned Debug Mode " + ChatImportant + (Base.DebugMode ? "On" : "Off"));
                return true;
            }
            
            if(args[0].equalsIgnoreCase("server")) {
                List<String> messages = new ArrayList<String>();
                
                messages.add(ChatColor.LIGHT_PURPLE + "=== Server Information ====");
                messages.add(ChatColor.WHITE + "TPS: " + ServerTPSThread.getTPS());
                for(PluginHook plugin : PluginHook.getHookingPlugins()) {
                    messages.add(ChatColor.WHITE + plugin.getPluginName() + " hooked: " + (plugin.isHooked() ? "Yes" : "No"));
                }
                
                messages.add(ChatColor.WHITE + "Registered Players: " + DomsPlayer.getRegisteredPlayers().size());
                messages.add(ChatColor.WHITE + "Threads: " + DomsThread.getThreads().size());
                messages.add(ChatColor.WHITE + "Listeners: " + DomsListener.getListeners().size());
                messages.add(ChatColor.WHITE + "Commands: " + BukkitCommand.getCommands().size());
                messages.add(ChatColor.WHITE + "Known Items: " + DomsItem.NEXT_ID);
                messages.add(ChatColor.WHITE + "Classes: " + Bukkit.getServicesManager().getKnownServices().size());
                
                sendMessage(sender, messages);
                return true;
            }
            
            sender.sendMessage(ChatError + "Unknown Argument " + args[0] + ".");
            return true;
        }
        
        Base.sendMessage(sender, new String[] {
            ChatImportant + " = " + ChatDefault + getPlugin().getName() + ChatImportant + " = ",
            ChatDefault + "\tProgrammed By: " + ChatColor.LIGHT_PURPLE + "Dom",
            ChatDefault + "\tWebsite: " + ChatColor.GREEN + "http://domsplace.com/",
            ChatDefault + "\tTesters: " + ChatColor.GOLD + "Heste, Jordan, Jenae, Kyle"
        });
        return true;
    }
}
