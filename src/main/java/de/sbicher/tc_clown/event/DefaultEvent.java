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

import com.google.common.base.MoreObjects;

/**
 * Rumpf-Implementierung eines Events
 */
public abstract class DefaultEvent implements Event {

    /**
     *Source of the event. Especially to avoid, that components react unnecessarily on their own fired events
     */
    private final Object source;

    /**
     * Constructor
     *
     * @param source Source of the event. Especially to avoid, that components react unnecessarily on their own fired events
     */
    public DefaultEvent(Object source) {
        this.source = source;
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("source", getSource()).toString();
    }

}
