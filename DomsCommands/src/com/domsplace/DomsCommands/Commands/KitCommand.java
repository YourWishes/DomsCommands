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
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Kit;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       26/10/2013
 */
public class KitCommand extends BukkitCommand {
    public KitCommand() {
        super("kit");
        this.addSubCommandOption(SubCommandOption.KIT_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can run this command.");
            return true;
        }
        
        if(args.length < 1 || label.equalsIgnoreCase("kits") || label.equalsIgnoreCase("getkits")) {
            List<Kit> kits = Kit.getKits(sender);
            List<String> msgs = new ArrayList<String>();
            msgs.add(ChatImportant + "Kits you can obtain:");
            String s = "";
            for(int i = 0; i < kits.size(); i++) {
                s += kits.get(i).getName();
                if(i < kits.size() - 1) s += ", ";
            }
            msgs.add(s);
            sendMessage(sender, msgs);
            return true;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        Kit k = Kit.getKit(args[0]);
        if(k == null) {
            sendMessage(sender, ChatError + "No kit by that name.");
            return true;
        }
        
        if(!player.hasPermisson(k.getPermission())) {
            sendMessage(sender, ChatError + "You don't have permission to get this kit.");
            return true;
        }
        
        long cooldown = player.getKitCooldown(k);
        if(cooldown > 0 && !player.hasPermisson("DomsCommands.kit.cooldown")) {
            long now = getNow();
            long diff = now - cooldown;
            long diff2 = now - (cooldown + (k.getCooldown()*1000));
            if(diff2 < 0) {
                sendMessage(sender, ChatError + "You have to wait " + getTimeDifference(cooldown + k.getCooldown() * 1000) + " to get this kit again.");
                return true;
            }
        }
        
        player.setKitCooldown(k, getNow());
        player.addItems(k.getItems());
        sendMessage(sender, "Giving you kit " + ChatImportant + k.getName() + ChatDefault + ".");
        return true;
    }
}
