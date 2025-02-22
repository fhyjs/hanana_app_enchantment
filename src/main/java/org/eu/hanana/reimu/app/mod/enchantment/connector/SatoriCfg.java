package org.eu.hanana.reimu.app.mod.enchantment.connector;

import com.google.gson.internal.LinkedTreeMap;
import lombok.AllArgsConstructor;
import org.eu.hanana.reimu.app.mod.enchantment.ModMain;
import org.eu.hanana.reimu.app.mod.webui.config.HasName;
import org.eu.hanana.reimu.app.mod.webui.config.IConfigSaver;
import org.eu.hanana.reimu.app.mod.webui.config.SaveProcessor;
import org.eu.hanana.reimu.hnnapp.mods.CfgCoreBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SaveProcessor(SatoriCfg.SaveProcessor.class)
@HasName("内置Satori连接器配置")
public class SatoriCfg {
    public static List<LinkedTreeMap<String,String>> cfg = new ArrayList<>(List.of(getDefaultCfg()));
    public static class SaveProcessor implements IConfigSaver{
        @Override
        public String save() {
            ModMain.getInstance().getSatoriManager().reload();
            return "";
        }
    }
    private static LinkedTreeMap<String,String> getDefaultCfg(){
        LinkedTreeMap<String, String> stringStringLinkedTreeMap = new LinkedTreeMap<>();
        stringStringLinkedTreeMap.put("address","http://127.0.0.1:5140/satori");
        stringStringLinkedTreeMap.put("token","abcd");
        return stringStringLinkedTreeMap;
    }
}
