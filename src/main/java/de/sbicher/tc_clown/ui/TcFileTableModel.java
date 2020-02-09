package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.i18n.TcNames;
import de.sbicher.tc_clown.model.TcFileInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Model for the table of files
 */
public class TcFileTableModel extends AbstractTableModel {

    private final List<TcFileInfo> files = new ArrayList<>();

    private final TcNames names;

    private static final int COL_NAME = 0;
    private static final int COL_SIZE = 1;
    private static final int COL_LAST_MODIFIED = 2;

    private int sortedColumn = COL_NAME;

    private boolean sortAscending = true;

    private final Comparator nameComparator = new Comparator<TcFileInfo>() {
        @Override
        public int compare(TcFileInfo o1, TcFileInfo o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    };


    private final Comparator sizeComparator = new Comparator<TcFileInfo>() {
        @Override
        public int compare(TcFileInfo o1, TcFileInfo o2) {
            return Long.compare(o1.getSize().getByteCount(),o2.getSize().getByteCount());
        }
    };

    private final Comparator lastModifiedComparator = new Comparator<TcFileInfo>() {
        @Override
        public int compare(TcFileInfo o1, TcFileInfo o2) {
            return o1.getLastModified().getDate().compareTo(o2.getLastModified().getDate());
        }
    };

    public TcFileTableModel(TcNames names) {
        this.names = names;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case COL_NAME:
                return names.get("fileName");
            case COL_SIZE:
                return names.get("fileSize");
            case COL_LAST_MODIFIED:
                return names.get("fileLastModified");
            default:
                throw new IllegalArgumentException("unknown column index: " + columnIndex);
        }
    }

    public void setFiles(File[] files) {
        this.files.clear();

        for (File f:files) {
            this.files.add(new TcFileInfo(f));
        }

        sort();
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return files.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= files.size()) {
            throw new IndexOutOfBoundsException("wrong row index: " + rowIndex);
        }

        TcFileInfo fileInfo = files.get(rowIndex);

        switch (columnIndex) {
            case COL_NAME:
                return fileInfo.getName();
            case COL_SIZE:
                return fileInfo.getSize();
            case COL_LAST_MODIFIED:
                return fileInfo.getLastModified();
            default:
                throw new IllegalArgumentException("unknown column index: " + columnIndex);
        }
    }

    public void setSortedColumn(int sortedColumn, boolean ascending) {
        this.sortedColumn = sortedColumn;
        this.sortAscending = ascending;

        sort();
    }

    /**
     * Sorts the model with the current sort settings (column, direction)
     */
    private void sort() {
        Comparator<TcFileInfo> comparator = null;
        switch (this.sortedColumn) {
            case COL_NAME:
                comparator = this.nameComparator;
                break;
            case COL_SIZE:
                comparator  = this.sizeComparator;
                break;
            case COL_LAST_MODIFIED:
                comparator = this.lastModifiedComparator;
                break;
            default:
                throw new IllegalArgumentException("can't sort by column " + sortedColumn);

        }

        this.files.sort(comparator);
        if (!sortAscending) {
            Collections.reverse(this.files);
        }

        fireTableDataChanged();
    }



    /**
     * Setds the displayed directory in this model
     * @param directory directory, which should be displayed
     */
    public void setDirectory(File directory) {
        setFiles(directory.listFiles());
    }
}
