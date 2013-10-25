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

import com.domsplace.DomsCommands.Bases.Base;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * @author      Dominic
 * @since       08/10/2013
 */
public class DomsLocation {
    public static DomsLocation guessLocation(String c) {
        /*
         * Valid Formats:
         * x,z
         * x,y,z
         * x,z,world
         * x,y,z,world
         */
        
        int validcoordsfound = 0;
        
        String[] split = c.split(",");
        if(split.length < 2) return null;
        
        DomsLocation loc = new DomsLocation();
        
        //Conversion Exceptions may be thrown
        try {
            if(split.length == 2) {
                loc.setX(Base.getDouble(split[0]));
                loc.setZ(Base.getDouble(split[1]));
            } else if(split.length == 3) {
                loc.setX(Base.getDouble(split[0]));
                double p2 = Base.getDouble(split[1]);
                if(Base.isDouble(split[2])) {
                    loc.setY(p2);
                    loc.setZ(Base.getDouble(split[2]));
                } else {
                    loc.setZ(p2);
                    loc.setWorld(split[2]);
                }
            } else if(split.length == 4) {
                loc.setX(Base.getDouble(split[0]));
                loc.setY(Base.getDouble(split[1]));
                loc.setZ(Base.getDouble(split[2]));
                loc.setWorld(split[3]);
            } else if (split.length == 5) {
                loc.setX(Base.getDouble(split[0]));
                loc.setY(Base.getDouble(split[1]));
                loc.setZ(Base.getDouble(split[2]));
                loc.setPitch(Base.getFloat(split[3]));
                loc.setYaw(Base.getFloat(split[4]));
            } else if(split.length >= 6) {
                loc.setX(Base.getDouble(split[0]));
                loc.setY(Base.getDouble(split[1]));
                loc.setZ(Base.getDouble(split[2]));
                loc.setPitch(Base.getFloat(split[3]));
                loc.setYaw(Base.getFloat(split[4]));
                loc.setWorld(split[5]);
            }
        } catch(Exception e) {
            return null;
        }
        
        return loc;
    }
    
    //Instance
    private double x;
    private double y;
    private double z;
    
    private float pitch;
    private float yaw;
    
    private String world;
    
    public DomsLocation(DomsLocation location) {
        this(location.toLocation());
    }
    
    public DomsLocation(Location f) {
        this(f.getX(), f.getY(), f.getZ(), f.getPitch(), f.getYaw(), f.getWorld().getName());
    }
    
    public DomsLocation() {
        this(-1.0d, -1.0d);
    }
    
    public DomsLocation(double x, double z) {
        this(x, z, null);
    }
    
    public DomsLocation(double x, double z, String world) {
        this(x, -1.0d, z, world);
    }
    
    public DomsLocation(double x, double y, double z) {
        this(x, y, z, null);
    }
    
    public DomsLocation(double x, double y, double z, String world) {
        this(x, y, z, -1.0f, -1.0f, world);
    }
    
    public DomsLocation(double x, double y, double z, float pitch, float yaw, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }
    
    public double getX() {return this.x;}
    public double getY() {return this.y;}
    public double getZ() {return this.z;}
    public float getPitch() {return this.pitch;}
    public float getYaw() {return this.yaw;}
    public String getWorld() {return this.world;}
    public World getBukkitWorld() {return Bukkit.getWorld(world);}
    
    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setZ(double z) {this.z = z;}
    public void setPitch(float p) {this.pitch = p;}
    public void setYaw(float y) {this.yaw = y;}
    public void setWorld(String world) {this.world = world;}
    
    public boolean isWorldLoaded() {return this.getBukkitWorld() != null;}
    
    public Location toLocation() {return new Location(this.getBukkitWorld(), this.x, this.y, this.z, this.yaw, this.pitch);}

    public DomsLocation copy() {return DomsLocation.guessLocation(this.toString());}
    
    public DomsLocation getSafeLocation() {
        //Returns the Safest Location
        int y = 256;
        int x = (int) this.x;
        int z = (int) this.z;
        
        Block b = this.getBukkitWorld().getBlockAt(x, y, z);
        Block below;
        Block up;
        Block d = this.getBukkitWorld().getBlockAt(x, 64, z);
        
        boolean look = true;
        
        while(look) {
            below = b.getRelative(0, -1, 0);
            up = b.getRelative(0, 1, 0);            
            if(below == null) {
                Location l = d.getLocation();
                l.setX(this.x);
                l.setZ(this.z);
                l.setPitch(this.pitch);
                l.setYaw(this.yaw);
                return new DomsLocation(l);
            }
            
            if(up == null) {
                Location l = d.getLocation();
                l.setX(this.x);
                l.setZ(this.z);
                l.setPitch(this.pitch);
                l.setYaw(this.yaw);
                return new DomsLocation(l);
            }
            
            if(below.getY() <= 0) {
                Location l = d.getLocation();
                l.setX(this.x);
                l.setZ(this.z);
                l.setPitch(this.pitch);
                l.setYaw(this.yaw);
                return new DomsLocation(l);
            }
            
            if(Base.isAir(b) && Base.isAir(up) && !Base.isAir(below)) {
                Location l = b.getLocation();
                l.setX(this.x);
                l.setZ(this.z);
                l.setPitch(this.pitch);
                l.setYaw(this.yaw);
                return new DomsLocation(l);
            }
            
            b = b.getRelative(0, -1, 0);
        }
        
        Location l = b.getLocation();
        l.setX(this.x);
        l.setZ(this.z);
        l.setPitch(this.pitch);
        l.setYaw(this.yaw);
        return new DomsLocation(l);
    }
    
    @Override
    public String toString() {
        String s = this.x + "," + this.y + "," + this.z + ",";
        if(this.pitch != -1.0f && this.yaw != -1.0f) {
            s += this.pitch + "," + this.yaw + ",";
        }
        s += this.world;
        return s;
    }
}
