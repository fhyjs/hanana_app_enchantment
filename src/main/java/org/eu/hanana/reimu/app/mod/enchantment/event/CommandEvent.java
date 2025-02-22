package org.eu.hanana.reimu.app.mod.enchantment.event;

import lombok.RequiredArgsConstructor;
import org.eu.hanana.reimu.lib.satori.v1.protocol.eventtype.MessageEvent;

import java.util.List;

@RequiredArgsConstructor
public class CommandEvent {
    public final String command;
    public final List<String> args;
    public final MessageEvent messageEvent;
}
