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
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Dominic Masters
 */
public class HelpCommand extends BukkitCommand {
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
        return true;
    }
    
    public boolean pluginHelp(CommandSender sender, Command cmd, String label, String[] args) {
        return true;
    }
}
