package de.sbicher.tc_clown.settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Settings for the commander
 */
public class TcSettings {

    public static final String KEY_DATE_FORMAT = "dateFormat";

    /**
     * Map for all the settings (Key == key of the setting, value == the setting)
     */
    private final Map<String, TcSetting> settings = new HashMap<>();


    public void setSetting (TcSetting setting) {
        this.settings.put(setting.getKey(),setting);
    }

    public void setSetting (String key, Object value) {
        this.settings.put(key,new TcSetting(key,value));
    }

    public Object getSettingValue (String key) {
        if (settings.containsKey(key)) {
            return settings.get(key);
        }

        return null;
    }

}
