package de.sbicher.tc_clown.event.impl;

import de.sbicher.tc_clown.event.DefaultEvent;
import de.sbicher.tc_clown.model.TcFileInfo;

/**
 * Event that shows, that a file was "startet" or executed
 */
public class StartFileEvent extends DefaultEvent {

    final TcFileInfo startedFile;

    /**
     * Constructor
     * @param  startedFile file which was "started"
     * @param source Source of the event. Especially to avoid, that components react unnecessarily on their own fired events
     */
    public StartFileEvent(TcFileInfo startedFile, Object source) {
        super(source);
        this.startedFile = startedFile;
    }

    public TcFileInfo getStartedFile() {
        return startedFile;
    }

    @Override
    public String toString() {
        return "FileStartedEvent{" + "startedFile=" + startedFile + '}';
    }
}
