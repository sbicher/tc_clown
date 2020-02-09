package de.sbicher.tc_clown.event.impl;

import de.sbicher.tc_clown.event.DefaultEvent;
import java.io.File;

/**
 * Event which signalises, that a file was selected
 */
public class FileSelectedEvent extends DefaultEvent {

    /**
     * File, that was selected
     */
    private final File file;

    /**
     * Constructor
     *
     * @param source Source of the event. Especially to avoid, that components react unnecessarily on their own fired events
     * @param  file  File, that was selected
     */
    public FileSelectedEvent(Object source, File file) {
        super(source);
        this.file = file;
    }

    /**
     * Delivers the file, that was selected
     * @return File, that was selected
     */
    public File getFile() {
        return file;
    }
}
