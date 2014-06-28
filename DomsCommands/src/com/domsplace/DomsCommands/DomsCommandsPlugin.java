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

import com.domsplace.DomsCommands.Bases.*;
import com.domsplace.DomsCommands.Commands.*;
import com.domsplace.DomsCommands.Exceptions.SaveResult;
import com.domsplace.DomsCommands.Listeners.*;
import com.domsplace.DomsCommands.Objects.DomsCommandsAddon;
import static com.domsplace.DomsCommands.Objects.DomsPlayer.*;
import com.domsplace.DomsCommands.Threads.*;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author      Dominic
 * @since       04/10/2013
 */
public class DomsCommandsPlugin extends JavaPlugin {
    private boolean enabled = false;
    
    //Commands
    private AddLoresCommand addLoresCommand;
    private AwayCommand awayCommand;
    private BackCommand backCommand;
    private BackpackCommand backpackCommand;
    private BanCommand banCommand;
    private BansCommand bansCommand;
    private ChatChannelCommand chatChannelCommand;
    private CheckLagCommand checkLagCommand;
    private ClearInventoryCommand clearInventoryCommand;
    private ClearLoresCommand clearLoresCommand;
    private DeleteHomeCommand deleteHomeCommand;
    private DeleteWarpCommand delwarpCommand;
    private DomsCommandsCommand domsCommands;
    private DoNothingCommand doNothingCommand;
    private DumpCommand dumpCommand;
    private EnchantCommand enchantCommand;
    private EnchantingTableCommand enchantingTableCommand;
    private EnderchestCommand enderchestCommand;
    private ExperienceCommand experienceCommand;
    private FeedCommand feedCommand;
    private FixChunkCommand fixChunkCommand;
    private FlyCommand flyCommand;
    private FurnaceCommand furnaceCommand;
    private GamemodeCommand gamemodeCommand;
    private GetIDCommand getIDCommand;
    private GiveCommand giveCommand;
    private HeadCommand headCommand;
    private HealCommand healCommand;
    private HelmetCommand helmetCommand;
    private HelpCommand helpCommand;
    private HomeCommand homeCommand;
    private HomesCommand homesCommand;
    private HugCommand hugCommand;
    private InvmodCommand invmodCommand;
    private ItemCommand itemCommand;
    private KickAllCommand kickAllCommand;
    private KickCommand kickCommand;
    private KicksCommand kicksCommand;
    private KillCommand killCommand;
    private KissCommand kissCommand;
    private KitCommand kitCommand;
    private MeCommand meCommand;
    private MegaSmiteCommand megaSmiteCommand;
    private MessageCommand messageCommand;
    private MoreCommand moreCommand;
    private MOTDCommand motdCommand;
    private MuteCommand muteCommand;
    private MutesCommand mutesCommand;
    private NameplateCommand nameplateCommand;
    private NicknameCommand nicknameCommand;
    private PardonCommand pardonCommand;
    private PingCommand pingCommand;
    private PlayerHomeCommand playerHomeCommand;
    private PositionCommand positionCommand;
    private PotionEffectCommand potionEffectCommand;
    private RealnameCommand realnameCommand;
    private RecipeCommand recipeCommand;
    private RenameItemCommand renameItemCommand;
    private ReplyCommand replyCommand;
    private RulesCommand rulesCommand;
    private ServerCommand serverCommand;
    private SetHomeCommand setHomeCommand;
    private SetSpawnCommand setSpawnCommand;
    private SetWarpCommand setWarpCommand;
    private ShutdownCommand stopCommand;
    private SlapCommand slapCommand;
    private SmiteCommand smiteCommand;
    private SpawnCommand spawnCommand;
    private SpawnMobCommand spawnMobCommand;
    private TeleportAcceptCommand tpacceptCommand;
    private TeleportAllCommand tpallCommand;
    private TeleportCommand tpCommand;
    private TeleportDenyCommand tpdenyCommand;
    private TeleportHereCommand tphereCommand;
    private TeleportRequestAllCommand tpaallCommand;
    private TeleportRequestCommand tpaCommand;
    private TeleportRequestHereCommand tpahCommand;
    private TellRawCommand tellRawCommand;
    private TimeCommand timeCommand;
    private WarnCommand warnCommand;
    private WarnsCommand warnsCommand;
    private WarpCommand warpCommand;
    private WeatherCommand weatherCommand;
    private WhoCommand whoCommand;
    private WhoisCommand whoisCommand;
    private WorkbenchCommand workbenchCommand;
    
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
    private SignListener signListener;
    private DomsInventoryListener domsInventoryListener;
    
    //Threads
    private ConfigSaveThread configSaveThread;
    private AddPlayerTimeThread playerTimeThread;
    private PlayerAwayThread playerAwayThread;
    public ServerCPUThread serverCPUThread;
    private ServerTPSThread serverTPSThread;
    private UpdateThread updateThread;
    
    @Override
    public void onEnable() {
        //Register Plugin
        Base.setPlugin(this);
        
        //1.7.9: Test for the new 1.7.9 UUID methods
        boolean v = false;
        try {
            OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(DOMIN8TRIX25_UUID));
            v = p != null;
        } catch(Throwable t) {
            this.disable();
            Base.error("This version of DomsCommands is designed for Minecraft/Bukkit 1.7.9 and higher only.", t);
            
        }
        if(!v) return;
        
        //Load Data
        if(!DataManager.loadAll()) {
            this.disable();
            return;
        }
        
        //Load Commands
        this.addLoresCommand = new AddLoresCommand();
        this.awayCommand = new AwayCommand();
        this.backCommand = new BackCommand();
        this.backpackCommand = new BackpackCommand();
        this.banCommand = new BanCommand();
        this.bansCommand = new BansCommand();
        this.chatChannelCommand = new ChatChannelCommand();
        this.checkLagCommand = new CheckLagCommand();
        this.clearInventoryCommand = new ClearInventoryCommand();
        this.clearLoresCommand = new ClearLoresCommand();
        this.deleteHomeCommand = new DeleteHomeCommand();
        this.delwarpCommand = new DeleteWarpCommand();
        this.domsCommands = new DomsCommandsCommand();
        this.doNothingCommand = new DoNothingCommand();
        this.dumpCommand = new DumpCommand();
        this.enchantCommand = new EnchantCommand();
        this.enchantingTableCommand = new EnchantingTableCommand();
        this.enderchestCommand = new EnderchestCommand();
        this.experienceCommand = new ExperienceCommand();
        this.feedCommand = new FeedCommand();
        this.fixChunkCommand = new FixChunkCommand();
        this.flyCommand = new FlyCommand();
        this.furnaceCommand = new FurnaceCommand();
        this.gamemodeCommand = new GamemodeCommand();
        this.getIDCommand = new GetIDCommand();
        this.giveCommand = new GiveCommand();
        this.headCommand = new HeadCommand();
        this.healCommand = new HealCommand();
        this.helmetCommand = new HelmetCommand();
        this.helpCommand = new HelpCommand();
        this.homeCommand = new HomeCommand();
        this.homesCommand = new HomesCommand();
        this.hugCommand = new HugCommand();
        this.invmodCommand = new InvmodCommand();
        this.itemCommand = new ItemCommand();
        this.kickAllCommand = new KickAllCommand();
        this.kickCommand = new KickCommand();
        this.kicksCommand = new KicksCommand();
        this.killCommand = new KillCommand();
        this.kissCommand = new KissCommand();
        this.kitCommand = new KitCommand();
        this.meCommand = new MeCommand();
        this.megaSmiteCommand = new MegaSmiteCommand();
        this.messageCommand = new MessageCommand();
        this.moreCommand = new MoreCommand();
        this.motdCommand = new MOTDCommand();
        this.muteCommand = new MuteCommand();
        this.mutesCommand = new MutesCommand();
        this.nameplateCommand = new NameplateCommand();
        this.nicknameCommand = new NicknameCommand();
        this.pardonCommand = new PardonCommand();
        this.pingCommand = new PingCommand();
        this.playerHomeCommand = new PlayerHomeCommand();
        this.positionCommand = new PositionCommand();
        this.potionEffectCommand = new PotionEffectCommand();
        this.realnameCommand = new RealnameCommand();
        this.recipeCommand = new RecipeCommand();
        this.renameItemCommand = new RenameItemCommand();
        this.replyCommand = new ReplyCommand();
        this.rulesCommand = new RulesCommand();
        this.serverCommand = new ServerCommand();
        this.setHomeCommand = new SetHomeCommand();
        this.setSpawnCommand = new SetSpawnCommand();
        this.setWarpCommand = new SetWarpCommand();
        this.slapCommand = new SlapCommand();
        this.smiteCommand = new SmiteCommand();
        this.spawnCommand = new SpawnCommand();
        this.spawnMobCommand = new SpawnMobCommand();
        this.stopCommand = new ShutdownCommand();
        this.tellRawCommand = new TellRawCommand();
        this.timeCommand = new TimeCommand();
        this.tpaallCommand = new TeleportRequestAllCommand();
        this.tpacceptCommand = new TeleportAcceptCommand();
        this.tpaCommand = new TeleportRequestCommand();
        this.tpahCommand = new TeleportRequestHereCommand();
        this.tpallCommand = new TeleportAllCommand();
        this.tpCommand = new TeleportCommand();
        this.tpdenyCommand = new TeleportDenyCommand();
        this.tphereCommand = new TeleportHereCommand();
        this.warnCommand = new WarnCommand();
        this.warnsCommand = new WarnsCommand();
        this.warpCommand = new WarpCommand();
        this.weatherCommand = new WeatherCommand();
        this.whoCommand = new WhoCommand();
        this.whoisCommand = new WhoisCommand();
        this.workbenchCommand = new WorkbenchCommand();
        
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
        this.signListener = new SignListener();
        this.domsInventoryListener = new DomsInventoryListener();
        
        //Load Threads
        this.configSaveThread = new ConfigSaveThread();
        this.playerTimeThread = new AddPlayerTimeThread();
        this.playerAwayThread = new PlayerAwayThread();
        this.serverCPUThread = new ServerCPUThread();
        this.serverTPSThread = new ServerTPSThread();
        this.updateThread = new UpdateThread();
        
        PluginHook.hookAll();
        
        //Invoke Silly Things
        DomsCommandsAddon.invoke();
        SaveResult.FILE_CREATE_FAILED.getResult();
        
        this.enabled = true;
        Base.log("Finished Loading " + this.getName() + ", " + BukkitCommand.getCommands().size() + " commands registered.");
    }
    
    @Override
    public void onDisable() {
        if(!enabled) {
            return;
        }
        
        DomsThread.stopAllThreads();
        DataManager.saveAll();
        
        for(DomsCommandsAddon addon : new ArrayList<DomsCommandsAddon>(DomsCommandsAddon.ADDONS)) {
            addon.disable();
        }
    }
    
    public void disable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
