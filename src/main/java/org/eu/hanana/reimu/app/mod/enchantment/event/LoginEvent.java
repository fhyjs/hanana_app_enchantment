package org.eu.hanana.reimu.app.mod.enchantment.event;

import lombok.Getter;
import org.eu.hanana.reimu.lib.satori.v1.protocol.Login;

public abstract class LoginEvent {
    @Getter
    private final Login login;

    public LoginEvent(Login login){
        this.login=login;
    }
    public static class Added extends LoginEvent{
        public Added(Login login) {
            super(login);
        }
    }
    public static class Removed extends LoginEvent{
        public Removed(Login login) {
            super(login);
        }
    }
    public static class Updated extends LoginEvent{
        public Updated(Login login) {
            super(login);
        }
    }
}
