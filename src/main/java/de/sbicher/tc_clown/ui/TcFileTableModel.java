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

    private File currentDirectory = null;

    private final TcNames names;

    static final int COL_NAME = 0;
    static final int COL_SIZE = 1;
    static final int COL_LAST_MODIFIED = 2;

    private int sortedColumn = COL_NAME;

    private boolean sortAscending = true;

    private final Comparator nameComparator = new Comparator<TcFileInfo>() {
        @Override
        public int compare(TcFileInfo o1, TcFileInfo o2) {
            int specialCompareResult = compareSpecialFileInfos (o1,o2);
            if(specialCompareResult != 0) {
                return specialCompareResult;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    };


    private final Comparator sizeComparator = new Comparator<TcFileInfo>() {
        @Override
        public int compare(TcFileInfo o1, TcFileInfo o2) {
            int specialCompareResult = compareSpecialFileInfos (o1,o2);
            if(specialCompareResult != 0) {
                return specialCompareResult;
            }
            return Long.compare(o1.getSize().getByteCount(),o2.getSize().getByteCount());
        }
    };

    private final Comparator lastModifiedComparator = new Comparator<TcFileInfo>() {
        @Override
        public int compare(TcFileInfo o1, TcFileInfo o2) {
            int specialCompareResult = compareSpecialFileInfos (o1,o2);
            if(specialCompareResult != 0) {
                return specialCompareResult;
            }
            return o1.getLastModified().getDate().compareTo(o2.getLastModified().getDate());
        }
    };

    /**
     * Compares the special entries for the file infos, like the "navigate one up" file info
     * @param o1 First object to compare
     * @param o2 Second object to compare
     * @return Result of the comparison
     */
    private int compareSpecialFileInfos(TcFileInfo o1, TcFileInfo o2) {
        if (o1 == TcFileInfo.NAVIGATE_UP) {
            return -1;
        } else if (o2 == TcFileInfo.NAVIGATE_UP) {
            return 1;
        }

        if (o1.getFile().isDirectory() && !o2.getFile().isDirectory()) {
            // directories first
            return -1;
        }
        if (o2.getFile().isDirectory() && !o1.getFile().isDirectory()) {
            // directories first
            return 1;
        }

        return 0;
    }

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

    /**
     * Sets
     * @param files
     */
    private void setFiles(File[] files) {


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
     * Gets the file info at pos {@code row}
     * @param row Row for which the file info is requested
     * @return File info at this row
     */
    public TcFileInfo getFileInfo (int row) {
        if (row < 0 || row >= this.files.size()) {
            throw new IllegalArgumentException("Row out of scope: " + row + " (total size: " + files.size());
        }

        return this.files.get(row);
    }

    /**
     * Setds the displayed directory in this model
     * @param directory directory, which should be displayed
     */
    public void setDirectory(File directory) {
        this.currentDirectory = directory;
        File[] filesToShow = directory.listFiles();

        this.files.clear();

        if (directory.getParentFile() != null) {
            this.files.add(TcFileInfo.NAVIGATE_UP);
        }

        for (File f:filesToShow) {
            this.files.add(new TcFileInfo(f));
        }

        sort();
    }

    /**
     * Gives the information, wether the table has a navigation row currently (it has no one, when it is in the root directory)
     * @return Information, if we have a navigation row
     */
    public boolean hasNavigationRow () {
        return !this.files.isEmpty() && this.files.get(0) == TcFileInfo.NAVIGATE_UP;
    }

    /**
     * Gets the currently displayed directory
     * @return currently displayed directory
     */
    public File getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Gets the row number of the file in the model, which equals the searched file
     * @param f searched file
     * @return Number of the row, in which the file appears
     */
    public int getRowNumber(File f) {
        for (int row=0; row<files.size(); row++) {
            File fileInRow = files.get(row).getFile();
            if (f.equals(fileInRow)) {
                return row;
            }
        }

        return -1;
    }
}
