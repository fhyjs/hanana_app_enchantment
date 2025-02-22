package org.eu.hanana.reimu.app.mod.enchantment.core.api;

import org.eu.hanana.reimu.app.mod.enchantment.event.CommandEvent;

import java.util.List;

public interface ICommandListener{
    boolean isAccept(String command);
    void process(CommandEvent event);
    List<String> getAcceptableCommands();
    void addAlia(String alia);
    String getDescribe();
}
