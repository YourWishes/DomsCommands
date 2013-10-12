/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Enums.PunishmentType;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class Punishment {
    private DomsPlayer player;
    private PunishmentType type;
    private String reason;
    private DomsLocation location;
    private String sender;
    private long date;
    private long endDate;
    private boolean pardoned;
    
    public Punishment(DomsPlayer player, PunishmentType type) {
        this(player, type, null);
    }
    
    public Punishment(DomsPlayer player, PunishmentType type, String reason) {
        this(player, type, reason, null);
    }
    
    public Punishment(DomsPlayer player, PunishmentType type, String reason, DomsLocation location) {
        this(player, type, reason, location, Base.getNow());
    }
    
    public Punishment(DomsPlayer player, PunishmentType type, String reason, DomsLocation location, long date) {
        this(player, type, reason, location, date, -1);
    }
    
    public Punishment(DomsPlayer player, PunishmentType type, String reason, DomsLocation location, long date, long endDate) {
        this.player = player;
        this.type = type;
        this.reason = reason;
        this.location = location;
        this.date = date;
        this.endDate = endDate;
        this.pardoned = false;
    }
    
    public DomsPlayer getPlayer() {return this.player;}
    public PunishmentType getType() {return this.type;}
    public long getDate() {return this.date;}
    public long getEndDate() {return this.endDate;}
    public DomsLocation getLocation() {return this.location;}
    public String getReason() {return this.reason;}
    public String getBanner() {return this.sender;}
    
    public void setBanner(String n) {this.sender = n;}
    public void setReason(String r) {this.reason = r;}
    public void setEndDate(long l) {this.endDate = l;}
    
    public boolean isPermanent() {return this.endDate > date;}
    public boolean isActive() {return !this.pardoned && (this.isPermanent()) || (this.getEndDate() <= Base.getNow());}
    public boolean isPardoned() {return this.pardoned;}
    
    public void isPardoned(boolean t) {this.pardoned = t;}
}
