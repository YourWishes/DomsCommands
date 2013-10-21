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

package com.domsplace.DomsCommands.Enums;

import com.domsplace.DomsCommands.Bases.DomsEnum;
import org.bukkit.World;

/**
 *
 * @author Dominic Masters
 */
public class WeatherType extends DomsEnum {
    public static final WeatherType SUN = new WeatherType("Sunny");
    public static final WeatherType RAIN = new WeatherType("Raining");
    public static final WeatherType STORM = new WeatherType("Storm");
    public static final WeatherType TOGGLE = new WeatherType("Toggle");
    
    private static void apply(World w, int d, WeatherType t) {
        if(t.equals(SUN)) {
            w.setStorm(false);
            w.setWeatherDuration(d);
            w.setThundering(false);
        }
        if(t.equals(RAIN)) {
            w.setStorm(true);
            w.setWeatherDuration(d);
            w.setThundering(false);
        }
        if(t.equals(STORM)) {
            w.setStorm(true);
            w.setWeatherDuration(d);
            w.setThundering(true);
            w.setThunderDuration(d);
        }
        
        if(t.equals(TOGGLE)) {
            if(getType(w).equals(RAIN) || getType(w).equals(STORM))apply(w,d,SUN);
            else apply(w,d,RAIN);
        }
    }
    
    public static final WeatherType getType(World w) {
        if(w.isThundering()) return STORM;
        if(w.hasStorm()) return RAIN;
        return SUN;
    }
    
    //Instance
    private final String name;
    private WeatherType(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void applyTo(World w, int d) {apply(w, d, this);}
}
