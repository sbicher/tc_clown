/*
 * ----------------------------------------------------------------------------
 *     (c) by data experts gmbh
 *            Postfach 1130
 *            Woldegker Str. 12
 *            17001 Neubrandenburg
 * ----------------------------------------------------------------------------
 *     Dieses Dokument und die hierin enthaltenen Informationen unterliegen
 *     dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
 *     Herausgebers weder als ganzes noch in Teilen dupliziert, reproduziert
 *     oder manipuliert werden.
 * ----------------------------------------------------------------------------
 *
 * ----------------------------------------------------------------------------
 */
package de.sbicher.tc_clown.event;

import java.awt.Component;

/**
 * Event, das eine UI anzeigt und deshalb eine Parent-Komponent bekommen kann
 */
public abstract class HasUiEvent extends DefaultEvent {

    /**
     * Constructor
     *
     * @param source Source of the event. Especially to avoid, that components react unnecessarily on their own fired events
     */
    public HasUiEvent(Object source) {
        super(source);
    }

    /**
     * Parent component, on which a possible UI is displayed
     */
    private Component parent;

    /**
     * Delivers the parent component, on which a possible UI is displayed
     *
     * @return Parent component, on which a possible UI is displayed
     */
    public Component getParent() {
        return parent;
    }

    /**
     * Sets the parent component, on which a possible UI is displayed
     *
     * @param parent
     *            Parent component, on which a possible UI is displayed
     */
    public void setParent(Component parent) {
        this.parent = parent;
    }

}
