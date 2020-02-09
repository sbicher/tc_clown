package de.sbicher.tc_clown.settings;

public class TcSetting {

    /**
     * Identifier for this setting
     */
    private final String key;

    /**
     * Value of this setting
     */
    private final Object value;

    public TcSetting(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }



    public Object getValue() {
        return value;
    }



    @Override
    public String toString() {
        return "TcSetting{" + "key='" + key + '\'' + ", value=" + value + '}';
    }
}
