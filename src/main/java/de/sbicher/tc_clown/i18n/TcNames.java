package de.sbicher.tc_clown.i18n;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.inject.Inject;

/**
 * Class for delivering the names, titles, descriptions, etc. of components
 */
public class TcNames {


    private final ResourceBundle bundle;

    /**
     * Constructor
     * @param usedLocale Locale to use for i18n
     */
    @Inject
    public TcNames(Locale usedLocale) {
        bundle = ResourceBundle.getBundle("MessageBundles",usedLocale);
    }

    /**
     * Returns the internationalized String for the given identifier
     * @param identifier Identifier, for which a string is searched
     * @return Internationalized string
     */
    public String get(String identifier) {
        return bundle.getString(identifier);
    }
}
