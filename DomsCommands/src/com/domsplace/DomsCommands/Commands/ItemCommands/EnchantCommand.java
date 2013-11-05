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

package com.domsplace.DomsCommands.Commands.ItemCommands;

import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Dominic Masters
 */
public class EnchantCommand extends BukkitCommand {
    public EnchantCommand() {
        super("enchant");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, SubCommandOption.ENCHANTMENT_OPTION.getOption(), "level", "force"));
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.ENCHANTMENT_OPTION.getOption(), "level", "force"));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter an enchantment name.");
            return false;
        }
        
        if(args.length < 2 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return false;
        }
        
        DomsPlayer player = null;
        List<Enchantment> enchantment = new ArrayList<Enchantment>();
        int lvl = 1;
        boolean force = false;
        
        for(int i = 0; i < args.length; i++) {
            String s = args[i];
            
            Enchantment e = Enchantment.getByName(s.toUpperCase());
            if(e != null) {
                enchantment.add(e);
                continue;
            }
            
            if(isInt(s)) {
                lvl = getInt(s);
                continue;
            }
            
            if(s.equalsIgnoreCase("force")) {
                force = true;
                continue;
            }
            
            if(s.equalsIgnoreCase("all")) {
                for(Enchantment en : Enchantment.values()) {
                    enchantment.add(en);
                }
                continue;
            }
            
            DomsPlayer g = DomsPlayer.guessOnlinePlayer(sender, s);
            if(g != null && player == null) {
                player = g;
                continue;
            }
            
            sendMessage(sender, ChatError + s + " is an invalid command.");
            return true;
        }
        
        if(player == null) {
            player = DomsPlayer.getPlayer(sender);
        }
        
        if(!player.isOnline(sender) || player.isConsole()) {
            sendMessage(sender, ChatError + player.getDisplayName() + ChatError + " isn't online.");
            return true;
        }
        
        if(!hasPermission(sender, "DomsCommands.enchant.others") && !player.equals(DomsPlayer.getPlayer(sender))) {
            return this.noPermission(sender, cmd, label, args);
        }
        
        if(enchantment.size() < 1) {
            sendMessage(sender, ChatError + "Please enter a valid Enchantment name.");
            return true;
        }
        
        if(lvl < 0) {
            sendMessage(sender, ChatError + "Level must be at least 0");
            return true;
        }
        
        if(lvl > 32767) {
            sendMessage(sender, ChatError + "Level must be less than 32767.");
            return true;
        }
        
        ItemStack is = player.getOnlinePlayer().getItemInHand();
        if(is == null || is.getType() == null || is.getType().equals(Material.AIR)) {
            sendMessage(sender, ChatError + "Can't apply enchantment to air.");
            return true;
        }
        
        if(!force) {
            for(Enchantment e : enchantment) {
                if(e.getMaxLevel() >= lvl && e.canEnchantItem(is)) continue;
                sendMessage(sender, ChatError + "Can't put " + e.getName() + " on to the item. Try using the force argument.");
                return true;
            }
        }
        
        String msg = ChatDefault + "Added the enchantments ";
        if(lvl == 0) msg = ChatDefault + "Removed the enchantments ";
        
        for(int i = 0; i < enchantment.size(); i++) {
            Enchantment e = enchantment.get(i);
            if(lvl == 0) {
                is.removeEnchantment(e);
            } else if(force) {
                is.addUnsafeEnchantment(e, lvl);
            } else {
                try {is.addEnchantment(e, lvl);} catch(Exception ex) {
                    sendMessage(sender, ChatError + "Can't put " + e.getName() + " on to the item. Try using the force argument.");
                    return true;
                }
            }
            msg += ChatImportant + e.getName();
            if(i < (enchantment.size() - 3)) msg += ChatDefault + ", ";
            else if(i < (enchantment.size() - 2)) msg += ChatDefault + " and ";
        }
        
        player.getOnlinePlayer().setItemInHand(is);
        
        if(lvl != 0) msg += ChatDefault + " at lvl " + ChatImportant + lvl;
         msg += ChatDefault + ".";
        sendMessage(sender, msg);
        return true;
    }
}
