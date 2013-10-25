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
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class WarpCommand extends BukkitCommand {
    public WarpCommand() {
        super("warp");
        this.addSubCommandOption(SubCommandOption.WARPS_OPTION);
    }
    
    private void sendList(CommandSender sender, int page) {        
        //Determine pages
        List<Warp> warps = Warp.getWarpsAlphabetically();
        int pages = (int) Math.ceil(((double) warps.size()) / 15d);
        if(page > pages) {
            sendMessage(sender, ChatError + "No warps found.");
            return;
        }
        
        if(page < 1) {
            sendMessage(sender, ChatError + "Please enter a page between 1 and " + pages);
            return;
        }
        
        String l = "";
        
        
        
        for(int i = 0; i < 15; i++) {
            try {
                Warp w = warps.get(i + ((page-1)*15));
                l += w.getName() + ", ";
            } catch(Exception e) {continue;}
        }
        
        sendMessage(sender, new String[] {
            ChatImportant + "Warps. Page " + page + " of " + pages,
            l
        });
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        if(args.length < 1) {
            sendList(sender, 1);
            return true;
        }
        
        String a = args[0];
        if(isInt(a)) {
            sendList(sender, getInt(a));
            return true;
        }
        
        Warp w = Warp.getWarp(a);
        if(w == null) {
            sendMessage(sender, ChatError + "Couldn't find warp by that name. For a list type /warps");
            return true;
        }
        
        if(!w.getLocation().isWorldLoaded()) {
            sendMessage(sender, ChatError + "The target world is no longer available.");
            return true;
        }
        
        DomsPlayer plyr = DomsPlayer.getPlayer(sender);
        plyr.setBackLocation(plyr.getLocation());
        plyr.teleport(w.getLocation());
        sendMessage(sender, ChatDefault + "Warping to " + ChatImportant + w.getName() + ChatDefault + "!");
        return true;
    }
}
