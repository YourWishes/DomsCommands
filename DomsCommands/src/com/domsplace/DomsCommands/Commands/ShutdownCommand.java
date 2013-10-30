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
import com.domsplace.DomsCommands.Events.ServerShutdownEvent;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class ShutdownCommand extends BukkitCommand {
    public ShutdownCommand() {
        super("shutdown");
        this.addSubCommandOption(new SubCommandOption("message"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        String message = Base.getConfig().getString("messages.shutdown.kickmessage", getPlugin().getServer().getShutdownMessage());
        if(args.length > 0) {
            message = Base.arrayToString(args);
        }
        
        ServerShutdownEvent event = new ServerShutdownEvent(sender, message);
        event.fireEvent();
        if(event.isCancelled()) return true;
        message = event.getMessage();
        
        log("Saving Players");
        getPlugin().getServer().savePlayers();
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(colorise(message));
        }
        
        for(World w : getPlugin().getServer().getWorlds()) {
            log("Saving world " + w.getName());
            w.save();
        }
        
        log("Shutting down...");
        getPlugin().getServer().shutdown();
        return true;
    }
}
