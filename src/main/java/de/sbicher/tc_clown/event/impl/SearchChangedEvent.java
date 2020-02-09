package de.sbicher.tc_clown.event.impl;

import de.sbicher.tc_clown.event.DefaultEvent;

/**
 * Event, when the search criteria changed
 */
public class SearchChangedEvent extends DefaultEvent {

    /**
     *  char which was added to the search
     */
    private final char addedSearchChar;

    /**
     * Code of the pressed key (for reaction on ESCAPE or BACKSPACE
     */
    private final int keyCode;

    /**
     * Constructor
     *
     * @param source Source of the event. Especially to avoid, that components react unnecessarily on their own fired events
     * @param addedSearchChar char which was added to the search
     * @param  keyCode Code of the pressed key (for reaction on ESCAPE or BACKSPACE
     */
    public SearchChangedEvent(char addedSearchChar,int keyCode, Object source) {
        super(source);
        this.addedSearchChar = addedSearchChar;
        this.keyCode = keyCode;
    }

    /**
     * Gets the char which was added to the search
     * @return char which was added to the search
     */
    public char getAddedSearchChar() {
        return addedSearchChar;
    }

    /**
     * Gets the code of the pressed key (for reaction on ESCAPE or BACKSPACE
     * @return Code of the pressed key (for reaction on ESCAPE or BACKSPACE
     */
    public int getKeyCode() {
        return keyCode;
    }
}
