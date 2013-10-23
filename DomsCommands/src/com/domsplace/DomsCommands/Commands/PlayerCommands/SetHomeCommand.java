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

package com.domsplace.DomsCommands.Commands.PlayerCommands;
    
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Home;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       17/10/2013
 */
public class SetHomeCommand extends BukkitCommand {
    public SetHomeCommand() {
        super("sethome");
        this.addSubCommandOption(new SubCommandOption("home"));
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
        
        if(!name.matches(Home.HOME_NAME_REGEX)) {
            sendMessage(sender, ChatError + "Home name is invalid.");
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        
        Home old = player.getHome(name);
        
        if(old != null && !player.hasPermisson("DomsCommands.sethome.multiple") && !name.equalsIgnoreCase("home")) {
            sendMessage(sender, ChatError + "You've set your max homes, use /sethome to override.");
            return true;
        }
        
        Home h = new Home(name, player.getLocation(), player);
        player.removeHome(old);
        player.addHome(h);
        sendMessage(sender, ChatDefault + "Set Home \"" + ChatImportant + name + ChatDefault + "\"!");
        return true;
    }
}
