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

import com.domsplace.DomsCommands.Objects.Chat.DomsChatComponent.ComponentType;
import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;


/**
 *
 * @author Dominic Masters
 */
public class DomsChatFormat {
    private String format;
    private String group;
    private List<DomsChatFormatter> formatters;
    
    public DomsChatFormat(String format) {
        this.format = format;
        
        this.formatters = new ArrayList<DomsChatFormatter>();
        this.formatters.add(DomsChatFormatter.MESSAGE_FORMATTER);
        this.formatters.add(DomsChatFormatter.VARIABLE_FORMATTER);
    }
    
    public String getFormat() {return this.format;}
    public String getGroup() {return this.group;}
    public List<DomsChatFormatter> getChatFormatters() {return new ArrayList<DomsChatFormatter>(this.formatters);}
    
    public void setFormat(String format) {this.format = format;}
    public void setGroup(String group) {this.group = group;}
    
    public void chat(DomsPlayer chatter, DomsPlayer reciever, DomsChannel channel, String message) {
        chatter.updateVariables();
        String formattingFormat = this.format + ChatColor.RESET;
        
        char[] chars = formattingFormat.toCharArray();
        
        boolean expectingColor = false;
        ChatColor c = null;
        boolean under = false;
        boolean strike = false;
        boolean obfus = false;
        boolean italic = false;
        boolean bold = false;
        
        boolean lunder = false;
        boolean lstrike = false;
        boolean lobfus = false;
        boolean litalic = false;
        boolean lbold = false;
        
        String current = "";
        
        DomsChatObject baseObject = null;
        DomsChatObject currentChatObject = null;
        
        for(int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];
            if(expectingColor) {
                ChatColor col;
                try {
                    col = ChatColor.getByChar(currentChar);
                    if(col == null) throw new Exception();
                } catch (Exception e) {
                    expectingColor = false;
                    i--;
                    continue;
                }
                
                if(col.isFormat() || col.equals(ChatColor.RESET)) {
                    if(col.equals(ChatColor.BOLD)) bold = true;
                    if(col.equals(ChatColor.ITALIC)) italic = true;
                    if(col.equals(ChatColor.MAGIC)) obfus = true;
                    if(col.equals(ChatColor.STRIKETHROUGH)) strike = true;
                    if(col.equals(ChatColor.UNDERLINE)) under = true;
                    
                    if(col.equals(ChatColor.RESET)) {
                        c = ChatColor.RESET;
                    } else {
                        continue;
                    }
                }
                
                if(c != null) {
                    //Color already registerd, we need to make a sub component
                    DomsChatObject thisAsObject = this.getObject(chatter, reciever, 
                            channel, current, c,
                            (under != lunder || under), 
                            (strike != lstrike || strike), 
                            (obfus != lobfus || obfus), 
                            (italic != litalic || italic), 
                            (bold != lbold || bold), message);
                    
                    if(baseObject == null) {
                        baseObject = thisAsObject;
                    }
                    
                    if(currentChatObject != null) {
                        List<DomsChatObject> objs = new ArrayList<DomsChatObject>();
                        DomsChatComponent extraComponent = new DomsChatComponent(ComponentType.EXTRA, objs);
                        currentChatObject.addChatComponent(extraComponent);
                        objs.add(thisAsObject);
                    }
                    
                    currentChatObject = thisAsObject;
                    current = "";
                }
                c = col;
                continue;
            }
            
            if(currentChar == ChatColor.COLOR_CHAR) {
                expectingColor = true;
                continue;
            }
            
            current += currentChar;
        }
        
        if(baseObject == null) return;
        Base.sendRawMessage(reciever, baseObject);
    }
    
    public DomsChatObject getObject(DomsPlayer talker, DomsPlayer reciever, DomsChannel channel,
            String current, ChatColor c, boolean underline, boolean strikethrough,
            boolean obfuscate, boolean italics, boolean bold, String message) {
        return this.getObject(talker, reciever, channel, current, c, underline, strikethrough, obfuscate, italics, bold, message, true);
    }
    
    public DomsChatObject getObject(DomsPlayer talker, DomsPlayer reciever, DomsChannel channel,
            String current, ChatColor c, boolean underline, boolean strikethrough,
            boolean obfuscate, boolean italics, boolean bold, String message, boolean format) {
        
        if(format) {
            for(DomsChatFormatter formatter : this.formatters) {
                DomsChatObject com = formatter.getObject(talker, reciever, channel, this, current, c, underline, strikethrough, obfuscate, italics, bold, message);
                if(com == null) continue;
                return com;
            }
        }
        
        DomsChatComponent textComponent = new DomsChatComponent(ComponentType.TEXT, current);

        DomsChatComponent colorComponent = null;

        DomsChatComponent underComponent = null;
        DomsChatComponent strikeComponent = null;
        DomsChatComponent obfusComponent = null;
        DomsChatComponent italicComponent = null;
        DomsChatComponent boldComponent = null;

        if(c != ChatColor.RESET) colorComponent = new DomsChatComponent(ComponentType.COLOR, c.name().toLowerCase());

        if(underline) underComponent = new DomsChatComponent(ComponentType.UNDERLINE, underline);
        if(strikethrough) strikeComponent = new DomsChatComponent(ComponentType.STRIKETHROUGH, strikethrough);
        if(obfuscate) obfusComponent = new DomsChatComponent(ComponentType.UNDERLINE, obfuscate);
        if(italics) italicComponent = new DomsChatComponent(ComponentType.UNDERLINE, italics);
        if(bold) boldComponent = new DomsChatComponent(ComponentType.UNDERLINE, bold);

        DomsChatObject thisAsObject = new DomsChatObject();
        thisAsObject.addChatComponent(textComponent);
        if(colorComponent != null) thisAsObject.addChatComponent(colorComponent);
        if(underComponent != null) thisAsObject.addChatComponent(underComponent);
        if(strikeComponent != null) thisAsObject.addChatComponent(strikeComponent);
        if(obfusComponent != null) thisAsObject.addChatComponent(obfusComponent);
        if(italicComponent != null) thisAsObject.addChatComponent(italicComponent);
        if(boldComponent != null) thisAsObject.addChatComponent(boldComponent);
        
        return thisAsObject;
    }
}
