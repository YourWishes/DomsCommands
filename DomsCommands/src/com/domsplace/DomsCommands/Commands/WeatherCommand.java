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
import com.domsplace.DomsCommands.Enums.WeatherType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       21/10/2013
 */
public class WeatherCommand extends BukkitCommand {
    public static final String[] TOGGLE_COMMANDS = new String[] {
        "toggledownfall",
        "weather",
        "toggledownfall",
        "toggledown",
        "togglerain",
        "togglestorm",
        "toggledf"
    };
    
    public static final String[] SUN_COMMANDS = new String[] {
        "sun",
        "clear",
        "fine",
        "day",
        "sunny",
        "fair",
        "glare",
        "sunshine"
    };
    
    public static final String[] RAIN_COMMANDS = new String[] {
        "rain",
        "foggy",
        "overcast",
        "fog",
        "downfall",
        "downf"
    };
    
    public static final String[] STORM_COMMANDS = new String[] {
        "storm",
        "lightning",
        "thunder",
        "storming"
    };
    
    public static WeatherType getType(String t) {
        for(String s : SUN_COMMANDS) {
            if(s.equalsIgnoreCase(t)) return WeatherType.SUN;
        }
        for(String s : RAIN_COMMANDS) {
            if(s.equalsIgnoreCase(t)) return WeatherType.RAIN;
        }
        for(String s : STORM_COMMANDS) {
            if(s.equalsIgnoreCase(t)) return WeatherType.STORM;
        }
        for(String s : TOGGLE_COMMANDS) {
            if(s.equalsIgnoreCase(t)) return WeatherType.TOGGLE;
        }
        
        return null;
    }
    
    public WeatherCommand() {
        super("weather");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.WEATHER_OPTION, SubCommandOption.WORLD_OPTION));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        String c = label;
        List<World> w = new ArrayList<World>();
        int duration = (300 + new Random().nextInt(600)) * 20;
        
        for(int i = 0; i < args.length; i++) {
            if(Bukkit.getWorld(args[i]) != null) {
                w.add(Bukkit.getWorld(args[i]));
            } else if(args[i].equalsIgnoreCase("all")) {
                w.addAll(Bukkit.getWorlds());
            } else if(isInt(args[i])) {
                duration = getInt(args[i]);
            } else {
                c = args[i];
            }
        }
        
        if(w.size() < 1 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a world name.");
            return false;
        }
        
        if(w.size() < 1) {
            World wr = DomsPlayer.getPlayer(sender).getLocation().getBukkitWorld();
            w.add(wr);
        }
        
        WeatherType type = getType(c);
        if(type == null) {
            sendMessage(sender, ChatError + "Invlaid weather type.");
            return true;
        }
        
        String message = "";
        if(type.equals(WeatherType.TOGGLE)) {
            message = ChatDefault + "Toggled Weather in world";
            if(w.size() > 1) message += "s";
            message += " ";
        } else {
            message = ChatDefault + "Set weather to " + ChatImportant + type.getName() + ChatDefault + " in world";
            if(w.size() > 1) message += "s";
            message += " ";
        }
        
        for(int i = 0; i < w.size(); i++) {
            World wr = w.get(i);
            type.applyTo(wr, duration);
            message += ChatImportant + wr.getName();
            if(i < (w.size()-2)) message += ChatDefault + ", ";
            if(i == (w.size() - 2)) message += ChatDefault + " and ";
        }
        
        sendMessage(sender, message);
        return true;
    }
}
