package org.eu.hanana.reimu.app.mod.enchantment.connector.event;

import lombok.AllArgsConstructor;
import org.eu.hanana.reimu.app.mod.enchantment.connector.SatoriEventBridge;
import org.eu.hanana.reimu.lib.satori.v1.client.SatoriClient;
import org.eu.hanana.reimu.lib.satori.v1.protocol.AbstractSignalBody;
import org.eu.hanana.reimu.lib.satori.v1.protocol.Opcode;
import org.eu.hanana.reimu.lib.satori.v1.protocol.SignalBodyEvent;
import org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.EventType;

@AllArgsConstructor
public abstract class SatoriEvent {
    public final SatoriClient client;
    public static class Receive extends SatoriEvent {
        public final Opcode opcode;
        public final AbstractSignalBody abstractSignalBody;

        public Receive(SatoriClient client, Opcode opcode, AbstractSignalBody abstractSignalBody) {
            super(client);
            this.opcode = opcode;
            this.abstractSignalBody = abstractSignalBody;
        }
    }

    public static class Event extends SatoriEvent{
        public final EventType<? extends SignalBodyEvent> eventType;
        public final SignalBodyEvent signalBodyEvent;

        public Event(SatoriClient client, EventType<? extends SignalBodyEvent> eventType, SignalBodyEvent signalBodyEvent) {
            super(client);
            this.eventType=eventType;
            this.signalBodyEvent=signalBodyEvent;
        }
    }
}
