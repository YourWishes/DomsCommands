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
import com.domsplace.DomsCommands.Exceptions.InvalidItemException;
import com.domsplace.DomsCommands.Objects.DomsItem;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

/**
 * @author      Dominic
 * @since       27/10/2013
 */
public class RecipeCommand extends BukkitCommand {
    public RecipeCommand() {
        super("recipe");
        this.addSubCommandOption(SubCommandOption.ITEM_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter an item name.");
            return true;
        }
        
        DomsItem item;
        if(args.length > 0) {
            try {
                item = DomsItem.guessItem(Base.arrayToString(args, " "));
            } catch(InvalidItemException e) {
                sendMessage(sender, ChatError + "Please enter a valid item.");
                return true;
            }
        } else {
            item = DomsItem.createItem(getPlayer(sender).getItemInHand());
        }
        
        if(item == null || item.isAir()) {
            sendMessage(sender, ChatError + "Please enter or be holding a valid item.");
            return true;
        }
        
        ItemStack is;
        try {
            is = item.getItemStack(1);
        } catch(InvalidItemException e) {
            sendMessage(sender, ChatError + "Please enter a valid item.");
            return true;
        }
        
        List<Recipe> recipes = Bukkit.getRecipesFor(is);
        if(recipes.size() < 1) {
            sendMessage(sender, ChatError + "There are no recipes for this item.");
            return true;
        }
        
        List<String> messages = new ArrayList<String>();
        messages.add(ChatImportant + "Showing " + recipes.size() + " recipes for " +  item.toHumanString().replaceAll(ChatDefault, ChatImportant));
        
        for(Recipe recipe : recipes) {
            if(recipe instanceof ShapedRecipe) {
                ShapedRecipe r = (ShapedRecipe) recipe;
                List<String> xes = new ArrayList<String>();
                for(String line : r.getShape()) {
                    char[] chars = line.toCharArray();
                    String x = "";
                    for(char c : chars) {
                        ItemStack it = r.getIngredientMap().get(c);
                        DomsItem itd = DomsItem.createItem(it);
                        x += "[" + Base.pad(Base.trim((itd == null ? " " : itd.toHumanString()), 16), 16).replaceAll(ChatDefault, ChatImportant) + ChatDefault + "]";
                    }
                    xes.add(x);
                }
                
                messages.add("Shape:\n" + Base.listToString(xes, "\n") + "\n§r");
                
                continue;
            }
            
            if(recipe instanceof ShapelessRecipe) {
                ShapelessRecipe r = (ShapelessRecipe) recipe;
                List<String> x = new ArrayList<String>();
                for(ItemStack it : r.getIngredientList()) {
                    List<String> d = DomsItem.getHumanMessages(DomsItem.itemStackToDomsItems(it));
                    x.addAll(d);
                }
                messages.add("Needed Ingredients (In any Spot):\n" + ChatImportant
                        + Base.listToString(x, ", ").replaceAll(ChatDefault, ChatImportant) + "\n§r");
            }
        }
        
        sendMessage(sender, messages);
        return true;
    }
}
