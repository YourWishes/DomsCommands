package com.domsplace.DomsCommands.Events;

import com.domsplace.DomsCommands.Bases.DomsCancellableEvent;
import java.util.List;
import org.bukkit.command.CommandSender;

public class PreCommandEvent extends DomsCancellableEvent {
    
    private CommandSender player;
    private String command;
    private List<String> args;
    
    public PreCommandEvent(CommandSender player, String command, List<String> args) {
        this.player = player;
        this.command = command;
        this.args = args;
    }
    
    public CommandSender getPlayer() {return this.player;}
    public String getCommand() {return this.command;}
    public List<String> getArgs() {return this.args;}
    
    public String toFullCommand() {
        String s = this.command;
        for(String str : args) {
            s += " " + str;
        }
        
        return s;
    }
}
