package com.domsplace.DomsCommands.Listeners;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Bases.DomsListener;
import com.domsplace.DomsCommands.Objects.DomsPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class TagAPILegacyListener extends DomsListener {
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    @Deprecated
    public void handlePlayerNames(PlayerReceiveNameTagEvent e) {
        Base.debug(e.getPlayer().getName() + " received " + e.getNamedPlayer().getName() + "'s nameplate.");
        DomsPlayer player = DomsPlayer.getDomsPlayerFromPlayer(e.getNamedPlayer());
        String plate = player.getNamePlate();
        if(plate == null) plate = player.getUsername();
        if(plate.equalsIgnoreCase("off")) plate = player.getUsername();
        if(plate.replaceAll(" ", "").equalsIgnoreCase("")) plate = player.getUsername();
        e.setTag(plate);
    }
}
