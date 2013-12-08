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

package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Bases.Base;
import static com.domsplace.DomsCommands.Objects.DomsChannel.escapeRaw;
import java.util.regex.Matcher;

/**
 *
 * @author Dominic Masters
 */
public class DomsChatFormat {
    private String group;
    private String format;
    
    /*
    private String playerFormat = "{text:\"{DISPLAYNAME}\",hoverEvent:{action:show_text,value:\"Username: {NAME}\"},clickEvent:{action:suggest_command,value:\"{NAME}\"}}";
    private String urlFormat = "{text:\"§aLink\",hoverEvent:{action:show_text,value:\"§aClick to go to: §r{URL}\"},clickEvent:{action:open_url,value:\"{URL}\"}}";
    private String commandFormat = "{text:\"§9{COMMAND}\",hoverEvent:{action:show_text,value:\"Click to try this command.\"},clickEvent:{action:suggest_command,value:\"{COMMAND}\"}}";
    */
    private String playerFormat = null;
    private String urlFormat = null;
    private String commandFormat = null;
    
    public DomsChatFormat(String group, String format) {
        this.group = group;
        this.format = format;
    }
    
    public String getGroup() {return this.group;}
    public String getFormat() {return this.format;}
    public String getPlayerFormat() {return this.playerFormat;}
    public String getURLFormat() {return this.urlFormat;}
    public String getCommandFormat() {return this.commandFormat;}
    
    public void setPlayerFormat(String format) {this.playerFormat = format;}
    public void setURLFormat(String format) {this.urlFormat = format;}
    public void setCommandFormat(String format) {this.commandFormat = format;}
    
    public String formatPart(String part, DomsPlayer talker, DomsPlayer reciever) {
        String guess = Base.removeColors(part);
        
        //Handle isPlayer
        if(this.playerFormat != null) {
            DomsPlayer g = DomsPlayer.getExactPlayer(guess.replaceAll(DomsPlayer.NOT_MINECRAFT_NAME_REGEX, ""));
            if(g != null) return formatPlayer(part, g, talker, reciever, guess);
        }
        
        if(this.urlFormat != null) {
            if(Base.isURL(guess)) return formatURL(part, talker, reciever, guess);
        }
        
        if(this.commandFormat != null) {
            if(guess.startsWith("/")) return formatCommand(part, talker, reciever, guess);
        }
        
        return formatText(part);
    }
    
    public String formatText(String x) {
        return "{text:\"" + escapeRaw(x) + "\"}";
    }
    
    public String formatPlayer(String f, DomsPlayer player, DomsPlayer talker, DomsPlayer reciever, String guess) {
        if(!reciever.canSee(player.getOfflinePlayer())) return formatText(f);
        if(!player.isOnline()) return formatText(f);
        String form = playerFormat;
        
        form = repl("DISPLAYNAME", form, player.getDisplayName());
        form = repl("NAME", form, player.getPlayer());
        form = repl("PLAYER", form, player.getPlayer());
        
        form = repl("GROUP", form, player.getGroup());
        form = repl("PREFIX", form, player.getChatPrefix());
        form = repl("SUFFIX", form, player.getChatSuffix());
        
        String[] x = f.split(guess.replaceAll(DomsPlayer.NOT_MINECRAFT_NAME_REGEX, ""));
        
        if(x.length > 0) {
            form = formatText(x[0]) + "," + form;
        }
        if(x.length > 1) {
            form = form + "," + formatText(x[1]);
        }
        
        return form;
    }
    
    public String formatURL(String f, DomsPlayer talker, DomsPlayer reciever, String guess) {
        String form = urlFormat;
        form = repl("URL", form, guess);
        return form;
    }
    
    public String formatCommand(String f, DomsPlayer talker, DomsPlayer reciever, String guess) {
        String form = commandFormat;
        form = repl("COMMAND", form, guess);
        return form;
    }
    
    public String repl(String d, String i, String r) {
        return i.replaceAll("(?i)\\{" + d + "\\}", Matcher.quoteReplacement(escapeRaw(r)));
    }
}
