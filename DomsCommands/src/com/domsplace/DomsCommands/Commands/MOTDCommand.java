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
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class MOTDCommand extends BukkitCommand {
    public MOTDCommand() {
        super("motd");
        this.addSubCommandOption(new SubCommandOption("Message of the day"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length > 0) {
            if(!hasPermission(sender, "DomsCommands.motd.set")) return this.noPermission(sender, cmd, label, args);
            String message = Base.arrayToString(args);
            getConfig().set("messages.motd", message);
            sendMessage(sender, "The New Message of the day is: " + ChatImportant + colorise(message));
            DataManager.saveAll();
            return true;
        }
        
        Base.sendMessage(sender, new String[] {
            "Servers Message of the day:",
            Bukkit.getMotd()
        });
        return true;
    }
}
