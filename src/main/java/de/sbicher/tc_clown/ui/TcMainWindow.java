package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.event.TcEventHandler;
import de.sbicher.tc_clown.event.Whiteboard;
import de.sbicher.tc_clown.event.impl.FileListPanelFocusedEvent;
import de.sbicher.tc_clown.event.impl.NavigateToDirEvent;
import de.sbicher.tc_clown.event.impl.SearchChangedEvent;
import de.sbicher.tc_clown.event.impl.StartFileEvent;
import de.sbicher.tc_clown.i18n.TcNames;
import de.sbicher.tc_clown.model.TcFileInfo;
import de.sbicher.tc_clown.settings.TcSettings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main window of the application - with the "classical" two-sided directories view
 */
public class TcMainWindow extends JFrame implements TcEventHandler {

    private final Logger logger = LoggerFactory.getLogger(TcMainWindow.class);

    private final ShortcutLinksPanel pnlShortcuts;

    private final Whiteboard whiteboard;

    private int currentSelectedFilePanelNr = -1;

    private final List<JTable> fileTables = new ArrayList<>();

    private final List<TcFileTableModel> fileTableModels = new ArrayList<>();

    private final TcFileTableRenderer tableRenderer;

    private final TcNames names;

    private final TcSettings settings;

    private final FileTableListener fileNavigationListener;

    private final JTextField fldSearch = new JTextField();

    private final JPanel pnlSearch = new JPanel();

    private final int filePanelsCount; // number of file panels displayed. They are numbered from top left to bottom right, for example "0, 1, 2" for the first row and "3, 4, 5" for the second row in a 3x2-Layout

    /**
     * Constructor
     */
    @Inject
    public TcMainWindow(TcNames names, Whiteboard whiteboard, ShortcutLinksPanel shortcutLinksPanel, TcFileTableRenderer tableRenderer, FileTableListener fileNavigationListener, TcSettings settings) {
        super("TC Clown");

        this.whiteboard = whiteboard;
        this.pnlShortcuts = shortcutLinksPanel;
        this.tableRenderer = tableRenderer;
        this.names = names;
        this.settings = settings;
        this.fileNavigationListener = fileNavigationListener;
        this.filePanelsCount = (Integer) settings.getSettingValue(TcSettings.KEY_FILE_TABLE_COUNT);

        initFileTableModels();
        initShortCutPanel();
        initSearchPanel();

        JComponent pnlDirectories = initDirectoriesPanel();

        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(this.pnlShortcuts, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        getContentPane().add(pnlDirectories, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.1, 10000.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
        getContentPane().add(pnlSearch, new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));

        // Testdtaen
        registerWhiteboardEvents();

        setPreferredSize(new Dimension(400, 400));
        setMinimumSize(getPreferredSize());

    }

    /**
     * Initializes the panel for the search, which appears when letters are written
     */
    private void initSearchPanel() {
        fldSearch.setPreferredSize(new Dimension(300, 20));
        pnlSearch.setLayout(new GridBagLayout());

        JLabel lblClose = new JLabel("X");
        lblClose.setBackground(new Color(150, 150, 150));

        pnlSearch.add(fldSearch, new GridBagConstraints(0, 0, 1, 1, 10000.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
        pnlSearch.add(lblClose, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * Initial all the used table models for the files
     */
    private void initFileTableModels() {

        this.fileTableModels.add(new TcFileTableModel(names)); // left file panel model
        this.fileTableModels.add(new TcFileTableModel(names)); // right file panel model
    }

    /**
     * Registers this component as listener for the used whiteboard events
     * Registers this component as listener for the used whiteboard events
     */
    private void registerWhiteboardEvents() {
        this.whiteboard.registerHandler(StartFileEvent.class, this);
        this.whiteboard.registerHandler(NavigateToDirEvent.class, this);
        this.whiteboard.registerHandler(FileListPanelFocusedEvent.class, this);
        this.whiteboard.registerHandler(SearchChangedEvent.class, this);
    }

    /**
     * Initializes the panels für the directories
     *
     * @return Component for the directories-view
     */
    private JComponent initDirectoriesPanel() {
        FocusListener fileListPanelListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String name = ((JComponent) e.getSource()).getName();
                whiteboard.fireEvent(new FileListPanelFocusedEvent(TcMainWindow.this, Integer.parseInt(name)));
            }
        };

        for (int i = 0; i < fileTableModels.size(); i++) {
            JTable table = createFileTable(fileTableModels.get(i));
            table.setName("" + i);
            table.addFocusListener(fileListPanelListener);
            fileTables.add(table);
        }

        JScrollPane scrLeft = new JScrollPane(fileTables.get(0));
        JScrollPane scrRight = new JScrollPane(fileTables.get(1));

        //Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrLeft, scrRight);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.5d);
        splitPane.setResizeWeight(0.5d);

        //Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        scrLeft.setMinimumSize(minimumSize);
        scrRight.setMinimumSize(minimumSize);

        splitPane.setPreferredSize(new Dimension(400, 200));

        // test data
        fileTableModels.get(0).setDirectory(new File("/home/bichi/ispace"));
        fileTableModels.get(1).setDirectory(new File("/tmp"));

        return splitPane;
    }

    private JTable createFileTable(TcFileTableModel letfFileTableModel) {
        final JTable table = new JTable(letfFileTableModel);
        table.setDefaultRenderer(Object.class, tableRenderer);
        table.setShowGrid(false);

        KeyListener[] keyListeners = table.getKeyListeners();
        for (int i = keyListeners.length - 1; i >= 0; i--) {
            table.removeKeyListener(keyListeners[i]);
        }

        table.addKeyListener(fileNavigationListener);

        return table;
    }

    /**
     * Initializes the shortcut component, with the display für drives, shortcuts, ...
     *
     * @return short component
     */
    private void initShortCutPanel() {
        this.pnlShortcuts.setPreferredSize(new Dimension(100, 30));
    }

    /**
     * Handles the selection of a file and displays that file in the currentWindow
     *
     * @param event Event, that was fired for the selection
     */
    @SuppressWarnings("unused")
    public void handleFileListPanelFocusedEvent(FileListPanelFocusedEvent event) {
        logger.info("Filepanel focues: " + event.getPanelNr());
        this.currentSelectedFilePanelNr = event.getPanelNr();
    }

    /**
     * Handles the start of a file and displays that file in the currentWindow
     *
     * @param event Event, that was fired for the start
     */
    @SuppressWarnings("unused")
    public void handleStartFileEvent(StartFileEvent event) {

        logger.info("Starte die Datei.....: " + event.getStartedFile().getName());

    }

    @SuppressWarnings("unused")
    public void handleNavigateToDirEvent(NavigateToDirEvent event) {
        TcFileTableModel fileTableModel = this.fileTableModels.get(this.currentSelectedFilePanelNr);
        fileTableModel.setDirectory(event.getTargetDir());

        // if the new directory is the direct parent directory of the source directory of the event,
        // the source directory should be selected
        int selectedRow = 0;
        if (Objects.equals(event.getSourceDir().getParentFile(), event.getTargetDir())) {
            int row = fileTableModel.getRowNumber(event.getSourceDir());
            if (row >= 0) {
                selectedRow = row;
            }
        }
        this.fileTables.get(this.currentSelectedFilePanelNr).getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
    }

    /**
     * Sets the focus on the first of the file panels
     */
    public void focusFirstFilePane() {
        this.fileTables.get(0).requestFocus();
        this.fileTables.get(0).getSelectionModel().setSelectionInterval(0, 0);
    }

    /**
     * Gets the current used/selected file table model
     *
     * @return current used/selected file table model
     */
    private TcFileTableModel getCurrentModel() {
        return this.fileTableModels.get(currentSelectedFilePanelNr);
    }

    /**
     * Gets the currently used/selected file table.
     * @return currently used/selected file table.
     */
    private JTable getCurrentTable () {
        return this.fileTables.get(currentSelectedFilePanelNr);
    }

    public void handleSearchCanceled() {
        this.fldSearch.setText("");
        this.fldSearch.setBackground(Color.white);
        this.pnlSearch.setVisible(false);
    }

    /**
     * React on the search of the search conditions.<br />
     * This can mean:<br />
     * <ul>
     *     <li>stop of the search (if keycode == KeyEvent.VK_ESCAPE</li>
     *     <li>remove the last search char (if keycode == KeyEvent.VK_BACK_SPACE</li>
     *     <li>append a char to the search</li>
     * </ul>
     * @param event
     */
    public void handleSearchChangedEvent(SearchChangedEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            handleSearchCanceled();
            return;
        }

        String orgText = fldSearch.getText();
        if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if(orgText.isEmpty()) {
                // nothing to delete
                return;
            }
           this.fldSearch.setText(orgText.substring(0, orgText.length() - 1));
        } else {
            // Buchstabe eingetippt
            this.fldSearch.setText(orgText + event.getAddedSearchChar());
        }

        pnlSearch.setVisible(true);

        String searchRegex = "(?i)" + fldSearch.getText() + ".*";

        if (Boolean.FALSE.equals(settings.getSettingValue(TcSettings.KEY_FILE_TABLE_MOVE_ON_SEARCH_DELETION))) {
            // does the current entry still matches?
            TcFileInfo currentSelected = getCurrentModel().getFileInfo(getCurrentTable().getSelectedRow());
            if (currentSelected.getFile() != null && currentSelected.getFile().getName().matches(searchRegex)) {
                // nothing to do
                return;
            }
        }

        // passende Datei heraussuchen und selektieren
        int row = getCurrentModel().searchForFileIndexByRegex(searchRegex);
        if (row == -1) {
            fldSearch.setBackground(new Color(236, 173, 167));
            return;
        }
        fldSearch.setBackground(Color.white);

        this.fileTables.get(currentSelectedFilePanelNr).getSelectionModel().setSelectionInterval(row, row);
    }

}
