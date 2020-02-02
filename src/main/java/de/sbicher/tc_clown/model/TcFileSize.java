package de.sbicher.tc_clown.model;

/**
 * Representing the size of a file
 */
public class TcFileSize {

    /**
     * Number of Bytes in the file
     */
    final long byteCount;

    public TcFileSize (long byteCount) {
        this.byteCount = byteCount;
    }

    public long getByteCount() {
        return byteCount;
    }

    /**
     * Delivers the file size in pretty formatted manner for example in bytes, kB, MB, GB, ... (what fits best)
     * @return Pretty string for the size
     */
    public String toPrettyString() {
        return getByteCount() + " bytes";
    }

    @Override
    public String toString() {
        return toPrettyString();
    }
}
