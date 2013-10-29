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

import com.domsplace.DomsCommands.Commands.PlayerCommands.HomeCommands.DeleteHomeCommand;
import com.domsplace.DomsCommands.Commands.PlayerCommands.HomeCommands.HomesCommand;
import com.domsplace.DomsCommands.Commands.PlayerCommands.HomeCommands.HomeCommand;
import com.domsplace.DomsCommands.Commands.PlayerCommands.HomeCommands.SetHomeCommand;
import com.domsplace.DomsCommands.Commands.ItemCommands.*;
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
    private CheckLagCommand checkLagCommand;
    private DoNothingCommand doNothingCommand;
    private DomsCommandsCommand domsCommands;
    private FeedCommand feedCommand;
    private HealCommand healCommand;
    private HelpCommand helpCommand;
    private KitCommand kitCommand;
    private MOTDCommand motdCommand;
    private PotionEffectCommand potionEffectCommand;
    private RulesCommand rulesCommand;
    private ServerCommand serverCommand;
    private ShutdownCommand stopCommand;
    private SpawnCommand spawnCommand;
    private SpawnMobCommand spawnMobCommand;
    private WhoCommand whoCommand;
    private WhoisCommand whoisCommand;
    
    private AwayCommand awayCommand;
    private BackpackCommand backpackCommand;
    private ChatChannelCommand chatChannelCommand;
    private FlyCommand flyCommand;
    private DeleteHomeCommand deleteHomeCommand;
    private GamemodeCommand gamemodeCommand;
    private InvmodCommand invmodCommand;
    private MeCommand meCommand;
    private HomeCommand homeCommand;
    private HomesCommand homesCommand;
    private MessageCommand messageCommand;
    private NicknameCommand nicknameCommand;
    private PingCommand pingCommand;
    private ReplyCommand replyCommand;
    private SlapCommand slapCommand;
    private SetHomeCommand setHomeCommand;
    private TimeCommand timeCommand;
    private WeatherCommand weatherCommand;
    
    private AddLoresCommand addLoresCommand;
    private ClearInventoryCommand clearInventoryCommand;
    private ClearLoresCommand clearLoresCommand;
    private EnchantCommand enchantCommand;
    private ExperienceCommand experienceCommand;
    private EnchantingTableCommand enchantingTableCommand;
    private GiveCommand giveCommand;
    private HeadCommand headCommand;
    private ItemCommand itemCommand;
    private GetIDCommand getIDCommand;
    private MoreCommand moreCommand;
    private RenameItemCommand renameItemCommand;
    
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
    private MuteCommand muteCommand;
    private WarnCommand warnCommand;
    
    //Listeners
    private PlayerRegisterListener playerRegisterListener;
    private PlayerNotificationListener playerNotificationListener;
    private CustomEventCommandListener customEventCommandListener;
    private CustomPlayerListener customPlayerListener;
    private PlayDirtyListener playDirtyListener;
    private EventCommandListener eventCommandListener;
    private PlayerAwayListener playerAwayListener;
    private PunishmentListener punishmentListener;
    private DomsChatListener domsChatListener;
    private ServerUnloadListener serverUnloadListener;
    
    //Threads
    private ConfigSaveThread configSaveThread;
    private AddPlayerTimeThread playerTimeThread;
    private PlayerAwayThread playerAwayThread;
    private ServerTPSThread serverTPSThread;
    
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
        this.checkLagCommand = new CheckLagCommand();
        this.doNothingCommand = new DoNothingCommand();
        this.domsCommands = new DomsCommandsCommand();
        this.feedCommand = new FeedCommand();
        this.healCommand = new HealCommand();
        this.helpCommand = new HelpCommand();
        this.kitCommand = new KitCommand();
        this.motdCommand = new MOTDCommand();
        this.potionEffectCommand = new PotionEffectCommand();
        this.rulesCommand = new RulesCommand();
        this.serverCommand = new ServerCommand();
        this.stopCommand = new ShutdownCommand();
        this.spawnCommand = new SpawnCommand();
        this.spawnMobCommand = new SpawnMobCommand();
        this.whoCommand = new WhoCommand();
        this.whoisCommand = new WhoisCommand();
        
        this.awayCommand = new AwayCommand();
        this.backpackCommand = new BackpackCommand();
        this.chatChannelCommand = new ChatChannelCommand();
        this.flyCommand = new FlyCommand();
        this.deleteHomeCommand = new DeleteHomeCommand();
        this.gamemodeCommand = new GamemodeCommand();
        this.invmodCommand = new InvmodCommand();
        this.meCommand = new MeCommand();
        this.homeCommand = new HomeCommand();
        this.homesCommand = new HomesCommand();
        this.messageCommand = new MessageCommand();
        this.nicknameCommand = new NicknameCommand();
        this.pingCommand = new PingCommand();
        this.replyCommand = new ReplyCommand();
        this.slapCommand = new SlapCommand();
        this.setHomeCommand = new SetHomeCommand();
        this.timeCommand = new TimeCommand();
        this.weatherCommand = new WeatherCommand();
        
        this.addLoresCommand = new AddLoresCommand();
        this.clearInventoryCommand = new ClearInventoryCommand();
        this.clearLoresCommand = new ClearLoresCommand();
        this.enchantCommand = new EnchantCommand();
        this.enchantingTableCommand = new EnchantingTableCommand();
        this.experienceCommand = new ExperienceCommand();
        this.giveCommand = new GiveCommand();
        this.headCommand = new HeadCommand();
        this.itemCommand = new ItemCommand();
        this.getIDCommand = new GetIDCommand();
        this.moreCommand = new MoreCommand();
        this.renameItemCommand = new RenameItemCommand();
        
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
        this.muteCommand = new MuteCommand();
        this.warnCommand = new WarnCommand();
        
        //Load Listeners
        this.playerRegisterListener = new PlayerRegisterListener();
        this.playerNotificationListener = new PlayerNotificationListener();
        this.customEventCommandListener = new CustomEventCommandListener();
        this.customPlayerListener = new CustomPlayerListener();
        this.playDirtyListener = new PlayDirtyListener();
        this.eventCommandListener = new EventCommandListener();
        this.playerAwayListener = new PlayerAwayListener();
        this.punishmentListener = new PunishmentListener();
        this.domsChatListener = new DomsChatListener();
        this.serverUnloadListener = new ServerUnloadListener();
        
        //Load Threads
        this.configSaveThread = new ConfigSaveThread();
        this.playerTimeThread = new AddPlayerTimeThread();
        this.playerAwayThread = new PlayerAwayThread();
        this.serverTPSThread = new ServerTPSThread();
        
        PluginHook.hookAll();
        
        this.enabled = true;
        Base.log("Finished Loading " + this.getName() + ", " + BukkitCommand.getCommands().size() + " commands registered.");
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
