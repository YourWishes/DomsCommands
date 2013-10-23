/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.domsplace.DomsCommands.Enums;

import com.domsplace.DomsCommands.Bases.DomsEnum;
import java.util.ArrayList;
import java.util.List;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class PunishmentType extends DomsEnum {
    private static final List<PunishmentType> TYPES = new ArrayList<PunishmentType>();
    
    public static final PunishmentType BAN = new PunishmentType("Ban");
    public static final PunishmentType KICK = new PunishmentType("Kick");
    public static final PunishmentType MUTE = new PunishmentType("Mute");
    public static final PunishmentType WARN = new PunishmentType("Warning");

    public static PunishmentType getType(String type) {
        for(PunishmentType t : TYPES) {
            if(t.type.equalsIgnoreCase(type)) return t;
        }
        return null;
    }
    
    //Instance
    private String type;
    
    private PunishmentType(String type) {
        this.type = type;
        TYPES.add(this);
    }
    
    public String getType() {return this.type;}
}
