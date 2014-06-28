/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Enums.PunishmentType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author      Dominic
 * @since       11/10/2013
 */
public class Punishment {
    public static final int PUNS_PER_PAGE = 5;
    public static final String DEFAULT_REASON = "An Unknown Reason";
    
    public static final List<Punishment> PUNISHMENTS = new ArrayList<Punishment>();
    
    public static Punishment getByID(long id) {
        for(Punishment pun : PUNISHMENTS) {
            if(pun.id == id) return pun;
        }
        return null;
    }
    
    private static long nextId = 0;
    
    private long id;
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
        this.id = nextId += 1;
        Punishment.PUNISHMENTS.add(this);
    }
    
    public long getId() {return this.id;}
    public DomsPlayer getPlayer() {return this.player;}
    public PunishmentType getType() {return this.type;}
    public long getDate() {return this.date;}
    public long getEndDate() {return this.endDate;}
    public DomsLocation getLocation() {return (this.location == null ? new DomsLocation(0, 0, 0) : this.location);}
    public String getReason() {if(this.reason == null) return Punishment.DEFAULT_REASON; return this.reason;}
    public String getBanner() {return this.sender;}
    
    public void setBanner(String n) {this.sender = n;}
    public void setReason(String r) {this.reason = r;}
    public void setDate(long l) {this.date = l;}
    public void setEndDate(long l) {this.endDate = l;}
    public void setLocation(DomsLocation location) {this.location = location.copy();}
    public void setLocation(DomsPlayer player) {if(!player.isConsole()) this.setLocation(player.getLocation());}
    
    public boolean isPermanent() {return (this.endDate <= 0) || (this.endDate <= date);}
    public boolean isActive() {
        if(this.pardoned) return false;
        if(this.isPermanent()) return true;
        return this.getEndDate() > Base.getNow();
    }
    public boolean isPardoned() {return this.pardoned;}
    
    public void isPardoned(boolean t) {this.pardoned = t;}

    public void delete() {
        this.player.removePunishment(this);
        PUNISHMENTS.remove(this);
    }
}
