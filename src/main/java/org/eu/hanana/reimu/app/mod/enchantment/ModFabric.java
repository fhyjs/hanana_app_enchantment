package org.eu.hanana.reimu.app.mod.enchantment;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;


public class ModFabric implements ModInitializer {
    public static final String MOD_ID = "enchantment";
    public static final LogCategory logCategory = LogCategory.createCustom(MOD_ID);
    @Override
    public void onInitialize() {
        Log.info(logCategory,"enchantment is loaded!");
    }
}
