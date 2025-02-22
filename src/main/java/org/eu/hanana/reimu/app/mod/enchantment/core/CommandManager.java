package org.eu.hanana.reimu.app.mod.enchantment.core;

import com.google.common.collect.Lists;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import org.eu.hanana.reimu.app.mod.enchantment.core.api.ICommandListener;
import org.eu.hanana.reimu.app.mod.enchantment.event.CommandEvent;
import org.eu.hanana.reimu.hnnapp.mods.Event;
import org.eu.hanana.reimu.hnnapp.mods.events.AfterInfoEvent;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    protected final EnchantmentManager enchantmentManager;
    public static final LogCategory logCategory = LogCategory.createCustom(EnchantmentManager.logCategory.name,"CommandManager");
    public final List<ICommandListener> commandListeners = Lists.newCopyOnWriteArrayList();
    public CommandManager(EnchantmentManager enchantmentManager) {
        this.enchantmentManager=enchantmentManager;
    }
    public void execute(CommandEvent event,boolean sync){
        for (ICommandListener commandListener : commandListeners) {
            if (commandListener.isAccept(event.command)) {
                Mono.create(monoSink -> {
                    try {
                        commandListener.process(event);
                    } catch (Throwable e) {
                        monoSink.error(e);
                    }
                    monoSink.success();
                }).subscribeOn(sync?Schedulers.immediate():Schedulers.boundedElastic()).doOnError(throwable -> {
                    Log.error(logCategory,"Error handling command : "+event.command,throwable);
                }).subscribe();
            }
        }
    }
    @Event
    public void onCommand(CommandEvent event){
        execute(event,false);
    }
}
