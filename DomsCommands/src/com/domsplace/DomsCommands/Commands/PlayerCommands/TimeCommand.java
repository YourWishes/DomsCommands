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
    
import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.BukkitCommand;
import com.domsplace.DomsCommands.Enums.WeatherType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       17/10/2013
 */
public class TimeCommand extends BukkitCommand {
    public static final String[] TIME_COMMANDS = new String[]{
        "time",
        "day",
        "night",
        "morning",
        "midnight",
        "midday",
        "afternoon",
        "fd",
        "fixday"
    };
    
    private static final long[] TIMES = new long[] {
        -1,
        0,
        16000,
        23000,
        18000,
        6000,
        10000,
        0,
        0
    };
    
    public long getTime(String a) {
        int i = 0;
        for(String s : TIME_COMMANDS) {
            if(a.equalsIgnoreCase(s)) return TIMES[i];
            i++;
        }
        
        return -1;
    }
    
    public long getTime(int hours, int minutes) {
        double mins = (minutes/60)*100;
        hours -= 6;
        while(hours < 0) {
            hours = hours + 24;
        }
        
        while(hours > 24) {
            hours = hours - 24;
        }
        long l = hours * 1000;
        l += (long) mins;
        return l;
    }
    
    public TimeCommand() {
        super("time");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.TIME_OPTION, SubCommandOption.WORLD_OPTION));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        String c = label;
        String ts = "SET";
        World w = null;
        boolean allWorlds = false;
        
        if(args.length == 0 && label.equalsIgnoreCase("time")) {
            sendMessage(sender, "The Server time is " + ChatImportant + Base.getHumanDate(new Date()));
            return true;
        }
        
        if(args.length == 0 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a world name.");
            return false;
        }
        
        if(args.length == 1) {
            if(Bukkit.getWorld(args[0]) != null) {
                w = Bukkit.getWorld(args[0]);
            } else if(args[0].equalsIgnoreCase("all")) {
                allWorlds = true;
            } else {
                c = args[0];
            }
        } else if(args.length > 2) {
            if(args[0].equalsIgnoreCase("set")) {
                ts = "SET";
            } else if(args[0].equalsIgnoreCase("add")) {
                ts = "ADD";
            } else {
                sendMessage(sender, ChatError + args[0] + " isn't a valid time.");
                return false;
            }
            if(args[2].equalsIgnoreCase("all")) {
                allWorlds = true;
            } else {
                w = Bukkit.getWorld(args[2]);
            }
            c = args[1];
        } else if(args.length > 1) {
            if(Bukkit.getWorld(args[1]) != null) {
                w = Bukkit.getWorld(args[1]);
                c = args[0];
            } else if(args[1].equalsIgnoreCase("all")) {
                allWorlds = true;
                c = args[0];
            } else {
                if(args[0].equalsIgnoreCase("set")) {
                    ts = "SET";
                } else if(args[0].equalsIgnoreCase("add")) {
                    ts = "ADD";
                } else {
                    sendMessage(sender, ChatError + args[0] + " isn't a valid time.");
                    return false;
                }

                c = args[1];
            }
        }
        
        long t = -1;
        
        if(c.contains(":")) {
            String[] sp = c.split(":");
            if(sp.length != 2) {
                sendMessage(sender, ChatError + c + " isn't a valid clock time (xx:xx).");
                return true;
            }
            
            if(!isInt(sp[0])) {
                sendMessage(sender, ChatError + c + " isn't a valid clock hour");
                return true;
            }
            
            if(!isInt(sp[1])) {
                sendMessage(sender, ChatError + c + " isn't a valid clock minute");
                return true;
            }
            
            int h = getInt(sp[0]);
            int m = getInt(sp[1]);
            if(h < 0 || h > 24) {
                sendMessage(sender, ChatError + h + " must be between 0 and 24.");
                return true;
            }
            
            if(m < 0 || m > 60) {
                sendMessage(sender, ChatError + m + " must be between 0 and 60.");
                return true;
            }
            
            t = getTime(h, m);
        }
        
        if(t < 0) {
            String x = c.replaceAll("t", "");
            if(isLong(x)) {
                t = getLong(x);
            }
        }
        
        if(t < 0) {
            t = getTime(c);
        }
        
        if(t < 0) {
            sendMessage(sender, ChatError + c + " is an invalid time.");
            return true;
        }
        
        if(w == null && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Invalid World!");
            return true;
        }
        
        if(w == null && !allWorlds) {
            DomsPlayer player = DomsPlayer.getPlayer(sender);
            w = player.getLocation().getBukkitWorld();
        }
        
        List<World> worlds = new ArrayList<World>();
        if(allWorlds) {
            worlds.addAll(Bukkit.getWorlds());
        } else {
            worlds.add(w);
        }
        
        long v = t;
        
        for(World wr : worlds) {
            if(wr == null) continue;
            v = t;
            if(ts.equalsIgnoreCase("ADD")) {
                v = v + wr.getTime();
            }

            while(v < 0) {
                v = v + 24000;
            }

            while(v > 24000) {
                v = t - 24000;
            }
            
            if((c.equalsIgnoreCase("fd") || c.equalsIgnoreCase("fixday")) && hasPermission(sender, "DomsCommands.weather")) {
                WeatherType.SUN.applyTo(w);
            }
            
            if(ts.equalsIgnoreCase("ADD")) {
                sendMessage(sender, "Set time of " + ChatImportant + wr.getName() + ChatDefault + " to " + ChatImportant + v + " ticks" + ChatDefault + ".");
            }
            
            wr.setTime(v);
        }
        
        if(!allWorlds) {
            sendMessage(sender, "Set time of " + ChatImportant + w.getName() + ChatDefault + " to " + ChatImportant + t + " ticks" + ChatDefault + ".");
        } else if(allWorlds && !ts.equalsIgnoreCase("ADD")) {
            sendMessage(sender, "Set time to " + ChatImportant + t + " ticks" + ChatDefault + " in all worlds.");
        }
        return true;
    }
}
