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
import com.domsplace.DomsCommands.Bases.DataManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       12/10/2013
 */
public class RulesCommand extends BukkitCommand {
    private static final int RULES_PER_PAGE = 9;
    
    public RulesCommand() {
        super("rules");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> rules = DataManager.RULES_MANAGER.rules;
        
        List<String> messages = new ArrayList<String>();
        int page = 0;
        int pages = (int) Math.ceil((double)rules.size() / (double)RULES_PER_PAGE);
        
        if(args.length > 0) {
            if(!isInt(args[0])) {
                sendMessage(sender, ChatError + "Page must be a number!");
                return true;
            }
            
            page = getInt(args[0])-1;
            if((page < 0) || page >= pages) {
                sendMessage(sender, ChatError + "Enter a number between 0 and " + pages+1);
                return true;
            }
        }
        
        messages.add(ChatImportant + "Rules! Page (" + ChatDefault + (page+1) + ChatImportant + "/" + ChatDefault + pages + ChatImportant + ")");
        for(int i = (page*RULES_PER_PAGE); i < (page+1)*RULES_PER_PAGE; i++) {
            try {messages.add(colorise(rules.get(i)));}catch(Exception e) {}
        }
        
        sendMessage(sender, messages);
        return true;
    }
}
