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
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.ChatColor;

/**
 *
 * @author Dominic Masters
 */
public abstract class DomsChatFormatter {
    public static final DomsChatFormatter MESSAGE_FORMATTER = new DomsChatParsedObjectFormatter("MESSAGE") {
        @Override
        public DomsChatObject request(DomsPlayer talker, DomsPlayer reciever, DomsChannel channel,
            DomsChatFormat format, String current, ChatColor c, boolean underline, boolean strikethrough,
            boolean obfuscate, boolean italics, boolean bold, String message) {
            
            current = Base.coloriseByPermission(message, talker, "DomsCommands.chat.colors.");
            
            return format.getObject(talker, reciever, channel, current, c, underline, strikethrough, obfuscate, italics, bold, message, false);
        }
    };
    
    public static final DomsChatURLFormatter URL_FORMATTER = new DomsChatURLFormatter();
    
    public static final DomsChatFormatter VARIABLE_FORMATTER = new DomsChatPlayerVariableFormatter() {};
    
    public DomsChatFormatter() {}
    
    public abstract DomsChatObject getObject(DomsPlayer talker, DomsPlayer reciever, DomsChannel channel,
            DomsChatFormat format, String current, ChatColor c, boolean underline, boolean strikethrough,
            boolean obfuscate, boolean italics, boolean bold, String message);
}
