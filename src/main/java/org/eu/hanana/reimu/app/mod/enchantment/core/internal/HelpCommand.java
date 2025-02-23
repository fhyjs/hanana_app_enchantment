package org.eu.hanana.reimu.app.mod.enchantment.core.internal;

import org.eu.hanana.reimu.app.mod.enchantment.ModMain;
import org.eu.hanana.reimu.app.mod.enchantment.core.api.AbstractCommandListener;
import org.eu.hanana.reimu.app.mod.enchantment.core.api.ICommandListener;
import org.eu.hanana.reimu.app.mod.enchantment.event.CommandEvent;
import org.eu.hanana.reimu.lib.satori.v1.protocol.Message;

public class HelpCommand extends AbstractCommandListener {
    public HelpCommand() {
        super("help");
        addAlia("帮助");
    }

    @Override
    public void process(CommandEvent event) {
        var sb = new StringBuilder("[HANANA ENCHANTMENT] HANANA消息处理平台-帮助\n");
        sb.append("可用命令列表:\n");
        for (ICommandListener commandListener : enchantmentManager.commandManager.commandListeners) {
            var names = new StringBuilder();
            names.append("/").append(commandListener.getAcceptableCommands().getFirst());
            if (commandListener.getAcceptableCommands().size()>1) {
                names.append(" (");
                for (int i = 0; i < commandListener.getAcceptableCommands().size(); i++) {
                    var item = commandListener.getAcceptableCommands().get(i);
                    if (i == 0) continue;
                    names.append("/").append(item);
                    if (i < commandListener.getAcceptableCommands().size()-1) names.append(",");
                }
                names.append(")");
            }
            sb.append("    ").append(names).append(" ").append(commandListener.getDescribe()).append("\n");
        }
        enchantmentManager.sendMessage(sb.toString(),event.messageEvent.getChannel().id,event.messageEvent.login);
    }

    @Override
    public String getDescribe() {
        return "显示注册的命令和帮助信息";
    }
}
