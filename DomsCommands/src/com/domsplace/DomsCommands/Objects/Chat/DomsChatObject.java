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
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Dominic Masters
 */
public class DomsChatObject {
    public static String escapeText(String text) {
        text = text.replaceAll("\\\\", "\\\\\\\\");
        text = text.replaceAll("\"", "\\\\\"");
        return text;
    }

    public static DomsChatObject createFromString(String msg) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(msg);
            
            DomsChatObject obj = new DomsChatObject();
            
            for(Object item : object.keySet()) {
                Object value = object.get(item);
                String key = item.toString();
            }
            
            return obj;
        } catch(Exception e) {
            return null;
        }
    }
    
    //Instance
    private List<DomsChatComponent> components;
    
    public DomsChatObject() {
        this.components = new ArrayList<DomsChatComponent>();
    }
    
    public List<DomsChatComponent> getComponents() {return new ArrayList<DomsChatComponent>(this.components);}
    
    public void addChatComponent(DomsChatComponent c) {this.components.add(c);}
    
    public void removeChatComponent(DomsChatComponent c) {this.components.remove(c);}
    
    public String compile() {
        String result = "{";
        
        List<DomsChatComponent> extras = new ArrayList<DomsChatComponent>();
        
        for(int i = 0; i < this.components.size(); i++) {
            if(this.components.get(i).getType().equals(ComponentType.EXTRA)) {
                extras.add(this.components.get(i));
                continue;
            }
            result += this.components.get(i).compile();
            if(i < (this.components.size() - 1)) result += ",";
        }
        
        if(extras.size() > 0) {
            if(!result.endsWith("{") && !result.endsWith(",")) result += ",";
            result += ComponentType.EXTRA.getKey() + ":[";
            for(int i = 0; i < extras.size(); i++) {
                String compile = extras.get(i).compile();
                compile = compile.replaceFirst(ComponentType.EXTRA.getKey() + ":\\[", "");
                compile = Base.trim(compile, compile.length() - 1);
                result += compile;
                if(i < (extras.size() - 1)) result += ",";
            }
            result += "]";
        }
        
        result += "}";
        return result;
    }
    
    public String compileUnformatted() {
        String colors = "";
        String current = "";
        
        for(DomsChatComponent component : this.components) {
            if(component.getType().equals(ComponentType.TEXT)) {
                current += component.getValue().toString();
                continue;
            }
            
            if(component.getType().equals(ComponentType.COLOR)) {
                colors += Base.getColorByName(component.getValue().toString());
                continue;
            }
            
            if(component.getType().equals(ComponentType.BOLD) && component.getValue().toString().equalsIgnoreCase("true")) {
                colors += ChatColor.BOLD;
                continue;
            }
            
            if(component.getType().equals(ComponentType.UNDERLINE) && component.getValue().toString().equalsIgnoreCase("true")) {
                colors += ChatColor.UNDERLINE;
                continue;
            }
            
            if(component.getType().equals(ComponentType.STRIKETHROUGH) && component.getValue().toString().equalsIgnoreCase("true")) {
                colors += ChatColor.STRIKETHROUGH;
                continue;
            }
            
            if(component.getType().equals(ComponentType.OBFUSCATE) && component.getValue().toString().equalsIgnoreCase("true")) {
                colors += ChatColor.MAGIC;
                continue;
            }
            
            if(component.getType().equals(ComponentType.ITALIC) && component.getValue().toString().equalsIgnoreCase("true")) {
                colors += ChatColor.ITALIC;
                continue;
            }
            
            if(component.getType().equals(ComponentType.EXTRA)) {
                Object v = component.getValue();
                if(v instanceof DomsChatObject[]) {
                    for(DomsChatObject ob : (DomsChatObject[]) v) {
                        current += ob.compileUnformatted();
                    }
                }
                if(v instanceof List) {
                    for(DomsChatObject ob : (List<DomsChatObject>) v) {
                        current += ob.compileUnformatted();
                    }
                }
            }
        }
        
        return colors + current;
    }
}