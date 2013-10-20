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

package com.domsplace.DomsCommands;

import com.domsplace.DomsCommands.Commands.TeleportCommands.*;
import com.domsplace.DomsCommands.Commands.WarpCommands.*;
import com.domsplace.DomsCommands.Bases.*;
import com.domsplace.DomsCommands.Commands.*;
import com.domsplace.DomsCommands.Commands.PlayerCommands.*;
import com.domsplace.DomsCommands.Commands.PunishmentCommands.*;
import com.domsplace.DomsCommands.Listeners.*;
import com.domsplace.DomsCommands.Threads.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author      Dominic
 * @since       04/10/2013
 */
public class DomsCommandsPlugin extends JavaPlugin {
    private boolean enabled = false;
    
    //Commands
    private DoNothingCommand doNothingCommand;
    private DomsCommandsCommand domsCommands;
    private MOTDCommand motdCommand;
    private ShutdownCommand stopCommand;
    
    private AwayCommand awayCommand;
    private GamemodeCommand gamemodeCommand;
    private GiveCommand giveCommand;
    private GetIDCommand getIDCommand;
    private MessageCommand messageCommand;
    private NicknameCommand nicknameCommand;
    private PingCommand pingCommand;
    private ReplyCommand replyCommand;
    
    private TeleportRequestCommand tpaCommand;
    private TeleportRequestHereCommand tpahCommand;
    private TeleportAcceptCommand tpacceptCommand;
    private TeleportDenyCommand tpdenyCommand;
    private BackCommand backCommand;
    private TeleportCommand tpCommand;
    private TeleportHereCommand tphereCommand;
    private TeleportAllCommand tpallCommand;
    private TeleportRequestAllCommand tpaallCommand;
    
    private SetWarpCommand setWarpCommand;
    private WarpCommand warpCommand;
    private DeleteWarpCommand delwarpCommand;
    
    private BanCommand banCommand;
    private PardonCommand pardonCommand;
    private KickCommand kickCommand;
    
    //Listeners
    private PlayerRegisterListener playerRegisterListener;
    private PlayerNotificationListener playerNotificationListener;
    private CustomEventCommandListener customEventCommandListener;
    private CustomPlayerListener customPlayerListener;
    private PlayDirtyListener playDirtyListener;
    private EventCommandListener eventCommandListener;
    private PlayerAwayListener playerAwayListener;
    
    //Threads
    private ConfigSaveThread configSaveThread;
    private AddPlayerTimeThread playerTimeThread;
    private PlayerAwayThread playerAwayThread;
    
    @Override
    public void onEnable() {
        //Register Plugin
        Base.setPlugin(this);
        
        //Load Data
        if(!DataManager.loadAll()) {
            this.disable();
            return;
        }
        
        //Load Commands
        this.doNothingCommand = new DoNothingCommand();
        this.domsCommands = new DomsCommandsCommand();
        this.motdCommand = new MOTDCommand();
        this.stopCommand = new ShutdownCommand();
        
        this.awayCommand = new AwayCommand();
        this.gamemodeCommand = new GamemodeCommand();
        this.giveCommand = new GiveCommand();
        this.getIDCommand = new GetIDCommand();
        this.messageCommand = new MessageCommand();
        this.nicknameCommand = new NicknameCommand();
        this.pingCommand = new PingCommand();
        this.replyCommand = new ReplyCommand();
        
        this.tpaCommand = new TeleportRequestCommand();
        this.tpahCommand = new TeleportRequestHereCommand();
        this.tpacceptCommand = new TeleportAcceptCommand();
        this.tpdenyCommand = new TeleportDenyCommand();
        this.backCommand = new BackCommand();
        this.tpCommand = new TeleportCommand();
        this.tphereCommand = new TeleportHereCommand();
        this.tpallCommand = new TeleportAllCommand();
        this.tpaallCommand = new TeleportRequestAllCommand();
        
        this.setWarpCommand = new SetWarpCommand();
        this.warpCommand = new WarpCommand();
        this.delwarpCommand = new DeleteWarpCommand();
        
        this.banCommand = new BanCommand();
        this.pardonCommand = new PardonCommand();
        this.kickCommand = new KickCommand();
        
        //Load Listeners
        this.playerRegisterListener = new PlayerRegisterListener();
        this.playerNotificationListener = new PlayerNotificationListener();
        this.customEventCommandListener = new CustomEventCommandListener();
        this.customPlayerListener = new CustomPlayerListener();
        this.playDirtyListener = new PlayDirtyListener();
        this.eventCommandListener = new EventCommandListener();
        this.playerAwayListener = new PlayerAwayListener();
        
        //Load Threads
        this.configSaveThread = new ConfigSaveThread();
        this.playerTimeThread = new AddPlayerTimeThread();
        this.playerAwayThread = new PlayerAwayThread();
        
        this.enabled = true;
        Base.debug("Enabled \"" + this.getName() + "\" Successsfully!");
    }
    
    @Override
    public void onDisable() {
        if(!enabled) {
            Base.debug("Failed to Enable " + this.getName());
            return;
        }
        
        DomsThread.stopAllThreads();
        
        DataManager.saveAll();
    }
    
    public void disable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
