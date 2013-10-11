/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.domsplace.DomsCommands.Enums;

import com.domsplace.DomsCommands.Bases.DomsEnum;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class PunishmentType extends DomsEnum {
    public static final PunishmentType BAN = new PunishmentType("Ban");
    public static final PunishmentType KICK = new PunishmentType("Kick");
    public static final PunishmentType MUTE = new PunishmentType("Mute");
    public static final PunishmentType WARN = new PunishmentType("Warning");
    
    //Instance
    private String type;
    
    private PunishmentType(String type) {
        this.type = type;
    }
    
    public String getType() {return this.type;}
}
