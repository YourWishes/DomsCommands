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

package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Bases.Base;
import java.util.Map;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.material.Colorable;

/**
 *
 * @author Dominic Masters
 */
public class DomsEntity {
    public static EntityType guessType(String n) {
        n = n.replaceAll("_", "").toLowerCase();
        
        for(EntityType ty : EntityType.values()) {
            if(!ty.isAlive()) continue;
            if(!ty.isSpawnable()) continue;
            String x = ty.name().toLowerCase().replaceAll("_", "");
            if(x.startsWith(n)) return ty;
            if(x.equalsIgnoreCase(n)) return ty;
            if(x.contains(n)) return ty;
        }
        return null;
    }
    
    public static DomsEntity craftEntityBasic(String s, DomsPlayer tamer) {
        try {
            String[] parts = s.split(":");
            
            EntityType type = guessType(parts[0]);
            if(type == null) return null;
            DomsEntity entity = new DomsEntity(type);
            
            if(parts.length > 1) {
                for(int i = 1; i < parts.length; i++) {
                    String p = parts[i];
                    
                    if(p.equalsIgnoreCase("child")) entity.setChild(true);
                    if(p.equalsIgnoreCase("adult")) entity.setChild(false);
                    if(p.equalsIgnoreCase("charged")) entity.setCharged(true);
                    if(p.equalsIgnoreCase("tamed")) {
                        entity.setTamed(true);
                        entity.setTamer(tamer);
                    }
                    if(p.equalsIgnoreCase("villager")) entity.setZombieVillager(true);
                    if(p.equalsIgnoreCase("anger")) entity.setWolfAngry(true);
                    if(p.equalsIgnoreCase("shorn")) entity.setShorn(true);
                    if(p.equalsIgnoreCase("charged")) entity.setCharged(true);
                    if(p.equalsIgnoreCase("saddled")) entity.setWearingSaddle(true);
                    if(p.equalsIgnoreCase("sitting")) entity.setWolfSitting(true);
                }
            }
            
            return entity;
        } catch(Exception e) {return null;}
    }
    
    public static DomsEntity craftEntity(String line, DomsPlayer tamer) {
        Base.debug("Creating Entity From: " + line);
        try {
            DomsEntity entity = craftEntityBasic(line, tamer);
            if(entity != null) return entity;
            
            Map<String, String> result = Base.getDomsJSON(line);
            
            DomsEntity ent = new DomsEntity(null);
            
            for(String s : result.keySet()) {
                String key = s.toLowerCase();
                String value = result.get(s);
                
                if(key.equals("type")) ent.setType(DomsEntity.guessType(value));
                if(key.equals("child") || key.equals("baby")) ent.setChild(value.equalsIgnoreCase("true"));
                if(key.equals("name")) ent.setName(Base.colorise(value));
                if(key.equals("villager")) ent.setZombieVillager(value.equalsIgnoreCase("true"));
                if(key.equals("collar")) ent.setWolfCollarColor(Base.getDyeColor(value));
                if(key.equals("angry")) {
                    if(Base.isInt(value)) {
                        ent.setPigZombieAnger(Base.getInt(value));
                    } else {
                        ent.setWolfAngry(value.equalsIgnoreCase("true"));
                    }
                }
                if(key.equals("color")) ent.setWoolColor(Base.getDyeColor(value));
                if(key.equals("shorn")) ent.setShorn(value.equalsIgnoreCase("true"));
                if(key.equals("skeleton")) ent.setSkeletonType(SkeletonType.valueOf(value.toUpperCase()));
                if(key.equals("size") && Base.isInt(value)) ent.setSlimeSize(Base.getInt(value));
                if(key.equals("profession")) ent.setProfession(Profession.valueOf(value.toUpperCase()));
                if(key.equals("charged")) ent.setCharged(value.equalsIgnoreCase("true"));
                if(key.equals("endermanitem")) ent.setEndermanMaterial(DomsItem.createItem(value));
                if(key.equals("saddled")) ent.setWearingSaddle(value.equalsIgnoreCase("true"));
                if(key.equals("cattype")) ent.setCatType(Type.valueOf(value.toUpperCase()));
                if(key.equalsIgnoreCase("sitting")) ent.setWolfSitting(value.equalsIgnoreCase("true"));
            }
            
            if(ent.getType() == null) return null;
            
            return ent;
        } catch(Exception e) {return null;}
    }
    
    //Instance
    private EntityType type;
    private boolean child;
    private String name;
    
    //Mob Specific Options:
    private boolean chargedCreeper;
    
    private DomsItem endermanHolding;
    
    //private Variant horseVariant;
    //private Style horseStyle; 
    //private Color horseColor;
    private boolean horseHasChest;
    private int horseDomestication;
    private double horseJumpStrength;
    
    private boolean golemByPlayer;
    
    private Type ocelotType;
    
    private boolean pigSaddled;
    
    private int pigZombieAnger = -1;
    
    private boolean isShed;
    private DyeColor sheepColor;
    
    private SkeletonType skeletonType;
    
    private int slimeSize = -1;
    
    private boolean isTamed;
    private DomsPlayer tamer;
    
    private Profession villagerProfession;
    
    private boolean isWolfSitting;
    private boolean isWolfAngry;
    private DyeColor wolfCollar;
    
    private boolean isZombieVillager;
    
    //Constructor
    public DomsEntity(EntityType t) {
        this.type = t;
    }
    
    public EntityType getType() {return this.type;}
    
    public void setType(EntityType type) {this.type = type;}
    public void setName(String name) {this.name = name;}
    public void setChild(boolean isChild) {this.child = isChild;}
    public void setCharged(boolean isCharged) {this.chargedCreeper = isCharged;}
    public void setTamed(boolean isTamed) {this.isTamed = isTamed;}
    public void setTamer(DomsPlayer tamer) {this.tamer = tamer;}
    public void setPigZombieAnger(int anger) {this.pigZombieAnger = anger;}
    public void setZombieVillager(boolean isZombieVillager) {this.isZombieVillager = isZombieVillager;}
    public void setWolfCollarColor(DyeColor color) {this.wolfCollar = color;}
    public void setWolfAngry(boolean isAngry) {this.isWolfAngry = isAngry;}
    public void setWoolColor(DyeColor color) {this.sheepColor = color;}
    public void setShorn(boolean isShorn) {this.isShed = isShorn;}
    public void setSkeletonType(SkeletonType type) {this.skeletonType = type;}
    public void setSlimeSize(int size) {this.slimeSize = size;}
    public void setProfession(Profession s) {this.villagerProfession = s;}
    public void setEndermanMaterial(Material m) {this.endermanHolding = new DomsItem(m);}
    public void setEndermanMaterial(DomsItem m) {this.endermanHolding = m;}
    public void setWearingSaddle(boolean isSaddled) {this.pigSaddled = isSaddled;}
    public void setCatType(Type type) {this.ocelotType = type;}
    public void setWolfSitting(boolean isSitting) {this.isWolfSitting = isSitting;}
    
    public Entity spawn(DomsLocation location) {
        if(!location.isWorldLoaded()) return null;
        Entity e = location.getBukkitWorld().spawnEntity(location.getSafeLocation().toLocation(), this.type);
        
        if(e instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) e;
            
            if(name != null) le.setCustomName(Base.trim(this.name, 64));
        }
        
        if(e instanceof Tameable) {
            Tameable t = (Tameable) e;
            
            t.setTamed(this.isTamed);
            if(this.tamer != null) t.setOwner(this.tamer.getOfflinePlayer());
        }
        
        if(e instanceof Ageable) {
            Ageable ag = (Ageable) e;
            if(this.child) ag.setBaby();
            else ag.setAdult();
        }
        
        if(e instanceof Zombie) {
            Zombie zombie = (Zombie) e;
            zombie.setBaby(this.child);
            zombie.setVillager(this.isZombieVillager);
        }
        
        if(e instanceof Wolf) {
            Wolf w = (Wolf) e;
            w.setAngry(this.isWolfAngry);
            w.setSitting(this.isWolfSitting);
            if(this.wolfCollar != null) w.setCollarColor(this.wolfCollar);
        }
        
        if(e instanceof PigZombie) {
            PigZombie pz = (PigZombie) e;
            if(this.pigZombieAnger != -1) pz.setAnger(this.pigZombieAnger);
        }
        
        if(e instanceof Colorable) {
            Colorable c = (Colorable) e;
            if(this.sheepColor != null) c.setColor(this.sheepColor);
        }
        
        if(e instanceof Sheep) {
            Sheep s = (Sheep) e;
            s.setSheared(this.isShed);
        }
        
        if(e instanceof Skeleton) {
            Skeleton s = (Skeleton) e;
            if(this.skeletonType != null) s.setSkeletonType(this.skeletonType);
        }
        
        if(e instanceof Slime) {
            Slime s = (Slime) e;
            if(this.slimeSize != -1) s.setSize(this.slimeSize);
        }
        
        if(e instanceof Villager) {
            Villager v = (Villager) e;
            if(this.villagerProfession != null) v.setProfession(this.villagerProfession);
        }
        
        if(e instanceof Creeper) {
            Creeper c = (Creeper) e;
            c.setPowered(this.chargedCreeper);
        }
        
        if(e instanceof Enderman) {
            Enderman en = (Enderman) e;
            if(this.endermanHolding != null) en.setCarriedMaterial(this.endermanHolding.getMaterialData());
        }
        
        if(e instanceof Pig) {
            Pig p = (Pig) e;
            p.setSaddle(this.pigSaddled);
        }
        
        if(e instanceof Ocelot) {
            Ocelot o = (Ocelot) e;
            if(this.ocelotType != null) o.setCatType(this.ocelotType);
        }
        
        return e;
    }

    public String toHumanString() {
        return Base.capitalizeEachWord(Base.arrayToString(this.type.name().split("_"), " ").toLowerCase());
    }
}
