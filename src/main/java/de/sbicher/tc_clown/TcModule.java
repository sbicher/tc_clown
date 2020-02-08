package de.sbicher.tc_clown;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.util.Locale;

/**
 * Module for the guice dependency injection
 */
public class TcModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
    }

    /**
     * Provides the used locale settings for the application for internationalization
     * @return Used Localsettings
     */
    @Provides
    private Locale getUsedLocale() {
        return new Locale("de","DE");
    }
}
