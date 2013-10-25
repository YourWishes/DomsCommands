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

package com.domsplace.DomsCommands.Commands.PlayerCommands.HomeCommands;
    
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Home;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       24/10/2013
 */
public class HomeCommand extends BukkitCommand {
    public HomeCommand() {
        super("home");
        this.addSubCommandOption(SubCommandOption.HOMES_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        String name = "home";
        if(args.length > 0) {
            name = args[0];
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        
        Home home = player.getHome(name);
        if(home == null) {
            sendMessage(sender, ChatError + "You don't have \"" + name + "\" set.");
            return true;
        }
        
        if(!home.getLocation().isWorldLoaded()) {
            sendMessage(sender, ChatError + "This world is no longer available.");
            return true;
        }
        
        player.setBackLocation(player.getLocation());
        player.teleport(home.getLocation());
        sendMessage(sender, "Going to \"" + ChatImportant + name + ChatDefault + "\".");
        return true;
    }
}
