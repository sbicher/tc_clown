package de.sbicher.tc_clown.model;

import java.io.File;
import java.util.Date;

/**
 * Information to on single file
 */
public class TcFileInfo {

    /**
     * File info object for the navigation one folder up
     */
    public static final TcFileInfo NAVIGATE_UP = new TcFileInfo(null) {
        @Override
        public String getName() {
            return "..";
        }

        public TcFileSize getSize() {
            return new TcFileSize(0);
        }

        public TcDate getLastModified() {
            return new TcDate(new Date(0));
        }
    };

    /**
     * File, that is represented by this {@link TcFileInfo}
     */
    private final File file;

    public TcFileInfo(File file) {
        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public TcDate getLastModified() {
        return new TcDate(new Date(file.lastModified()));
    }

    public TcFileSize getSize() {
        return new TcFileSize(file.length());
    }

    public File getFile() {
        return file;
    }
}
