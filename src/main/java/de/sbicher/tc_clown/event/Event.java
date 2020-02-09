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

/**
 * Interface für Events, die an das {@link Whiteboard} gemeldet werden können
 */
public interface Event {

    /**
     * Liefert die Quelle des Events, damit Komponenten, die sowohl Sender als auch Empfänger von Events sind, sicherstellen können, dass sie nicht (unnötig) auf eigene Events reagieren
     *
     * @return Quelle des Events (sollte die feuernde Komponente sein)
     */
    public Object getSource();
    // nix - reines Marker-Interface
}
