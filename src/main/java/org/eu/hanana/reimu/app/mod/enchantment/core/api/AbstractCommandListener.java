package org.eu.hanana.reimu.app.mod.enchantment.core.api;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableListIterator;
import org.eu.hanana.reimu.app.mod.enchantment.ModMain;
import org.eu.hanana.reimu.app.mod.enchantment.core.EnchantmentManager;
import org.eu.hanana.reimu.app.mod.enchantment.event.CommandEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommandListener implements ICommandListener{
    protected final List<String> alias = new ArrayList<>();
    protected final EnchantmentManager enchantmentManager = ModMain.getInstance().getEnchantmentManager();
    public AbstractCommandListener(String name){
        alias.add(name);
    }
    @Override
    public void addAlia(String alia) {
        this.alias.add(alia);
    }

    @Override
    public boolean isAccept(String command) {
        return alias.contains(command);
    }

    @Override
    public List<String> getAcceptableCommands() {
        return Collections.unmodifiableList(alias);
    }

    @Override
    public String getDescribe() {
        return "command."+getAcceptableCommands().getFirst()+".describe";
    }
}
