package org.eu.hanana.reimu.app.mod.enchantment.connector;

import com.google.gson.internal.LinkedTreeMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import net.fabricmc.loader.impl.util.log.Log;
import org.eu.hanana.reimu.app.mod.enchantment.ModMain;
import org.eu.hanana.reimu.app.mod.enchantment.connector.event.SatoriEvent;
import org.eu.hanana.reimu.app.mod.enchantment.core.Util;
import org.eu.hanana.reimu.app.mod.enchantment.core.api.IConnectorApi;
import org.eu.hanana.reimu.app.mod.enchantment.event.CommandEvent;
import org.eu.hanana.reimu.app.mod.enchantment.event.LoginEvent;
import org.eu.hanana.reimu.hnnapp.ModLoader;
import org.eu.hanana.reimu.hnnapp.mods.Event;
import org.eu.hanana.reimu.lib.satori.v1.client.AuthenticatorC;
import org.eu.hanana.reimu.lib.satori.v1.client.SatoriClient;
import org.eu.hanana.reimu.lib.satori.v1.protocol.Login;
import org.eu.hanana.reimu.lib.satori.v1.protocol.SignalBodyEvent;
import org.eu.hanana.reimu.lib.satori.v1.protocol.SignalBodyIdentify;
import org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.EventType;
import org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.InteractionEvent;
import org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.MessageEvent;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.eu.hanana.reimu.app.mod.enchantment.core.Util.parseCommand;
import static org.eu.hanana.reimu.hnnapp.ModLoader.postEvent;

public class SatoriManager implements Closeable, IConnectorApi {
    public final List<SatoriClient> satoriClients = new CopyOnWriteArrayList<>();
    public void openAll(){
        this.reload();
    }

    public void reload() {
        this.close();
        for (LinkedTreeMap<String,String> configItem : SatoriCfg.cfg) {
            var sc = SatoriClient.createSatoriClient(configItem.get("address"));
            satoriClients.add(sc);
            sc.addAuthenticator(new Authenticator(new SignalBodyIdentify(configItem.get("token")),this,sc));
            sc.addEventListener(new SatoriEventBridge(sc));
            sc.open();
        }
    }

    @Override
    public void close() {
        List<Login> allLogins = getAllLogins();
        for (Login allLogin : allLogins) {
            Map<String, IConnectorApi> connectors = ModMain.getInstance().getEnchantmentManager().connectors;
            connectors.remove(allLogin.platform);
        }
        for (SatoriClient satoriClient : satoriClients) {
            new Thread(satoriClient::close).start();
        }
        satoriClients.clear();
    }
    @Event
    public void onSCEvent(SatoriEvent.Event event){
        event= new SatoriEvent.Event(event.client,event.eventType,Util.copyObj(event.signalBodyEvent));
        event.signalBodyEvent.login=Util.copyObj(event.signalBodyEvent.login);
        event.signalBodyEvent.login.platform+="$@$"+event.client.hashCode();
        final EventType<? extends SignalBodyEvent> eventType = event.eventType;
        SatoriEvent.Event finalEvent = event;
        var type = event.eventType;
        Mono.create(monoSink -> {
            if (eventType.equals(EventType.message_created)){
                MessageEvent signalBodyEvent = (MessageEvent) finalEvent.signalBodyEvent;
                postEvent(new org.eu.hanana.reimu.app.mod.enchantment.event.MessageEvent.Created(signalBodyEvent));
            }else if (type.equals(EventType.login_removed)) {
                var login = Util.copyObj(finalEvent.signalBodyEvent.login);
                postEvent(new LoginEvent.Removed(login));
            } else if (type.equals(EventType.login_added)) {
                var login = Util.copyObj(finalEvent.signalBodyEvent.login);
                postEvent(new LoginEvent.Added(login));
            } else if (type.equals(EventType.login_updated)) {
                var login = Util.copyObj(finalEvent.signalBodyEvent.login);
                postEvent(new LoginEvent.Updated(login));
            } else if (type.equals(EventType.interaction_command)) {
                var ce = ((InteractionEvent.InteractionEventCommand) finalEvent.signalBodyEvent);
                ce.message.channel = ce.channel;
                ce.message.user = ce.user;
                ce.message.guild = ce.guild;
                ce.message.member = ce.member;
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.login=ce.login;
                messageEvent.user=ce.user;
                messageEvent.member=ce.member;
                messageEvent.message=ce.message;
                messageEvent.channel=ce.channel;
                ModLoader.postEvent(parseCommand(ce.message.content,messageEvent));
            }
            monoSink.success();
        }).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }

    public void onOpened(SatoriClient client) {
        List<Login> allLogins = getAllLogins();
        for (Login allLogin : allLogins) {
            postEvent(new LoginEvent.Updated(allLogin));
            Map<String, IConnectorApi> connectors = ModMain.getInstance().getEnchantmentManager().connectors;
            if (!connectors.containsKey(allLogin.platform)) connectors.put(allLogin.platform,this);
        }
    }

    @Override
    public List<Login> getAllLogins() {
        List<Login> l = new ArrayList<>();
        for (SatoriClient satoriClient : satoriClients) {
            if (satoriClient.loginData==null) continue;
            for (Login login : satoriClient.loginData.logins) {
                Login login1 = new Login();
                login1.platform=login.platform+"$@$"+satoriClient.hashCode();
                login1.adapter=login.adapter;
                login1.features=login.features;
                login1.user=login.user;
                login1.sn=login.sn;
                login1.status=login.status;
                l.add(login1);
            }
        }
        return l;
    }
    public SatoriClient getClient(Login login){
        for (SatoriClient satoriClient : satoriClients) {
            if (satoriClient.hashCode()==Integer.parseInt(login.platform.split("\\$@\\$")[1])){
                return satoriClient;
            }
        }
        return null;
    }
    public Login getRawLogin(Login login){
        Login login1 = new Login();
        login1.platform=login.platform.split("\\$@\\$")[0];
        login1.adapter=login.adapter;
        login1.features=login.features;
        login1.user=login.user;
        login1.sn=login.sn;
        login1.status=login.status;
        return login1;
    }
    @Override
    public void sendMessage(String message,String channel, Login login) {
        getClient(login).getClientApi().getMessageApi().create(getRawLogin(login),channel,message).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
