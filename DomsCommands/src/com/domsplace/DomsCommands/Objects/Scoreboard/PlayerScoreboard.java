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

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import com.domsplace.DomsCommands.Objects.Scoreboard.ScoreboardType.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dominic Masters
 */
public class PlayerScoreboard {
    public static long CYCLE_SPEED = 10*1000;
    
    private final DomsPlayer player;
    private long lastCycle;
    private Map<Type, ScoreboardType> scoreboards;
    
    public PlayerScoreboard(DomsPlayer player) {
        this.player = player;
        this.lastCycle = 0L;
        
        this.scoreboards = new HashMap<Type, ScoreboardType>();
    }
    
    public final DomsPlayer getPlayer() {return this.player;}
    public long getLastCycle() {return this.lastCycle;}
    public Map<Type, ScoreboardType> getScoreboards() {return new HashMap<Type, ScoreboardType>(this.scoreboards);}
    public ScoreboardType getScoreboard(Type type) {return (this.scoreboards.containsKey(type) ? this.scoreboards.get(type) : null);}
    
    public void setScoreboard(ScoreboardType sc) {this.setScoreboard(sc.getType(), sc);}
    public void setScoreboard(Type type, ScoreboardType sc) {this.scoreboards.put(type, sc);}
    
    public ScoreboardType getNextScoreboard(Type t) {
        List<ScoreboardType> types = ScoreboardType.getScoreboardsOfType(t);
        ScoreboardType type = this.getScoreboard(t);
        
        if(type != null) {
            int i = -1;
            for(int x = 0; x < types.size(); x++) {
                ScoreboardType st = types.get(x);
                if(!st.equals(this.scoreboards)) continue;
                i = x;
                break;
            }
            
            i++;
            if(i >= types.size()) i = 0;
            if(i < 0) i = 0;
            return types.get(i);
        }
        return types.get(0);
    }
    
    public void run() {
        long now = Base.getNow();
        if(now < (this.lastCycle + PlayerScoreboard.CYCLE_SPEED)) return;
        this.lastCycle = now;
    }
}
