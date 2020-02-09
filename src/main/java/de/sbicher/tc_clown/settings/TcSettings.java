package de.sbicher.tc_clown.settings;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings for the commander
 */
public class TcSettings {

    public static final String KEY_DATE_FORMAT = "dateFormat";
    public static final String KEY_DATE_TIME_FORMAT = "dateTimeFormat";
    public static final String KEY_FILE_TABLE_FONT = "fileTable.font";
    public static final String KEY_FILE_TABLE_BG_COLOR_EVEN_ROW = "fileTable.colors.evenRowBackground";
    public static final String KEY_FILE_TABLE_BG_COLOR_ODD_ROW = "fileTable.colors.oddRowBackground";
    public static final String KEY_FILE_TABLE_BG_COLOR_SELECTED_ROW = "fileTable.colors.selectedRowBackground";
    public static final String KEY_FILE_TABLE_FG_COLOR_UNSELECTED_ROW = "fileTable.colors.unselectedRowForeground";
    public static final String KEY_FILE_TABLE_FG_COLOR_SELECTED_ROW = "fileTable.colors.selectedRowForeground";

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
            return settings.get(key).getValue();
        }

        return null;
    }

    /**
     * This function is more or less for documentation and shows, which attribute has which (root) type
     * @param key Name of the setting
     * @return Type / Class for the value of this setting
     */
    @SuppressWarnings("unused")
    public Class getTypeForKey(String key) {
        switch (key) {
            case KEY_DATE_FORMAT:
                return String.class;
            case KEY_FILE_TABLE_FONT:
                return Font.class;
        }

        return null;
    }
}
