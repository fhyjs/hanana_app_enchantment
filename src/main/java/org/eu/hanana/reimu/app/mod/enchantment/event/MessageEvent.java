package org.eu.hanana.reimu.app.mod.enchantment.event;



public abstract class MessageEvent {
    public final org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.MessageEvent messageEvent;

    protected MessageEvent(org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.MessageEvent messageEvent) {
        this.messageEvent = messageEvent;
    }
    public static class Created extends MessageEvent{
        public Created(org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.MessageEvent messageEvent) {
            super(messageEvent);
        }
    }
}
