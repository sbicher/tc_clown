package de.sbicher.tc_clown;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import de.sbicher.tc_clown.settings.TcSettings;
import de.sbicher.tc_clown.settings.TcSettingsReader;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Module for the guice dependency injection
 */
public class TcModule extends AbstractModule {

    private final Logger logger = LoggerFactory.getLogger(TcModule.class);

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

    @Singleton
    @Provides
    private TcSettings getSettings(TcSettingsReader settingsReader) throws IOException {
        File settingsFile = new File ("tc_settings.xml");

        if (!settingsFile.exists()) {
            logger.info("Settings could be put at " + settingsFile.getCanonicalPath());
            return createDefaultSettings();
        }

        return settingsReader.readSettings (settingsFile);
    }

    /**
     * Creates the default settings, when no settings file is present
     * @return the default settings
     */
    private TcSettings createDefaultSettings() {
        TcSettings settings = new TcSettings();

        settings.setSetting(TcSettings.KEY_DATE_FORMAT,"dd.MM.yyyy");

        return settings;
    }
}
