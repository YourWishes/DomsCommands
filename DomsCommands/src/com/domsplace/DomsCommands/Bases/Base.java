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

package com.domsplace.DomsCommands.Bases;

import com.domsplace.DomsCommands.DataManagers.ConfigManager;
import com.domsplace.DomsCommands.DomsCommandsPlugin;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class Base extends RawBase {
    public static final String TAB = "    ";
    
    public static boolean DebugMode = false;
    public static DomsCommandsPlugin plugin;
    
    public static String ChatDefault = ChatColor.GRAY.toString();
    public static String ChatImportant = ChatColor.BLUE.toString();
    public static String ChatError = ChatColor.RED.toString();
    
    private static String permissionMessage = "&4You don't have permission to do this!";
    
    //HOOKING OPTIONS

    //String Utils    
    public static String getDebugPrefix() {
        return ChatColor.LIGHT_PURPLE + "DEBUG: " + ChatColor.AQUA;
    }
    
    public static String colorise(Object o) {
        String msg = o.toString();
        
        String[] andCodes = {"&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", 
            "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "&l", "&o", "&n", 
            "&m", "&k", "&r"};
        
        String[] altCodes = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", 
            "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§l", "§o", "§n", 
            "§m", "§k", "§r"};
        
        for(int x = 0; x < andCodes.length; x++) {
            msg = msg.replaceAll(andCodes[x], altCodes[x]);
        }
        
        return msg;
    }
    
    public static String coloriseByPermission(Object message, DomsPlayer player, String permissionPrefix) {
        String msg = message.toString();
        
        
        
        return msg;
    }
    
    public static String getPermissionMessage() {
        return Base.permissionMessage;
    }
    
    public static void setPermissionMessage(String msg) {
        Base.permissionMessage = msg;
    }

    public static String[] listToArray(List<String> c) {
        String[] s = new String[c.size()];
        for(int i = 0; i < c.size(); i++) {
            s[i] = c.get(i);
        }
        
        return s;
    }
    
    public static String capitalizeFirstLetter(String s) {
        if(s.length() < 2) return s.toUpperCase();
        String end = s.substring(1, s.length());
        return s.substring(0, 1).toUpperCase() + end;
    }

    public static String capitalizeEachWord(String toLowerCase) {
        String[] words = toLowerCase.split(" ");
        String w = "";
        for(int i = 0; i < words.length; i++) {
            w += capitalizeFirstLetter(words[i]);
            if(i < (words.length-1)) {
                w += " ";
            }
        }
        return w;
    }
    
    public static String arrayToString(Object[] array) {
        return Base.arrayToString(array, " ");
    }
    
    public static String arrayToString(Object[] array, String seperator) {
        String m = "";
        for(int i = 0; i < array.length; i++) {
            m += array[i].toString();
            if(i < (array.length - 1)) {
                m += seperator;
            }
        }
        
        return m;
    }
    
    public static String trim(String s, int length) {
        if(s.length() < length) return s;
        return s.substring(0, length);
    }
    
    //Messaging Utils
    public static void sendMessage(CommandSender sender, String msg) {
        if(msg.replaceAll(" ", "").equalsIgnoreCase("")) return;
        msg = msg.replaceAll("\\t", TAB);
        sender.sendMessage(ChatDefault + msg);
    }

    public static void sendMessage(CommandSender sender, String msg, Object... objs) {
        String s = msg;
        for(int i = 0; i < objs.length; i++) {
            s = s.replaceAll("{" + i + "}", objs[i].toString());
        }
        sendMessage(sender, s);
    }
    
    public static void sendMessage(CommandSender sender, Object[] msg) {
        for(Object o : msg) {
            sendMessage(sender, o);
        }
    }

    public static void sendMessage(CommandSender sender, List<?> msg) {
        sendMessage(sender, msg.toArray());
    }

    public static void sendMessage(CommandSender sender, Object msg) {
        if(msg == null) return;
        if(msg instanceof String) {
            sendMessage(sender, (String) msg);
            return;
        }
        
        if(msg instanceof Object[]) {
            sendMessage(sender, (Object[]) msg);
            return;
        }
        
        if(msg instanceof List<?>) {
            sendMessage(sender, (List<?>) msg);
            return;
        }
        sendMessage(sender, msg.toString());
    }

    public static void sendMessage(Player sender, Object... msg) {
        sendMessage((CommandSender) sender, msg);
    }

    public static void sendMessage(OfflinePlayer sender, Object... msg) {
        if(!sender.isOnline()) return;
        sendMessage(sender.getPlayer(), msg);
    }

    public static void sendMessage(Entity sender, Object... msg) {
        if(!(sender instanceof CommandSender)) return;
        sendMessage(sender, msg);
    }

    public static void sendMessage(DomsPlayer sender, Object... msg) {
        sendMessage(sender.getOfflinePlayer(), msg);
    }
    
    public static void sendMessage(Object o) {
        sendMessage(Bukkit.getConsoleSender(), o);
    }
    
    public static void sendAll(List<Player> players, Object o) {
        for(Player p : players) {
            sendMessage(p, o);
        }
    }
    
    public static void sendAll(Player[] players, Object o) {
        for(Player p : players) {
            sendMessage(p, o);
        }
    }
    
    public static void sendAll(Object o) {
        sendAll(Bukkit.getOnlinePlayers(), o);
    }
    
    public static void sendAll(String permission, Object o) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!hasPermission((CommandSender) p, permission)) continue;
            sendMessage(p, o);
        }
    }
    
    public static void broadcast(Object o) {
        sendMessage(o);
        sendAll(o);
    }
    
    public static void broadcast(String permission, Object o) {
        sendMessage(o);
        sendAll(permission, o);
    }
    
    public static void debug(Object o) {
        if(!DebugMode) return;
        broadcast(getDebugPrefix() + o.toString());
    }
    
    public static void error(String message, boolean postfix) {
        String msg = ChatError + "Error: " + ChatColor.DARK_RED + message;
        if(postfix && DebugMode) msg += ChatColor.YELLOW + " Caused by: ";
        if(postfix && !DebugMode) msg += ChatColor.YELLOW + " Turn debug mode on to view whole error.";
        sendMessage(msg);
    }
    
    public static void error(String message) {
        error(message, false);
    }
    
    public static void error(String message, Exception e) {
        error(message, true);
        if(!DebugMode) return;
        String lines = "\n" + e.getClass().getName() + ":  " +  e.getMessage();
        for(StackTraceElement ste : e.getStackTrace()) {
            
            lines += "\t" + ChatColor.GRAY + "at " + ste.getClassName() + "." 
                    + ste.getMethodName() + "(" + ste.getFileName() + ":" + 
                    ste.getLineNumber() + ")\n";
        }
        
        sendMessage(lines);
    }
    
    public static void log(Object o) {
        getPlugin().getLogger().info(o.toString());
    }
    
    //Conversion Utils
    public static boolean isPlayer(Object o) {
        return o instanceof Player;
    }
    
    public static Player getPlayer(Object o) {
        return (Player) o;
    }
    
    public static Player getPlayer(CommandSender sender, String argument) {
        Player p = null;
        for(Player plyr : Bukkit.getOnlinePlayers()) {
            if(!canSee(sender, plyr)) continue;
            if(plyr.getName().toLowerCase().contains(argument.toLowerCase())) {
                p = plyr;
                break;
            }
        }
        
        if(p == null) {
            for(Player plyr : Bukkit.getOnlinePlayers()) {
                if(!canSee(sender, plyr)) continue;
                if(plyr.getDisplayName().toLowerCase().contains(argument.toLowerCase())) {
                    p = plyr;
                    break;
                }
            }
        }
        return p;
    }
    
    public static OfflinePlayer getOfflinePlayer(Player player) {
        return Bukkit.getOfflinePlayer(player.getName());
    }
    
    public static OfflinePlayer getOfflinePlayer(String player) {
        return Bukkit.getOfflinePlayer(player);
    }
    
    public static OfflinePlayer getOfflinePlayer(CommandSender relative, String player) {
        OfflinePlayer p = Base.getPlayer(relative, player);
        if(p == null || !p.isOnline()) {
            p = Bukkit.getOfflinePlayer(player);
        }
        return p;
    }
    
    public static boolean isInt(Object o) {
        try {
            Integer.parseInt(o.toString());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static int getInt(Object o) {
        return Integer.parseInt(o.toString());
    }
    
    public static double getDouble(Object o) {
        return Double.parseDouble(o.toString());
    }
    
    public static boolean isDouble(Object o) {
        try {
            Double.parseDouble(o.toString());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static boolean isByte(Object o) {
        try {
            Byte.parseByte(o.toString());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static byte getByte(Object o) {
        return Byte.parseByte(o.toString());
    }
    
    public static String listToString(List<? extends Object> strings) {
        return listToString(strings, ", ");
    }
    
    public static String listToString(List<? extends Object> strings, String seperator) {
        String m = "";
        
        for(int i = 0; i < strings.size(); i++) {
            m += strings.get(i).toString();
            if(i < (strings.size() - 1)) m += seperator;
        }
        
        return m;
    }
    
    //Plugin Utils
    public static void setPlugin(DomsCommandsPlugin plugin) {
        Base.plugin = plugin;
    }
    
    public static DomsCommandsPlugin getPlugin() {
        return plugin;
    }
    
    public static File getDataFolder() {
        return getPlugin().getDataFolder();
    }
    
    public static YamlConfiguration getConfig() {
        return getConfigManager().getCFG();
    }
    
    public static ConfigManager getConfigManager() {
        return DataManager.CONFIG_MANAGER;
    }
    
    //Location Utils
    public static String getLocationString(Location location) {
        return location.getX() + ", " + location.getY() + ", " + location.getZ()
                + " " + location.getWorld().getName();
    }
    
    public static String getStringLocation (Chunk chunk) {
        return chunk.getX() + ", " + chunk.getZ() + " : " + chunk.getWorld().getName();
    }
    
    public static boolean isCoordBetweenCoords(int checkX, int checkZ, int outerX, int outerZ, int maxX, int maxZ) {
        if(checkX >= Math.min(outerX, maxX) && checkX <= Math.max(outerX, maxX)) {
            if(checkZ >= Math.min(outerZ, maxZ) && checkZ <= Math.max(outerZ, maxZ)) { return true; }
        }
        return false;
    }
    
    //Player Utils
    public static boolean hasPermission(CommandSender sender, String permission) {
        if(permission.equals("DomsCommands.none")) return true;
        return sender.hasPermission(permission);
    }
    
    public static boolean hasPermission(Player player, String permission) {return hasPermission((CommandSender) player, permission);}
    
    public static boolean hasPermission(OfflinePlayer player, String permission) {
        if(!player.isOnline()) return false;
        return hasPermission((CommandSender) player.getPlayer(), permission);
    }
    
    public static boolean canSee(CommandSender p, OfflinePlayer target) {
        if(!isPlayer(p)) return true;
        if(!target.isOnline()) return true;
        return getPlayer(p).canSee(target.getPlayer());
    }
    
    public static boolean canSee(OfflinePlayer player, OfflinePlayer target) {
        if(!player.isOnline()) return player.isOp();
        return canSee((CommandSender) getPlayer(player), target);
    }
    
    public static boolean isVisible(OfflinePlayer t) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getName().equals(t.getName())) continue;
            if(!canSee((CommandSender) p, t)) return false;
        }
        return true;
    }
    
    public static List<OfflinePlayer> getPlayersList() {
        List<OfflinePlayer> rv = new ArrayList<OfflinePlayer>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!isVisible(p)) continue;
            rv.add(Bukkit.getOfflinePlayer(p.getName()));
        }
        return rv;
    }
    
    //Time Utils
    public static long getNow() {
        return System.currentTimeMillis();
    }
    public static boolean isValidTime(String input) {
        String[] names = new String[]{
            ("year"),
            ("years"),
            ("month"),
            ("months"),
            ("day"),
            ("days"),
            ("hour"),
            ("hours"),
            ("minute"),
            ("minutes"),
            ("second"),
            ("seconds")
        };
        
        Pattern timePattern = Pattern.compile(
        "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
        
        Matcher m = timePattern.matcher(input);
        
        while(m.find()) {
            	if (m.group() == null || m.group().isEmpty()) {
                    continue;
                }
                for (int i = 0; i < m.groupCount(); i++) {
                    if (m.group(i) != null && !m.group(i).isEmpty()) {
                        return true;
                    }
                }
        }
        
        return false;
    }
    
    public static String getTimeDifference(Date late) {return Base.getTimeDifference(new Date(), late);}
    
    public static String getTimeDifference(Date early, Date late) {
        Long NowInMilli = late.getTime();
        Long TargetInMilli = early.getTime();
        Long diffInSeconds = (NowInMilli - TargetInMilli) / 1000;

        long diff[] = new long[] {0,0,0,0,0};
        /* sec */diff[4] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        /* min */diff[3] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        /* hours */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        /* days */diff[1] = (diffInSeconds = (diffInSeconds / 24)) >= 31 ? diffInSeconds % 31: diffInSeconds;
        /* months */diff[0] = (diffInSeconds = (diffInSeconds / 31));
        
        String message = "";
        
        if(diff[0] > 0) {
            message += diff[0] + " month";
            if(diff[0] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[1] > 0) {
            message += diff[1] + " day";
            if(diff[1] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[2] > 0) {
            message += diff[2] + " hour";
            if(diff[2] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[3] > 0) {
            message += diff[3] + " minute";
            if(diff[3] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[4] > 0) {
            message += diff[4] + " second";
            if(diff[4] > 1) {
                message += "s";
            }
            return message;
        }
        
        return "Time Error";
    }
    
    public static Date addStringToNow(String input) {
        boolean found = false;
        
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        Date now = new Date();
        String[] names = new String[]{
            ("year"),
            ("years"),
            ("month"),
            ("months"),
            ("day"),
            ("days"),
            ("hour"),
            ("hours"),
            ("minute"),
            ("minutes"),
            ("second"),
            ("seconds")
        };
        
        Pattern timePattern = Pattern.compile(
        "(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
        
        Matcher m = timePattern.matcher(input);
        
        while(m.find()) {
            	if (m.group() == null || m.group().isEmpty()) {
                    continue;
                }
                for (int i = 0; i < m.groupCount(); i++) {
                    if (m.group(i) != null && !m.group(i).isEmpty()) {
                        found = true;
                    }
                    if(found) {
                        if (m.group(1) != null && !m.group(1).isEmpty()) {
                            years = Integer.parseInt(m.group(1));
                        }
                        if (m.group(2) != null && !m.group(2).isEmpty()) {
                            months = Integer.parseInt(m.group(2));
                        }
                        if (m.group(3) != null && !m.group(3).isEmpty()) {
                            weeks = Integer.parseInt(m.group(3));
                        }
                        if (m.group(4) != null && !m.group(4).isEmpty()) {
                            days = Integer.parseInt(m.group(4));
                        }
                        if (m.group(5) != null && !m.group(5).isEmpty()) {
                            hours = Integer.parseInt(m.group(5));
                        }
                        if (m.group(6) != null && !m.group(6).isEmpty()) {
                            minutes = Integer.parseInt(m.group(6));
                        }
                        if (m.group(7) != null && !m.group(7).isEmpty()) {
                            seconds = Integer.parseInt(m.group(7));
                        }
                        break;
                    }
                }
        }
        
        Calendar c = Calendar.getInstance();
        if (years > 0) {
            c.add(Calendar.YEAR, years);
        }
        if (months > 0)  {
            c.add(Calendar.MONTH, months);
        }
        if (weeks > 0) {
            c.add(Calendar.WEEK_OF_YEAR, weeks);
        }
        if (days > 0) {
            c.add(Calendar.DAY_OF_MONTH, days);
        }
        if (hours > 0) {
            c.add(Calendar.HOUR_OF_DAY, hours);
        }
        if (minutes > 0) {
            c.add(Calendar.MINUTE, minutes);
        }
        if (seconds > 0) {
            c.add(Calendar.SECOND, seconds);
        }
        now = c.getTime();
        return now;
    }

    public static String getHumanTimeAway(Date unbanDate) {
        Long NowInMilli = (new Date()).getTime();
        Long TargetInMilli = unbanDate.getTime();
        Long diffInSeconds = (TargetInMilli - NowInMilli) / 1000+1;

        long diff[] = new long[] {0,0,0,0,0};
        /* sec */diff[4] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        /* min */diff[3] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        /* hours */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        /* days */diff[1] = (diffInSeconds = (diffInSeconds / 24)) >= 31 ? diffInSeconds % 31: diffInSeconds;
        /* months */diff[0] = (diffInSeconds = (diffInSeconds / 31));
        
        String message = "";
        
        if(diff[0] > 0) {
            message += diff[0] + " month";
            if(diff[0] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[1] > 0) {
            message += diff[1] + " day";
            if(diff[1] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[2] > 0) {
            message += diff[2] + " hour";
            if(diff[2] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[3] > 0) {
            message += diff[3] + " minute";
            if(diff[3] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[4] > 0) {
            message += diff[4] + " second";
            if(diff[4] > 1) {
                message += "s";
            }
            return message;
        }
        
        return "Invalid Time Diff!";
    }
    
    //Material Utils
    public static boolean isAir(Block type) {
        if(type == null) return true;
        return isAir(type.getType());
    }
    
    public static boolean isAir(Material type) {
        if(type == null) return true;
        if(type.equals(Material.AIR)) return true;
        return !type.isSolid();
    }
}
