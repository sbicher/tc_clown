package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.event.EventHandler;
import de.sbicher.tc_clown.event.Whiteboard;
import de.sbicher.tc_clown.event.impl.FileListPanelFocusedEvent;
import de.sbicher.tc_clown.event.impl.NavigateToDirEvent;
import de.sbicher.tc_clown.event.impl.StartFileEvent;
import de.sbicher.tc_clown.i18n.TcNames;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main window of the application - with the "classical" two-sided directories view
 */
public class TcMainWindow extends JFrame implements EventHandler {

    private final Logger logger = LoggerFactory.getLogger(TcMainWindow.class);

    private final ShortcutLinksPanel pnlShortcuts;

    private final Whiteboard whiteboard;

    private int focusedFilePanelNr = -1;

    private final List<JTable> fileTables = new ArrayList<>();

    private final List<TcFileTableModel> fileTableModels = new ArrayList<>();

    private final  TcFileTableRenderer tableRenderer;

    private final TcNames names;

    private final FileNavigationListener fileNavigationListener;

    /**
     * Constructor
     */
    @Inject
    public TcMainWindow(TcNames names, Whiteboard whiteboard, ShortcutLinksPanel shortcutLinksPanel, TcFileTableRenderer tableRenderer, FileNavigationListener fileNavigationListener) {
        super("TC Clown");

        this.whiteboard = whiteboard;
        this.pnlShortcuts = shortcutLinksPanel;
        this.tableRenderer = tableRenderer;
        this.names = names;
        this.fileNavigationListener = fileNavigationListener;


        initFileTableModels();
        initShortCutPanel();
        JComponent pnlDirectories = initDirectoriesPanel();

        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(this.pnlShortcuts, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        getContentPane().add(pnlDirectories, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.1, 10000.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 0, 0));

        // Testdtaen
        registerWhiteboardEvents();

        setPreferredSize(new Dimension(400,400));
        setMinimumSize(getPreferredSize());

    }

    private void initFileTableModels() {
       this.fileTableModels.add (new TcFileTableModel(names)); // left file panel model
       this.fileTableModels.add (new TcFileTableModel(names)); // right file panel model
    }

    private void registerWhiteboardEvents() {
        this.whiteboard.registerHandler(StartFileEvent.class, this);
        this.whiteboard.registerHandler(NavigateToDirEvent.class,this);
        this.whiteboard.registerHandler(FileListPanelFocusedEvent.class, this);
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
                String name =   ((JComponent) e.getSource()).getName();
                whiteboard.fireEvent(new FileListPanelFocusedEvent(TcMainWindow.this,Integer.parseInt(name)));
            }
        };

        for (int i=0; i<fileTableModels.size(); i++) {
            JTable table = createFileTable(fileTableModels.get(i));
            table.setName("" + i);
            table.addFocusListener(fileListPanelListener);
            fileTables.add( table);
        }


        JScrollPane scrLeft = new JScrollPane(fileTables.get(0));
        JScrollPane scrRight = new JScrollPane(fileTables.get(1));


        //Create a split pane with the two scroll panes in it.
         JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrLeft, scrRight);
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
        table.setDefaultRenderer(Object.class,tableRenderer);
        table.setShowGrid(false);

        KeyListener[] keyListeners = table.getKeyListeners();
        for (int i=keyListeners.length-1;i>=0; i--) {
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
     * @param event Event, that was fired for the selection
     */
    @SuppressWarnings("unused")
    public void handleFileListPanelFocusedEvent(FileListPanelFocusedEvent event) {
        logger.info("Filepanel focues: " + event.getPanelNr());
        this.focusedFilePanelNr = event.getPanelNr();
    }

    /**
     * Handles the start of a file and displays that file in the currentWindow
     * @param event Event, that was fired for the start
     */
    @SuppressWarnings("unused")
    public void handleStartFileEvent (StartFileEvent event) {

        logger.info ("Starte die Datei.....: " +event.getStartedFile().getName());


    }

    @SuppressWarnings("unused")
    public void handleNavigateToDirEvent (NavigateToDirEvent event) {
        TcFileTableModel fileTableModel = this.fileTableModels.get(this.focusedFilePanelNr);
        fileTableModel.setDirectory(event.getTargetDir());

        // if the new directory is the direct parent directory of the source directory of the event,
        // the source directory should be selected
        int selectedRow = 0;
        if (Objects.equals(event.getSourceDir().getParentFile(),event.getTargetDir())) {
            int row = fileTableModel.getRowNumber(event.getSourceDir());
            if (row >= 0) {
                selectedRow = row;
            }
        }
                this.fileTables.get(this.focusedFilePanelNr).getSelectionModel().setSelectionInterval(selectedRow,selectedRow);
    }

    public void focusFirstFilePane() throws InterruptedException {
        this.fileTables.get(0).requestFocus();
        this.fileTables.get(0).getSelectionModel().setSelectionInterval(0,0);
    }
}
