package org.eu.hanana.reimu.app.mod.enchantment.core;

import org.eu.hanana.reimu.app.mod.enchantment.connector.SatoriCfg;
import org.eu.hanana.reimu.hnnapp.mods.CfgCoreBase;

public class ConfigCore extends CfgCoreBase {
    @Override
    public void init() {
        addCfgClass(SatoriCfg.class);
    }
}
