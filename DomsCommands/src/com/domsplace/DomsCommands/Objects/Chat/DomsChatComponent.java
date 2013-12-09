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

import java.util.List;

/**
 *
 * @author Dominic Masters
 */
public class DomsChatComponent {
    public enum DataType {
        OBJECT, STRING, ARRAY, BOOLEAN
    };
    
    public enum ComponentType {
        TEXT                ("Text", "text", DataType.STRING),
        ITALIC              ("Italics", "italic", DataType.BOOLEAN),
        BOLD                ("Bold", "bold", DataType.BOOLEAN),
        STRIKETHROUGH       ("Strikethrough", "strikethrough", DataType.BOOLEAN),
        UNDERLINE           ("Underline", "underlined", DataType.BOOLEAN),
        OBFUSCATE           ("Obfuscate", "obfuscated", DataType.BOOLEAN),
        COLOR               ("Color", "color", DataType.STRING),
        CLICK_EVENT         ("Click Event", "clickEvent", DataType.OBJECT),
        HOVER_EVENT         ("Hover Event", "hoverEvent", DataType.OBJECT),
        EVENT_ACTION        ("Event Action", "action", DataType.STRING),
        EVENT_VALUE         ("Event Value", "value", DataType.STRING),
        EXTRA               ("Extra", "extra", DataType.ARRAY),
        TRANSLATE           ("Translate", "translate", DataType.STRING),
        TRANSLATE_ARGUMENT  ("Translate Argument", "with", DataType.ARRAY)
        ;
        
        private final String name;
        private final String key;
        private final DataType type;
        ComponentType(String name, String key, DataType type) {
            this.name = name;
            this.key = key;
            this.type = type;
        }
        public final String getName() {return this.name;}
        public final String getKey() {return this.key;}
        public final DataType getType() {return this.type;}
    };
    
    private ComponentType type;
    private Object value;
    
    public DomsChatComponent(ComponentType type, Object value) {
        this.type = type;
        this.value = value;
    }
    
    public ComponentType getType() {return this.type;}
    public Object getValue() {return this.value;}
    
    public void setValue(String value) {this.value = value;}
    
    public String compile() {
        String rv = this.type.key + ":";
        
        if(this.type.type.equals(DataType.OBJECT) && this.value instanceof DomsChatObject) {
            rv += ((DomsChatObject) this.value).compile();
        } else if(this.type.type.equals(DataType.ARRAY) && (this.value instanceof DomsChatObject[])) {
            DomsChatObject[] values = (DomsChatObject[]) this.value;
            rv += "[";
            
            for(int i = 0; i < values.length; i++) {
                rv += values[i].compile();
                if(i < values.length - 1) rv += ",";
            }
            
            rv += "]";
        } else if(this.type.type.equals(DataType.ARRAY) && (this.value instanceof List)) {
            List<? extends DomsChatObject> values = (List<? extends DomsChatObject>) this.value;
            rv += "[";
            
            for(int i = 0; i < values.size(); i++) {
                rv += values.get(i).compile();
                if(i < values.size() - 1) rv += ",";
            }
            
            rv += "]";
        } else if(this.type.type.equals(DataType.BOOLEAN) && (this.value instanceof Boolean)) {
            rv += ((Boolean) this.value).booleanValue();
        } else {
            rv += "\"" + DomsChatObject.escapeText(this.value.toString()) + "\"";
        }
        
        return rv;
    }
}
