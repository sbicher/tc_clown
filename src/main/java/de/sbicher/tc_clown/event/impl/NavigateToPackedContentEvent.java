package de.sbicher.tc_clown.event.impl;

import de.sbicher.tc_clown.event.DefaultEvent;
import java.io.File;

/**
 * Event that shows, that a zip file should be navigated to
 */
public class NavigateToPackedContentEvent extends DefaultEvent {

    /**
     * Zip, which contents should be shown in the commander
     */
    final File sourceZip;

    /**
     * Constructor
     * @param  sourceZip Zip, which contents should be shown in the commander
     * @param source Source of the event. Especially to avoid, that components react unnecessarily on their own fired events
     */
    public NavigateToPackedContentEvent(File sourceZip, Object source) {
        super(source);
        this.sourceZip = sourceZip;
    }

    public File getSourceZip() {
        return sourceZip;
    }

    @Override
    public String toString() {
        return "NavigateToZipContentEvent{" + "sourceZip=" + sourceZip.getName() + '}';
    }
}
