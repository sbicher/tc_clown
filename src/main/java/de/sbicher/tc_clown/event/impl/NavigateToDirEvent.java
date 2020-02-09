package de.sbicher.tc_clown.event.impl;

import de.sbicher.tc_clown.event.DefaultEvent;
import java.io.File;

/**
 * Event that shows, that a file was "startet", for example a directory should be entered or a file was executed
 */
public class NavigateToDirEvent extends DefaultEvent {

    /**
     * directory, from which the navigation startet
     */
    final File sourceDir;

    final File targetDir;

    /**
     * Constructor
     * @param  targetDir dir to where should be navigated file which was "started"
     * @param source Source of the event. Especially to avoid, that components react unnecessarily on their own fired events
     */
    public NavigateToDirEvent(File sourceDir, File targetDir, Object source) {
        super(source);
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;
    }

    public File getTargetDir() {
        return targetDir;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    @Override
    public String toString() {
        return "NavigateToDirEvent{" + "targetDir=" + targetDir.getName() + '}';
    }
}
