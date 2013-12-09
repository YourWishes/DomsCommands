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

import com.domsplace.DomsCommands.Objects.DomsPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

/**
 *
 * @author Dominic Masters
 */
public abstract class DomsChatParsedObjectFormatter extends DomsChatFormatter {
    private final String listeningFor;
    
    public DomsChatParsedObjectFormatter(String list) {
        this.listeningFor = list;
    }
    
    public String getListeningFor() {return this.listeningFor;}
    
    @Override
    public DomsChatObject getObject(DomsPlayer talker, DomsPlayer reciever, DomsChannel channel,
        DomsChatFormat format, String current, ChatColor c, boolean underline, boolean strikethrough,
        boolean obfuscate, boolean italics, boolean bold, String message) {
        if(!current.toUpperCase().contains("{" + listeningFor.toUpperCase() + "}")) return null;

        String[] parts = current.split("(?i)\\{" + listeningFor.toUpperCase() + "\\}");

        DomsChatObject obj = null;
        if(parts.length > 0) {
            obj = format.getObject(talker, reciever, channel, parts[0], c, 
                    underline, strikethrough, obfuscate, italics, bold, message);
        }

        /* Perform Message Formatting */
        DomsChatObject messageObject = this.request(talker, reciever, channel, format, current, c, underline, strikethrough, obfuscate, italics, bold, message);

        if(obj == null) {
            obj = messageObject;
        } else {
            List<DomsChatObject> objs = new ArrayList<DomsChatObject>();
            DomsChatComponent extra = new DomsChatComponent(DomsChatComponent.ComponentType.EXTRA, objs);
            objs.add(messageObject);
            obj.addChatComponent(extra);
        }

        if(parts.length > 1) {
            DomsChatObject ob = format.getObject(talker, reciever, channel, parts[1], c, 
                    underline, strikethrough, obfuscate, italics, bold, message);

            List<DomsChatObject> objs = new ArrayList<DomsChatObject>();
            DomsChatComponent extra = new DomsChatComponent(DomsChatComponent.ComponentType.EXTRA, objs);
            objs.add(ob);
            obj.addChatComponent(extra);
        }

        return obj;
    }
    
    public DomsChatObject request(DomsPlayer talker, DomsPlayer reciever, DomsChannel channel,
        DomsChatFormat format, String current, ChatColor c, boolean underline, boolean strikethrough,
        boolean obfuscate, boolean italics, boolean bold, String message) {
        return format.getObject(talker, reciever, channel, message, c, underline, strikethrough, obfuscate, italics, bold, message);
    }
}
