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

import static com.domsplace.DomsCommands.Bases.Base.ChatError;
import static com.domsplace.DomsCommands.Bases.Base.sendMessage;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import com.domsplace.DomsCommands.Objects.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class DeleteWarpCommand extends BukkitCommand {
    public DeleteWarpCommand() {
        super("deletewarp");
        this.addSubCommandOption(SubCommandOption.WARPS_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a warp name.");
            return false;
        }
        
        Warp w = Warp.getWarp(args[0]);
        if(w == null) {
            sendMessage(sender, ChatError + "Couldn't find a warp by that name.");
            return true;
        }
        
        String name = w.getName();
        w.deRegister();
        w = null;
        sendMessage(sender, "Deleted warp " + ChatImportant + name + ChatDefault + ".");
        return true;
    }
}
