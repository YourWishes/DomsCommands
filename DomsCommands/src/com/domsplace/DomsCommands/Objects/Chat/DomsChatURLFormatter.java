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

package com.domsplace.DomsCommands.Objects.Chat;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Objects.Chat.DomsChatComponent.ComponentType;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import java.util.regex.Matcher;
import org.bukkit.ChatColor;

/**
 *
 * @author Dominic Masters
 */
public class DomsChatURLFormatter extends DomsChatFormatter {
    public static final String URL_CHAT_FORMAT = Base.colorise("{text:\"&aLink\",clickEvent:{action:open_url,value:\"{URL}\"},hoverEvent:{action:show_text,value:\"&bGo to &a{URL}\"}}");
    
    @Override
    public DomsChatObject getObject(DomsPlayer talker, DomsPlayer reciever, DomsChannel channel, DomsChatFormat format, String current, ChatColor c, boolean underline, boolean strikethrough, boolean obfuscate, boolean italics, boolean bold, String message) {
        if(!talker.hasPermisson("DomsCommands.chat.url")) return null;
        String[] parts = current.split(" ");
        String x = "";
        
        DomsChatObject topObj = null;
        
        for(String s : parts) {
            if(!Base.isURL(s)) {
                x += s + " ";
                continue;
            }
            if(!x.equals("")) {
                DomsChatObject remainingObj 
                        = format.getObject(talker, reciever, channel, x, c, underline, strikethrough, obfuscate, italics, bold, message, false);
                if(topObj == null) {
                    topObj = remainingObj;
                } else {
                    topObj.addChatComponent(new DomsChatComponent(ComponentType.EXTRA, new DomsChatObject[] {remainingObj}));
                }
            }
            
            DomsChatObject subObject = new DomsChatObject();
            subObject.addChatComponent(new DomsChatComponent(ComponentType.TEXT, (topObj != null ? " " : "") + "Link"));
            subObject.addChatComponent(new DomsChatComponent(ComponentType.COLOR, "green"));
            
            DomsChatObject clickEvent = new DomsChatObject();
            clickEvent.addChatComponent(new DomsChatComponent(ComponentType.EVENT_ACTION, "open_url"));
            clickEvent.addChatComponent(new DomsChatComponent(ComponentType.EVENT_VALUE, s));
            subObject.addChatComponent(new DomsChatComponent(ComponentType.CLICK_EVENT, clickEvent));
            
            DomsChatObject hoverEvent = new DomsChatObject();
            hoverEvent.addChatComponent(new DomsChatComponent(ComponentType.EVENT_ACTION, "show_text"));
            hoverEvent.addChatComponent(new DomsChatComponent(ComponentType.EVENT_VALUE, ChatColor.DARK_AQUA + "Click to go to " + ChatColor.AQUA + s));
            subObject.addChatComponent(new DomsChatComponent(ComponentType.HOVER_EVENT, hoverEvent));
            
            if(topObj == null) {
                topObj = new DomsChatObject();
                topObj.addChatComponent(new DomsChatComponent(ComponentType.TEXT, ""));
            }
            topObj.addChatComponent(new DomsChatComponent(ComponentType.EXTRA, new DomsChatObject[] {subObject}));
            x = " ";
        }
        
        if(x.length() > 0) x = Base.trim(x, x.length() - 1);
        
        DomsChatObject remaining = format.getObject(talker, reciever, channel, x, c, underline, strikethrough, obfuscate, italics, bold, message);
        if(topObj == null) {
            topObj = remaining;
        } else {
            topObj.addChatComponent(new DomsChatComponent(ComponentType.EXTRA, new DomsChatObject[] {remaining}));
        }
        
        return topObj;
    }
}
