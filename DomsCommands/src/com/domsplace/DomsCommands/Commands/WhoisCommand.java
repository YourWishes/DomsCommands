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
import com.domsplace.DomsCommands.Bases.PluginHook;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Punishment;
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       26/10/2013
 */
public class WhoisCommand extends BukkitCommand {
    public WhoisCommand() {
        super("whois");
        this.addSubCommandOption(SubCommandOption.PLAYERS_OPTION);
    }
    
    @Override
    @Deprecated
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return false;
        }
        
        DomsPlayer player = DomsPlayer.guessPlayerExactly(sender, args[0], false);
        if(player == null || player.isConsole()) {
            sendMessage(sender, ChatError + "That player hasn't played before.");
            return true;
        }
        
        
        List<String> messages = new ArrayList<String>();
        try {messages.add(ChatImportant + "Information about " + player.getUsername());} catch(Exception e) {}
        try {messages.add(ChatImportant + "UUID: " + ChatDefault + player.getStringUUID());} catch(Exception e) {}
        try {messages.add(ChatImportant + "Display Name: " + ChatDefault + player.getDisplayName());} catch(Exception e) {}
        if(player.getNamePlate() != null && !player.getNamePlate().equalsIgnoreCase("off") && !player.getNamePlate().replaceAll(" ", "").equals("")) {
            messages.add(ChatImportant + "Nameplate: " + ChatDefault + player.getNamePlate());
        }
        try {messages.add(ChatImportant + "Online: " + ChatDefault + (player.isOnline(sender) ? "Yes" : "No"));} catch(Exception e) {}
        try {messages.add(ChatImportant + "IP: " + ChatDefault + player.getLastIP());} catch(Exception e) {}
        try {messages.add(ChatImportant + "Joined: " + ChatDefault + Base.getTimeDifference(player.getJoinTime(), getNow()) + ChatDefault + " ago");} catch(Exception e) {}
        try {messages.add(ChatImportant + "Last Login: " + ChatDefault + Base.getTimeDifference(player.getLoginTime(), getNow()) + ChatDefault + " ago");} catch(Exception e) {}
        try {messages.add(ChatImportant + "Last Logout: " + ChatDefault + Base.getTimeDifference(player.getLogoutTime(), getNow()) + ChatDefault + " ago");} catch(Exception e) {}
        try {messages.add(ChatImportant + "Playtime: " + ChatDefault + Base.capitalizeFirstLetter(Base.getTimeDifference(0, player.getPlayTime())));} catch(Exception e) {}
        try {messages.add(ChatImportant + "Last Location: " + ChatDefault + player.getLocation().toHumanString());} catch(Exception e) {}
        try {messages.add(ChatImportant + "Group: " + ChatDefault + player.getGroup());} catch(Exception e) {}
        try {messages.add(ChatImportant + "Can Fly: " + ChatDefault + (player.isFlightMode() ? "Yes" : "No"));} catch(Exception e) {}
        try {messages.add(ChatImportant + "Operator: " + ChatDefault + (player.isOp() ? "Yes" : "No"));}catch(Exception e) {}
        
        try {
            if(player.getLastIP().equals("")) throw new Exception("IDGAF");
            List<DomsPlayer> ipPlayers = DomsPlayer.getPlayersByIP(player.getLastIP());
            if(ipPlayers.size() > 1) {
                String x = ChatImportant + "Players Using IP: " + ChatDefault;
                for(int i = 0; i < ipPlayers.size(); i++) {
                    if(ipPlayers.get(i).isOnline(sender)) x+= ChatColor.GREEN;
                    x += ipPlayers.get(i).getUsername() + ChatDefault;
                    if(i < ipPlayers.size() - 1) x += ", ";
                }
                messages.add(x);
            }
        } catch(Exception e) {}
        
        List<Punishment> bans = player.getPunishmentsOfType(PunishmentType.BAN);
        List<Punishment> kicks = player.getPunishmentsOfType(PunishmentType.KICK);
        List<Punishment> warns = player.getPunishmentsOfType(PunishmentType.WARN);
        List<Punishment> mutes = player.getPunishmentsOfType(PunishmentType.MUTE);
        
        if(bans.size() > 0) messages.add(ChatImportant + "Bans: " + ChatDefault + bans.size());
        if(kicks.size() > 0) messages.add(ChatImportant + "Kicks: " + ChatDefault + kicks.size());
        if(warns.size() > 0) messages.add(ChatImportant + "Warnings: " + ChatDefault + warns.size());
        if(mutes.size() > 0) messages.add(ChatImportant + "Mutes: " + ChatDefault + mutes.size());
        
        if(player.isBanned()) messages.add(ChatImportant + "Banned for: " + ChatDefault + player.getLastPunishmentReason(PunishmentType.BAN));
        if(player.isMuted()) messages.add(ChatImportant + "Muted for: " + ChatDefault + player.getLastPunishmentReason(PunishmentType.MUTE));
        
        if(player.isOnline(sender)) {
            Player p = player.getOnlinePlayer();
            messages.add(ChatImportant + "GameMode: " + ChatDefault + p.getGameMode().name());
            messages.add(ChatImportant + "Health: " + ChatDefault + p.getHealth() + "/" + p.getMaxHealth());
            messages.add(ChatImportant + "Food: " + ChatDefault + p.getFoodLevel() + "/20");
            messages.add(ChatImportant + "Running: " + ChatDefault + (p.isSprinting() ? "Yes" : "No"));
            messages.add(ChatImportant + "Sneaking: " + ChatDefault + (p.isSneaking() ? "Yes" : "No"));
        }
        
        if(PluginHook.FORUMAA_HOOK.isHooked()) {
            messages.add(ChatImportant + "ForumAA Registered: " + ChatDefault + (player.isForumAARegistered() ? "Yes" : "No"));
            messages.add(ChatImportant + "ForumAA Activated: " + ChatDefault + (player.isForumAAActivated()? "Yes" : "No"));
        }
        
        if(Base.useEcon()) {
            messages.add(ChatImportant + "Has Account: " + ChatDefault + (Base.hasBalance(player.getUsername()) ? "Yes" : "No"));
            messages.add(ChatImportant + "Balance: " + ChatDefault + Base.formatEcon(Base.getBalance(player.getUsername())));
        }
        
        if(PluginHook.VILLAGES_HOOK.isHooked()) {
            try {
                Resident r = Resident.getResident(player.getOfflinePlayer());
                Village v = Village.getPlayersVillage(r);
                messages.add(ChatImportant + "In Village: " + ChatDefault + (v == null ? "No" : "Yes"));
                if(v != null) {
                    messages.add(ChatImportant + "Village: " + ChatDefault + v.getName());
                    messages.add(ChatImportant + "Is Mayor: " + ChatDefault + (v.isMayor(r) ? "Yes" : "No"));
                }
            } catch(Exception e) {} catch(Error e) {}
        }
        
        sendMessage(sender, messages);
        return true;
    }
}
