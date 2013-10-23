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

package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Commands.PlayerCommands.GamemodeCommand;
import com.domsplace.DomsCommands.Commands.PlayerCommands.TimeCommand;
import com.domsplace.DomsCommands.Commands.PlayerCommands.WeatherCommand;
import com.domsplace.DomsCommands.Exceptions.InvalidItemException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

/**
 * @author      Dominic
 * @since       05/10/2013
 */
public class SubCommandOption extends Base {
    public static final SubCommandOption PLAYERS_OPTION = new SubCommandOption("[PLAYER]");
    public static final SubCommandOption HOMES_OPTION = new SubCommandOption("[HOME]");
    public static final SubCommandOption WORLD_OPTION = new SubCommandOption("[WORLD]");
    public static final SubCommandOption ENCHANTMENT_OPTION = new SubCommandOption("[ENCHANTS]");
    public static final SubCommandOption MOB_OPTION = new SubCommandOption("[MOB]");
    public static final SubCommandOption ITEM_OPTION = new SubCommandOption("[ITEM]");
    public static final SubCommandOption POTION_OPTION = new SubCommandOption("[POTION]");
    public static final SubCommandOption WEATHER_OPTION = new SubCommandOption("[WEATHER]");
    public static final SubCommandOption TIME_OPTION = new SubCommandOption("[TIME]");
    public static final SubCommandOption WARPS_OPTION = new SubCommandOption("[WARP]");
    public static final SubCommandOption GAMEMODE_OPTION = new SubCommandOption("[GAMEMODE]");
    
    //Instance
    private String option;
    private List<SubCommandOption> subOptions;
    
    public SubCommandOption(String option) {
        this.option = option;
        this.subOptions = new ArrayList<SubCommandOption>();
    }
    
    public SubCommandOption(String option, SubCommandOption... options) {
        this(option);
        for(SubCommandOption o : options) {
            this.subOptions.add(o);
        }
    }
    
    public SubCommandOption(String option, String... options) {
        this(option);
        for(String s : options) {
            this.subOptions.add(new SubCommandOption(s));
        }
    }
    
    public SubCommandOption(SubCommandOption option, SubCommandOption... options) {
        this(option.option, options);
    }
    
    public SubCommandOption(SubCommandOption option, String... options) {
        this(option.option, options);
    }
    
    public String getOption() {return this.option;}
    public List<SubCommandOption> getSubCommandOptions() {return new ArrayList<SubCommandOption>(this.subOptions);}

    public List<String> getOptionsFormatted(CommandSender sender) {
        List<String> returnV = new ArrayList<String>();
        if(this.compare(SubCommandOption.PLAYERS_OPTION)) {
            for(DomsPlayer p : DomsPlayer.getOnlinePlayers(sender)) {
                returnV.add(p.getPlayer());
            }
        } else if(this.compare(SubCommandOption.HOMES_OPTION)) {
            for(Home home : DomsPlayer.getPlayer(sender).getHomes()) {
                returnV.add(home.getName());
            }
        } else if(this.compare(SubCommandOption.ENCHANTMENT_OPTION)) {
            for(Enchantment e : Enchantment.values()) {
                returnV.add(e.getName());
            }
        } else if(this.compare(SubCommandOption.WORLD_OPTION)) {
            for(World w : Bukkit.getWorlds()) {
                returnV.add(w.getName());
            }
        } else if(this.compare(SubCommandOption.MOB_OPTION)) {
            for(EntityType et : EntityType.values()) {
                if(!et.isAlive()) continue;
                returnV.add(et.getName());
            }
        } else if(this.compare(SubCommandOption.ITEM_OPTION)) {
            for(Material m : Material.values()) {
                if(m.equals(Material.AIR)) continue;
                returnV.add(m.name());
            }
        } else if(this.compare(SubCommandOption.POTION_OPTION)) {
            for(PotionEffectType pet : PotionEffectType.values()) {
                returnV.add(pet.getName());
            }
        } else if(this.compare(SubCommandOption.WEATHER_OPTION)) {
            for(String s : WeatherCommand.RAIN_COMMANDS) {
                returnV.add(s);
            }
            for(String s : WeatherCommand.SUN_COMMANDS) {
                returnV.add(s);
            }
            for(String s : WeatherCommand.STORM_COMMANDS) {
                returnV.add(s);
            }
            for(String s : WeatherCommand.TOGGLE_COMMANDS) {
                returnV.add(s);
            }
        } else if(this.compare(SubCommandOption.TIME_OPTION)) {
            for(String s : TimeCommand.TIME_COMMANDS) {
                returnV.add(s);
            }
        } else if(this.compare(SubCommandOption.WARPS_OPTION)) {
            for(Warp w : Warp.getWarpsAlphabetically()) {
                returnV.add(w.getName());
            }
        } else if(this.compare(SubCommandOption.GAMEMODE_OPTION)) {
            for(String s : GamemodeCommand.adventureCommands) {
                returnV.add(s);
            }
            for(String s : GamemodeCommand.survivalCommands) {
                returnV.add(s);
            }
            for(String s : GamemodeCommand.creativeCommands) {
                returnV.add(s);
            }
            for(String s : GamemodeCommand.toggleCommands) {
                returnV.add(s);
            }
        } else {
            returnV.add(this.option);
        }
        return returnV;
    }
    
    public static String reverse(String s, CommandSender sender) {
        if(Bukkit.getPlayer(s) != null) return SubCommandOption.PLAYERS_OPTION.option;
        if(Bukkit.getWorld(s) != null) return SubCommandOption.WORLD_OPTION.option;
        if(Enchantment.getByName(s) != null) return SubCommandOption.ENCHANTMENT_OPTION.option;
        if(EntityType.fromName(s) != null) return SubCommandOption.ENCHANTMENT_OPTION.option;
        if(PotionEffectType.getByName(s) != null) return SubCommandOption.POTION_OPTION.option;
        if(WeatherCommand.getType(s) != null) return SubCommandOption.WEATHER_OPTION.option;
        if(Warp.getWarp(s) != null) return SubCommandOption.WARPS_OPTION.option;
        if(GamemodeCommand.getGameMode(s) != null) return SubCommandOption.GAMEMODE_OPTION.option;
        try {if(DomsItem.guessItem(s) != null) return SubCommandOption.ITEM_OPTION.option;} catch(InvalidItemException e){}
        return s;
    }
    
    public List<String> getOptionsAsStringList(CommandSender sender) {
        List<String> returnV = new ArrayList<String>();
        for(SubCommandOption sc : this.subOptions) {
            returnV.addAll(sc.getOptionsFormatted(sender));
        }
        
        return returnV;
    }
    
    public List<String> tryFetch(String[] args, int lvl, CommandSender sender) {
        List<String> opts = new ArrayList<String>();
        
        lvl = lvl + 1;
        int targetLevel = args.length;
        
        if(targetLevel > lvl) {
            for(SubCommandOption sco : this.subOptions) {
                String s = args[lvl-1].toLowerCase();
                s = reverse(s, sender);
                if(!sco.getOption().toLowerCase().startsWith(s.toLowerCase())) continue;
                opts.addAll(sco.tryFetch(args, lvl, sender));
            }
        } else {
            return this.getOptionsAsStringList(sender);
        }
        
        return opts;
    }
    
    public boolean compare(SubCommandOption option) {
        if(option.getOption().equalsIgnoreCase(this.getOption())) return true;
        return false;
    }
}
