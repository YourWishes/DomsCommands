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
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Dominic Masters
 */
public class RenameItemCommand extends BukkitCommand {
    public RenameItemCommand() {
        super("renameitem");
        this.addSubCommandOption(new SubCommandOption("name"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sendMessage(sender, ChatError + "Only players can do this.");
            return true;
        }
        
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter an item name.");
            return false;
        }
        
        String name = Base.arrayToString(args,  " ");
        DomsPlayer player = DomsPlayer.getPlayer(sender);
        
        ItemStack is = player.getOnlinePlayer().getItemInHand();
        if(is == null || is.getType() == null || is.getType().equals(Material.AIR) || is.getItemMeta() == null) {
            sendMessage(sender, ChatError + "Can't rename this item.");
            return true;
        }
        
        name = Base.coloriseByPermission(name, player, "DomsCommands.renameitem.colors.");
        
        DomsItem item = DomsItem.createItem(is);
        sendMessage(sender, "Renamed " + ChatImportant + item.toHumanString() + ChatDefault + " to " + ChatImportant + name + ChatDefault + ".");
        item.setName(name);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(item.getName());
        is.setItemMeta(im);
        player.getOnlinePlayer().setItemInHand(is);
        return true;
    }
}
