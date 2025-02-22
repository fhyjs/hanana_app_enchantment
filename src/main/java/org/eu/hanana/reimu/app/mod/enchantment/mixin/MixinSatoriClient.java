package org.eu.hanana.reimu.app.mod.enchantment.mixin;

import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.eu.hanana.reimu.app.mod.enchantment.connector.SatoriEventBridge;
import org.eu.hanana.reimu.lib.satori.v1.client.SatoriClient;
import org.eu.hanana.reimu.lib.satori.v1.common.CallbackWsReceiver;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Log4j2
@Mixin(SatoriClient.class)
public abstract class MixinSatoriClient {
    @Shadow @Final public List<CallbackWsReceiver.Callback> events;

    @Shadow public abstract SatoriClient addEventListener(CallbackWsReceiver.Callback callback);

    @Inject(method = {"open(ZZ)V"},at=@At("RETURN"))
    public void open(boolean first, boolean sync, CallbackInfo ci){
        
        log.info("opened");
    }
}
