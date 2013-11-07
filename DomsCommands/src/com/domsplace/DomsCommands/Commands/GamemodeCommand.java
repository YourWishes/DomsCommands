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
import com.domsplace.DomsCommands.Objects.SubCommandOption;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       17/10/2013
 */
public class GamemodeCommand extends BukkitCommand {
    public static final String[] survivalCommands = new String[] {
        "survival",
        "gms",
        "gamemodes",
        "gamemodesurvival",
        "s",
        "0"
    };
    
    public static final String[] creativeCommands = new String[] {
        "creative",
        "gmc",
        "gamemodec",
        "gamemodecreative",
        "c",
        "1"
    };
    
    public static final String[] adventureCommands = new String[] {
        "adventure",
        "gma",
        "gamemodea",
        "gamemodeadventure",
        "a",
        "3"
    };
    
    public static final String[] toggleCommands = new String[] {
        "gm",
        "gamemode",
        "togglegamemode",
        "togglegm"
    };
    
    private static boolean isToggle(String s) {
        for(String g : toggleCommands) {
            if(s.equalsIgnoreCase(g)) return true;
        }
        return false;
    }
    
    public static GameMode getGameMode(String command) {
        GameMode gamemode = null;
        
        if(gamemode == null) {
            for(String s : survivalCommands) {
                if(!s.equalsIgnoreCase(command)) continue;
                gamemode = GameMode.SURVIVAL;
                break;
            }
        }
        
        if(gamemode == null) {
            for(String s : creativeCommands) {
                if(!s.equalsIgnoreCase(command)) continue;
                gamemode = GameMode.CREATIVE;
                break;
            }
        }
        
        if(gamemode == null) {
            for(String s : adventureCommands) {
                if(!s.equalsIgnoreCase(command)) continue;
                gamemode = GameMode.ADVENTURE;
                break;
            }
        }
        
        return gamemode;
    }
    
    public GamemodeCommand() {
        super("gamemode");
        this.addSubCommandOption(new SubCommandOption(SubCommandOption.PLAYERS_OPTION, SubCommandOption.GAMEMODE_OPTION));
        this.addSubCommandOption(SubCommandOption.GAMEMODE_OPTION);
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        GameMode gamemode = null;
        DomsPlayer player;
        
        if(args.length == 0 && !isPlayer(sender)) {
            sendMessage(sender, ChatError + "Please enter a player name.");
            return true;
        }
        
        if(args.length == 0 && this.isToggle(label)) {
            player = DomsPlayer.getPlayer(sender);
            player.toggleGameMode();
            sendMessage(sender, "Changed GameMode to " + ChatImportant + player.getOnlinePlayer().getGameMode().name() + ChatDefault + ".");
            return true;
        }
        
        if(args.length == 1 && !isPlayer(sender)) {
            player = DomsPlayer.guessOnlinePlayer(sender, args[0]);
            if(player == null || !player.isOnline(sender) || player.isConsole()) {
                sendMessage(sender, ChatError + "Target isn't online.");
                return true;
            }
            
            player.toggleGameMode();
            sendMessage(sender, ChatDefault + "Changed " + ChatImportant + 
                    player.getDisplayName() + ChatDefault + "'s GameMode to " + 
                    ChatImportant + player.getOnlinePlayer().getGameMode().name()
                    + ChatDefault + ".");
            player.sendMessage("Your GameMode has been changed.");
            return true;
        }
        
        String command = "";
        
        if(args.length == 0) {
            command = label;
            player = DomsPlayer.getPlayer(sender);
        } else if(args.length == 1) {
            command = args[0];
            player = DomsPlayer.getPlayer(sender);
        } else {
            command = args[1];
            player = DomsPlayer.guessOnlinePlayer(sender, args[0]);
        }
        
        gamemode = getGameMode(command);
        
        if(player == null || !player.isOnline(sender) || player.isConsole()) {
            sendMessage(sender, ChatError + "Target isn't online.");
            return true;
        }
        
        if(!player.equals(DomsPlayer.getPlayer(sender)) && !hasPermission(sender, "DomsCommands.gamemode.others")) {
            return this.noPermission(sender, cmd, label, args);
        }
        
        if(gamemode == null) {
            sendMessage(sender, ChatError + "Not a valid GameMode.");
            return true;
        }
        
        if(gamemode.equals(player.getOnlinePlayer().getGameMode())) {
            sendMessage(sender, ChatError + "Player already has this GameMode.");
            return true;
        }
        
        player.getOnlinePlayer().setGameMode(gamemode);
        
        if(!player.compare(sender)) {
            sendMessage(sender, "Set " + ChatImportant + player.getDisplayName() + ChatDefault + "'s GameMode to " + ChatImportant + gamemode.name() + ChatDefault + ".");
            player.sendMessage("Your GameMode has been updated.");
            return true;
        } else {
            sendMessage(sender, "Your GameMode has been set to " + ChatImportant + gamemode.name() + ChatDefault + ".");
            return true;
        }
    }
}
