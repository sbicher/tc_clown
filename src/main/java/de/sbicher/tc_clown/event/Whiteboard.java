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

import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event bus in the sense of the whiteboard pattern (https://en.wikipedia.org/wiki/Whiteboard_Pattern).<br>
 * Components can register for any events at the whiteboard and will bie informed (in no specific or determined order)
 */
@Singleton
public class Whiteboard {

    private final Logger logger = LoggerFactory.getLogger(Whiteboard.class);

    /**
     * Zusammenfassung des konkreten Event-Handlers und der Methode, mit Hilfe derer die entsprechenden Events behandelt werden.
     */
    private static class EventHandlerMethod {
        private final TcEventHandler handler;
        private final Method handleMethod;

        /**
         * Konstruktor
         *
         * @param handler
         *            Konkreter Handler
         * @param handleMethod
         *            Methode, die auf diesem Handler bei Eintreffen des Events ausgelöst wird
         */
        private EventHandlerMethod(TcEventHandler handler, Method handleMethod) {
            this.handler = handler;
            this.handleMethod = handleMethod;
        }
    }

    /**
     * Registrierte EventHandler.<br>
     * Schlüssel == Typ des Events (Klasse)<br>
     * Wert == "zugehöriger" Eventhandler (handelt Events vom Typ des Schlüssels
     */
    // private <T extends Event> Map<Class<T>, EventHandler<T>> eventHandlers = new HashMap<>();
    private Multimap<Class<? extends Event>, EventHandlerMethod> eventHandlers = HashMultimap.create();

    /**
     * Registriert einen Handler am {@link Whiteboard} für das globale Event
     *
     * @param eventType
     *            Typ des Events, für das der Handler registriert werden soll
     * @param eventHandler
     *            Event-Handler, der registriert werden soll
     */
    public void registerHandler(Class<? extends Event> eventType, TcEventHandler eventHandler) {
        logger.debug("Registriere Handler: " + Strings.padEnd(eventType.getSimpleName(), 30, ' ') + " - " + eventHandler.getClass().getSimpleName());
        String eventTypeName = eventType.getSimpleName();
        // der Handler muss die Methode handle<EVENT_TYPE_NAME> (eventType) implementieren
        try {
            Method handleMethod = eventHandler.getClass().getMethod("handle" + eventTypeName, eventType);
            eventHandlers.put(eventType, new EventHandlerMethod(eventHandler, handleMethod));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Der Handler muss die Methode handle" + eventTypeName + "(" + eventType.getName() + ") implementieren.");
        }
    }

    /**
     * Feuert ein Event am {@link Whiteboard}, d.h. es wird publiziert und alle registrierten {@link TcEventHandler} werden informiert
     *
     * @param e
     *            Event, das gefeuert werden soll
     */
    public void fireEvent(@Nonnull Event e) {
        if (!eventHandlers.containsKey(e.getClass())) {
            // kein Handler registriert
            return;
        }

        for (EventHandlerMethod ehm : eventHandlers.get(e.getClass())) {
            try {
                ehm.handleMethod.invoke(ehm.handler, e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
