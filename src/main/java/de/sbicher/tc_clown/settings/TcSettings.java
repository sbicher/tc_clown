package de.sbicher.tc_clown.settings;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings for the commander
 */
public class TcSettings {

    public static final String KEY_DATE_FORMAT = "dateFormat";
    public static final String KEY_DATE_TIME_FORMAT = "dateTimeFormat";

    // Execution settings
    public static final String KEY_EXEC_HANDLE_ZIPS_LIKE_DIRS = "exec.handleZipLikeDirs"; // handle zip-files like directories and jump into them on ENTER

    // File Table settings
    public static final String KEY_FILE_TABLE_FONT = "fileTable.font";
    public static final String KEY_FILE_TABLE_BG_COLOR_DIRECTORY_ROW = "fileTable.colors.directoryRowBackground";
    public static final String KEY_FILE_TABLE_BG_COLOR_FILE_ROW = "fileTable.colors.fileRowBackground";
    public static final String KEY_FILE_TABLE_BG_COLOR_SELECTED_ROW = "fileTable.colors.selectedRowBackground";
    public static final String KEY_FILE_TABLE_FG_COLOR_UNSELECTED_ROW = "fileTable.colors.unselectedRowForeground";
    public static final String KEY_FILE_TABLE_FG_COLOR_SELECTED_ROW = "fileTable.colors.selectedRowForeground";
    public static final String KEY_FILE_TABLE_COUNT = "fileTableCount"; // count of displayed fileTables

    //
    public static final String KEY_FILE_TABLE_MOVE_ON_SEARCH_DELETION = "fileTable.search.moveOnDeletion"; // should the cursor move to the (possible new) first fitting entry, when text from the search box is deleted?

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
            case KEY_DATE_TIME_FORMAT:
                return String.class;
            case KEY_EXEC_HANDLE_ZIPS_LIKE_DIRS:
            case KEY_FILE_TABLE_MOVE_ON_SEARCH_DELETION:
                return Boolean.class;
            case KEY_FILE_TABLE_COUNT:
                return Integer.class;
            case KEY_FILE_TABLE_BG_COLOR_DIRECTORY_ROW:
            case KEY_FILE_TABLE_BG_COLOR_FILE_ROW:
            case KEY_FILE_TABLE_BG_COLOR_SELECTED_ROW:
            case KEY_FILE_TABLE_FG_COLOR_SELECTED_ROW:
            case KEY_FILE_TABLE_FG_COLOR_UNSELECTED_ROW:
                return Color.class;
            case KEY_FILE_TABLE_FONT:
                return Font.class;
        }

        return null;
    }
}
