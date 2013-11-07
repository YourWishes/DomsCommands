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

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsItem;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Dominic Masters
 */
public class AddLoresCommand extends BukkitCommand {
    public AddLoresCommand() {
        super("addlores");
        this.addSubCommandOption(new SubCommandOption("lore"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a lore name.");
            return false;
        }
        
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        
        ItemStack is = player.getOnlinePlayer().getItemInHand();
        if(is == null || 
                is.getType() == null || 
                is.getType().equals(Material.AIR) || 
                is.getItemMeta() == null) {
            sendMessage(sender, ChatError + "Cannot add lores to this item");
            return true;
        }
        
        String lore = Base.arrayToString(args,  " ");
        lore = Base.coloriseByPermission(lore, player, "DomsCommands.addlores.colors.");
        
        sendMessage(sender, "Added lore " + ChatImportant + lore + ChatDefault + " to " + ChatImportant + DomsItem.createItem(is).toHumanString() + ChatDefault + ".");
        
        ItemMeta im = is.getItemMeta();
        List<String> lores = im.getLore();
        if(lores == null) lores = new ArrayList<String>();
        lores.add(lore);
        im.setLore(lores);
        
        is.setItemMeta(im);
        return true;
    }
}
