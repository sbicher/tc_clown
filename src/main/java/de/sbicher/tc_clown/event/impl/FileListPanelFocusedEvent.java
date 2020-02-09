package de.sbicher.tc_clown.event.impl;

import de.sbicher.tc_clown.event.DefaultEvent;

/**
 * Event, which is fired, when a file list panel gets the focus
 */
public class FileListPanelFocusedEvent extends DefaultEvent {

    /**
     * Number of the panel (for example 0 for the left and 1 for the right panel in a horizontal 2-side-view)
     */
    private final int panelNr;

    /**
     * Constructor
     *
     * @param source Source of the event. Especially to avoid, that components react unnecessarily on their own fired events
     * @param panelNr Number of the panel (for example 0 for the left and 1 for the right panel in a horizontal 2-side-view)
     */
    public FileListPanelFocusedEvent(Object source, int panelNr) {
        super(source);
        this.panelNr = panelNr;
    }

    public int getPanelNr() {
        return panelNr;
    }
}
