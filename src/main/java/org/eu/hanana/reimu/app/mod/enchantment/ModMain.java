package org.eu.hanana.reimu.app.mod.enchantment;

import lombok.Getter;
import lombok.SneakyThrows;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import org.eu.hanana.reimu.app.mod.enchantment.connector.SatoriCfg;
import org.eu.hanana.reimu.app.mod.enchantment.connector.SatoriManager;
import org.eu.hanana.reimu.app.mod.enchantment.core.ConfigCore;
import org.eu.hanana.reimu.app.mod.enchantment.core.EnchantmentManager;
import org.eu.hanana.reimu.app.mod.enchantment.core.internal.HelpCommand;
import org.eu.hanana.reimu.app.mod.enchantment.core.internal.StatusCommand;
import org.eu.hanana.reimu.app.mod.webui.config.WebuiCfgCore;
import org.eu.hanana.reimu.app.mod.webui.event.WebUiCreatedEvent;
import org.eu.hanana.reimu.hnnapp.ModLoader;
import org.eu.hanana.reimu.hnnapp.mods.Event;
import org.eu.hanana.reimu.hnnapp.mods.ModEntry;
import org.eu.hanana.reimu.hnnapp.mods.events.AfterInfoEvent;
import org.eu.hanana.reimu.hnnapp.mods.events.ExitEvent;
import org.eu.hanana.reimu.hnnapp.mods.events.PostInitModsEvent;

import static org.eu.hanana.reimu.app.mod.webui.ModFabric.MOD_ID_Legacy;

@ModEntry(name = ModFabric.MOD_ID,id = ModMain.MOD_ID)
public class ModMain {
    public static final String MOD_ID = ModFabric.MOD_ID+"_legacy";
    public static final LogCategory category = LogCategory.createCustom(ModFabric.MOD_ID);
    @Getter
    protected static ModMain instance;
    @Getter
    protected final EnchantmentManager enchantmentManager = new EnchantmentManager();
    @Getter
    protected final SatoriManager satoriManager = new SatoriManager();
    @Event
    public void onPostInitModsEvent(PostInitModsEvent event){
        ModLoader.getLoader().regCfgCore(MOD_ID,new ConfigCore());
    }
    @Event
    public void onAfterInfoEvent(AfterInfoEvent event){
        enchantmentManager.commandManager.commandListeners.add(new HelpCommand());
        enchantmentManager.commandManager.commandListeners.add(new StatusCommand());
    }
    @SneakyThrows
    @Event
    public void onWebUiCreated(WebUiCreatedEvent event){
        satoriManager.openAll();
    }
    public ModMain(){
        instance=this;
        ModLoader.getLoader().regEventBuses(this);
        ModLoader.getLoader().regEventBuses(enchantmentManager);
        ModLoader.getLoader().regEventBuses(satoriManager);
        ModLoader.getLoader().regEventBuses(enchantmentManager.commandManager);
        Log.info(category,"starting...");
    }
}