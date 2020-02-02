package de.sbicher.tc_clown.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nonnull;

/**
 * date information
 */
public class TcDate {

    private final Date date;

    public TcDate (@Nonnull  Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return this.date.toString();
    }
}
