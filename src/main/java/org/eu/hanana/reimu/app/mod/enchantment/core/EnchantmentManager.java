package org.eu.hanana.reimu.app.mod.enchantment.core;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import org.eu.hanana.reimu.app.mod.enchantment.ModFabric;
import org.eu.hanana.reimu.app.mod.enchantment.core.api.IConnectorApi;
import org.eu.hanana.reimu.app.mod.enchantment.event.LoginEvent;
import org.eu.hanana.reimu.app.mod.enchantment.event.MessageEvent;
import org.eu.hanana.reimu.hnnapp.ModLoader;
import org.eu.hanana.reimu.hnnapp.mods.Event;
import org.eu.hanana.reimu.lib.satori.v1.protocol.Login;
import org.eu.hanana.reimu.lib.satori.v1.protocol.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.eu.hanana.reimu.hnnapp.ModLoader.postEvent;

public class EnchantmentManager {
    public final CommandManager commandManager = new CommandManager(this);
    public final Map<String, List<Login>> logins = new ConcurrentHashMap<>(){
        @Override
        public List<Login> get(Object key) {
            if (!this.containsKey(key)&&key instanceof String){
                this.put((String) key,new ArrayList<>());
            }
            return super.get(key);
        }
        @Override
        public boolean containsKey(Object key) {
            return super.get(key) != null;
        }
    };
    public final Map<String, IConnectorApi> connectors = new ConcurrentHashMap<>();
    public static final LogCategory logCategory = LogCategory.create(ModFabric.MOD_ID,"EnchantmentManager");
    @Event
    public void onLoginAdded(LoginEvent.Added event){
        Log.info(logCategory,"add login %s",event.getLogin().toString());
        logins.get(event.getLogin().platform).add(event.getLogin());
    }
    @Event
    public void onLoginRemoved(LoginEvent.Removed event){
        logins.get(event.getLogin().platform).remove(event.getLogin());
        Log.info(logCategory,"remove login %s",event.getLogin().toString());
    }
    @Event
    public void onLoginUpdated(LoginEvent.Updated event){
        Log.info(logCategory,"update login %s",event.getLogin().toString());
        var f = true;
        var logins = this.logins.get(event.getLogin().platform);
        for (Login login : logins) {
            if (login.equals(event.getLogin())){
                f=false;
                login.user=event.getLogin().user;
                login.status=event.getLogin().status;
                login.platform=event.getLogin().platform;
                login.adapter=event.getLogin().adapter;
                login.features=event.getLogin().features;
                break;
            }
        }
        if (f) {
            onLoginAdded(new LoginEvent.Added(event.getLogin()));
        }
    }
    @Event
    public void onMessageCreated(MessageEvent.Created event){
        if (event.messageEvent.getMessage().content.startsWith("/")){
            postEvent(Util.parseCommand(event.messageEvent.getMessage().content, event.messageEvent));
        }
    }
    public Login findLogin(@NotNull Login login){
        if (!logins.containsKey(login.platform)) return null;
        List<Login> logins1 = logins.get(login.platform);
        for (Login login1 : logins1) {
            if (login.equals(login1)){
                return login1;
            }
        }
        return null;
    }
    public IConnectorApi findConnector(@NotNull Login login){
        if (!logins.containsKey(login.platform)) return null;
        return connectors.get(login.platform);
    }
    public void sendMessage(String message,String channel,Login login) {
        login=findLogin(login);
        findConnector(login).sendMessage(message,channel,login);
    }
}
