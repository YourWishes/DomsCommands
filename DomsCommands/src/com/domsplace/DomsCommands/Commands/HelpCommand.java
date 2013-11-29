/*
 * Copyright 2013 Dominic Masters.
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
import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;

/**
 *
 * @author Dominic Masters
 */
public class HelpCommand extends BukkitCommand {
    private static final int HELP_PER_PAGE = 7;
    
    public HelpCommand() {
        super("help");
        this.addSubCommandOption(SubCommandOption.COMMAND_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(getConfig().getBoolean("commands.help.usecustom", false)) {
            return customHelp(sender, cmd, label, args);
        }
        
        return pluginHelp(sender, cmd, label, args);
    }
    
    public boolean customHelp(CommandSender sender, Command cmd, String label, String[] args) {
        int page = 1;
        if(args.length > 0) {
            if(!isInt(args[0])) {
                sendMessage(sender, ChatError + "Page must be a number.");
                return true;
            }
            
            page = getInt(args[0]);
        }
        
        String[] pages = DataManager.HELP_MANAGER.help;
        
        if(page > pages.length || page < 1) {
            sendMessage(sender, ChatError + "Page must be between 1 and " + pages.length);
            return true;
        }
        
        page = page - 1;
        sendMessage(sender, colorise(pages[page]).split("\n"));
        return true;
    }
    
    public boolean pluginHelp(CommandSender sender, Command cmd, String label, String[] args) {
        HelpMap helpMap = Bukkit.getHelpMap();
        int page = 1;
        
        if(args.length > 0) {
                String search = args[0];
            if(!isInt(search)) {
                HelpTopic topic = this.getTopic(search, sender);
                if(topic == null) {
                    sendMessage(sender, ChatError + "Help Topic not found.");
                    return true;
                }
                
                if(args.length > 1) {
                    if(!isInt(args[1])) {
                        sendMessage(sender, ChatError + "Page must be a number.");
                        return true;
                    }
                    
                    page = getInt(args[1]);
                }
                
                String[] lines = topic.getFullText(sender).split("\n");
                
                int pages =  ((int)Math.ceil((double)lines.length/(double)HelpCommand.HELP_PER_PAGE));
                
                if(page < 1 || page > pages) {
                    sendMessage(sender, ChatError + "Please enter a page between 1 and " + pages);
                    return true;
                }
                
                sendMessage(sender, ChatImportant + "Help - Page " + page + "/" + pages);
                page --;
                for(int i = (page * HelpCommand.HELP_PER_PAGE); i < ((page+1) * HelpCommand.HELP_PER_PAGE); i++) {
                    try {
                        String line = lines[i];
                        line = line.replaceAll("§e", ChatImportant);
                        line = line.replaceAll("§6", ChatImportant);
                        line = line.replaceAll("§r", ChatDefault);
                        line = line.replaceAll("§f", ChatDefault);
                        sendMessage(sender, line);
                    }catch(Exception e) {}
                }
                return true;
            }
            page = getInt(args[0]);
        }
        
        HelpTopic topic = this.getTopic("", sender);
        String[] lines = topic.getFullText(sender).split("\n");

        int pages =  ((int)Math.ceil((double)lines.length/(double)HelpCommand.HELP_PER_PAGE));

        if(page < 1 || page > pages) {
            sendMessage(sender, ChatError + "Please enter a page between 1 and " + pages);
            return true;
        }

        sendMessage(sender, ChatImportant + "Help - Page " + page + "/" + pages);
        page --;
        for(int i = (page * HelpCommand.HELP_PER_PAGE); i < ((page+1) * HelpCommand.HELP_PER_PAGE); i++) {
            try {
                String line = lines[i];
                line = line.replaceAll("§e", ChatImportant);
                line = line.replaceAll("§6", ChatImportant);
                line = line.replaceAll("§r", ChatDefault);
                line = line.replaceAll("§f", ChatDefault);
                sendMessage(sender, line);
            }catch(Exception e) {}
        }
        return true;
    }
    
    public HelpTopic getTopic(String topic, CommandSender sender) {
        HelpTopic t = null;
        for(HelpTopic top : Bukkit.getHelpMap().getHelpTopics()) {
            if(top == null) continue;
            if(!top.canSee(sender)) continue;
            if(!top.getName().equalsIgnoreCase(topic)) continue;
            return top;
        }
        for(HelpTopic top : Bukkit.getHelpMap().getHelpTopics()) {
            if(top == null) continue;
            if(!top.canSee(sender)) continue;
            if(!top.getName().toLowerCase().startsWith(topic.toLowerCase())) continue;
            return top;
        }
        for(HelpTopic top : Bukkit.getHelpMap().getHelpTopics()) {
            if(top == null) continue;
            if(!top.canSee(sender)) continue;
            if(!top.getName().toLowerCase().contains(topic.toLowerCase())) continue;
            return top;
        }
        return t;
    }
}
