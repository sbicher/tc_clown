package de.sbicher.tc_clown;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import de.sbicher.tc_clown.settings.TcSettings;
import de.sbicher.tc_clown.settings.TcSettingsReader;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
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
    private TcSettings getSettings(TcSettingsReader settingsReader) throws IOException, FontFormatException {
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
    private TcSettings createDefaultSettings() throws IOException, FontFormatException {
        TcSettings settings = new TcSettings();

        settings.setSetting(TcSettings.KEY_DATE_FORMAT,"dd.MM.yyyy");
        settings.setSetting(TcSettings.KEY_DATE_TIME_FORMAT,"dd.MM.yyyy HH:mm:ss");

        settings.setSetting(TcSettings.KEY_FILE_TABLE_BG_COLOR_FILE_ROW, Color.white);
        settings.setSetting(TcSettings.KEY_FILE_TABLE_BG_COLOR_DIRECTORY_ROW, new Color(243,241,186));
        settings.setSetting(TcSettings.KEY_FILE_TABLE_BG_COLOR_SELECTED_ROW, new Color(65,103,147));

        settings.setSetting(TcSettings.KEY_FILE_TABLE_FG_COLOR_UNSELECTED_ROW, Color.black);
        settings.setSetting(TcSettings.KEY_FILE_TABLE_FG_COLOR_SELECTED_ROW, Color.white);

        Font monospacedFont = Font.createFont(Font.TRUETYPE_FONT,TcModule.class.getResourceAsStream("/fonts/DejaVuSansMono.ttf"));
        monospacedFont = monospacedFont.deriveFont(15f);
        settings.setSetting(TcSettings.KEY_FILE_TABLE_FONT, monospacedFont);

        settings.setSetting(TcSettings.KEY_FILE_TABLE_MOVE_ON_SEARCH_DELETION,true);
        settings.setSetting(TcSettings.KEY_FILE_TABLE_COUNT,2);


        return settings;
    }
}
