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

package com.domsplace.DomsCommands.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class Warp {
    private static final List<Warp> WARPS = new ArrayList<Warp>();
    public static final String WARP_NAME_REGEX = "^[a-zA-Z0-9]*$";
    
    public static List<Warp> getWarps() {return new ArrayList<Warp>(WARPS);}
    
    public static Warp getWarp(String name) {
        for(Warp w : WARPS) {
            if(w.getName().equalsIgnoreCase(name)) return w;
        }
        return null;
    }
    
    public static List<Warp> getWarpsAlphabetically() {
        List<Warp> warps = new ArrayList<Warp>(WARPS);
        Collections.sort(warps, new Comparator<Warp>() {
        @Override
        public int compare(Warp result1, Warp result2) {
                return result1.getName().compareTo(result2.getName());
            }
        });
        
        return warps;
    }
    
    //Instance
    private String warp;
    private DomsLocation location;
    
    public Warp (String name, DomsLocation location) {
        this.warp = name;
        this.location = location;
        
        this.register();
    }
    
    public String getName() {return this.warp;}
    public DomsLocation getLocation() {return this.location;}
    
    public final void register() {Warp.WARPS.add(this);}
    public final void deRegister() {Warp.WARPS.remove(this);}
}
