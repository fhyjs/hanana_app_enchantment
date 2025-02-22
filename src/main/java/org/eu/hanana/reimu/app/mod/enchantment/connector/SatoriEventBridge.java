package org.eu.hanana.reimu.app.mod.enchantment.connector;

import lombok.RequiredArgsConstructor;
import org.eu.hanana.reimu.app.mod.enchantment.ModMain;
import org.eu.hanana.reimu.app.mod.enchantment.connector.event.SatoriEvent;
import org.eu.hanana.reimu.hnnapp.ModLoader;
import org.eu.hanana.reimu.lib.satori.v1.client.SatoriClient;
import org.eu.hanana.reimu.lib.satori.v1.common.CallbackWsReceiver;
import org.eu.hanana.reimu.lib.satori.v1.common.SignalEvent;
import org.eu.hanana.reimu.lib.satori.v1.protocol.AbstractSignalBody;
import org.eu.hanana.reimu.lib.satori.v1.protocol.Opcode;
import org.eu.hanana.reimu.lib.satori.v1.protocol.Signal;
import org.eu.hanana.reimu.lib.satori.v1.protocol.SignalBodyEvent;
import org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.EventType;
import org.eu.hanana.reimu.webui.core.IEventCallback;

@RequiredArgsConstructor
public class SatoriEventBridge extends SignalEvent {
    protected final SatoriClient client;
    @Override
    public boolean onReceive(Opcode opcode, AbstractSignalBody abstractSignalBody) {
        SatoriManager satoriManager = ModMain.getInstance().getSatoriManager();
        ModLoader.postEvent(new SatoriEvent.Receive(client,opcode,abstractSignalBody));
        return true;
    }

    @Override
    public boolean onEvent(EventType<? extends SignalBodyEvent> eventType, SignalBodyEvent signalBodyEvent) {
        ModLoader.postEvent(new SatoriEvent.Event(client,eventType,signalBodyEvent));
        return true;
    }
}
