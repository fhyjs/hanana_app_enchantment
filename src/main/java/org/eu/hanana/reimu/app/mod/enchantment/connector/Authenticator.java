package org.eu.hanana.reimu.app.mod.enchantment.connector;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.eu.hanana.reimu.lib.satori.v1.client.AuthenticatorC;
import org.eu.hanana.reimu.lib.satori.v1.client.SatoriClient;
import org.eu.hanana.reimu.lib.satori.v1.protocol.SignalBodyIdentify;

public class Authenticator extends AuthenticatorC {
    private final SatoriManager manager;
    private final SatoriClient client;
    protected Runnable cb;
    public Authenticator(SignalBodyIdentify token, SatoriManager manager, SatoriClient client) {
        super(token);
        this.manager = manager;
        this.client = client;
    }

    @Override
    public AuthenticatorC setCallback(Runnable runnable) {
        this.cb=runnable;
        return super.setCallback(new Runnable() {
            @Override
            public void run() {
                cb.run();
                manager.onOpened(client);
            }
        });
    }
}
