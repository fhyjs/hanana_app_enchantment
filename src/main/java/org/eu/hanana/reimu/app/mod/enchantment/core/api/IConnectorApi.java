package org.eu.hanana.reimu.app.mod.enchantment.core.api;

import org.eu.hanana.reimu.lib.satori.v1.protocol.Login;

import java.util.List;

public interface IConnectorApi {
    List<Login> getAllLogins();
    public void sendMessage(String message,String channel,Login login) ;
}
