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

package com.domsplace.DomsCommands.Commands.WarpCommands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import com.domsplace.DomsCommands.Objects.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class SetWarpCommand extends BukkitCommand {
    public SetWarpCommand() {
        super("setwarp");
        this.addSubCommandOption(new SubCommandOption("name"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a warp name.");
            return true;
        }
        
        if(args.length > 1) {
            sendMessage(sender, ChatError + "Warp name cannot contain spaces.");
            return true;
        }
        
        String name = args[0];
        if(!name.matches(Warp.WARP_NAME_REGEX)) {
            sendMessage(sender, ChatError + "Name contains an invalid symbol.");
            return true;
        }
        
        if(isInt(name)) {
            sendMessage(sender, ChatError + "Name cannot be a number.");
            return true;
        }
        
        //Check name isn't taken
        Warp w = Warp.getWarp(name);
        if(w != null) {
            sendMessage(sender, ChatError + "That warp name is taken.");
            return true;
        }
        
        //Create this as a warp
        w = new Warp(name, DomsPlayer.getPlayer(sender).getLocation());
        sendMessage(sender, "Set warp " + ChatImportant + w.getName() + ChatDefault + "!");
        return true;
    }
}
