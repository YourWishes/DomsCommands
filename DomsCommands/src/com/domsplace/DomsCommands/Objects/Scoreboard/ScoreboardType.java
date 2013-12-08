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

package com.domsplace.DomsCommands.Objects.Scoreboard;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dominic Masters
 */
public abstract class ScoreboardType {
    public enum Type {
        HEAD, SIDE_BAR, HEALTH
    };
    
    public static final List<ScoreboardType> SCOREBOARD_TYPES =  new ArrayList<ScoreboardType>();
    
    public static List<ScoreboardType> getScoreboardsOfType(Type t) {
        List<ScoreboardType> types =  new ArrayList<ScoreboardType>();
        for(ScoreboardType ty : ScoreboardType.SCOREBOARD_TYPES) {
            if(!t.equals(ty.type)) continue;
            types.add(ty);
        }
        return types;
    }
    
    //Instance
    private final String name;
    private final Type type;
    
    public ScoreboardType(final String name, final Type type) {
        this.name = name;
        this.type = type;
        
        SCOREBOARD_TYPES.add(this);
    }
    
    public final String getName() {return this.name;}
    public final Type getType() {return this.type;}
    
    public abstract void applyTo(PlayerScoreboard scoreboard);
}
