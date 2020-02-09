package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.event.EventHandler;
import de.sbicher.tc_clown.event.Whiteboard;
import de.sbicher.tc_clown.event.impl.FileSelectedEvent;
import de.sbicher.tc_clown.i18n.TcNames;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import javax.inject.Inject;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main window of the application - with the "classical" two-sided directories view
 */
public class TcMainWindow extends JFrame implements EventHandler {

    private final Logger logger = LoggerFactory.getLogger(TcMainWindow.class);

    private final TcDirectoryPanel leftDirPanel;

    private final TcDirectoryPanel rightDirPanel;

    private final ShortcutLinksPanel pnlShortcuts;

    private final Whiteboard whiteboard;

    private boolean isInLeftPanel = true;

    /**
     * Constructor
     */
    @Inject
    public TcMainWindow(TcNames names, Whiteboard whiteboard, ShortcutLinksPanel shortcutLinksPanel) {
        super("TC Clown");

        this.whiteboard = whiteboard;
        this.pnlShortcuts = shortcutLinksPanel;

        leftDirPanel = new TcDirectoryPanel(names);
        rightDirPanel = new TcDirectoryPanel(names);

        initShortCutPanel();
        JComponent pnlDirectories = initDirectoriesPanel();

        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(this.pnlShortcuts, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
        getContentPane().add(pnlDirectories, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.1, 10000.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 0, 0));

        // Testdtaen
        leftDirPanel.setDirectory(new File("/home/bichi/ispace"));
        rightDirPanel.setDirectory(new File("/tmp"));

        registerWhiteboardEvents();

    }

    private void registerWhiteboardEvents() {
        this.whiteboard.registerHandler(FileSelectedEvent.class, this);
    }

    /**
     * Initializes the panels für the directories
     *
     * @return Component for the directories-view
     */
    private JComponent initDirectoriesPanel() {
        JSplitPane pnlSplit = new JSplitPane();

        pnlSplit.setLeftComponent(leftDirPanel);
        pnlSplit.setRightComponent(rightDirPanel);

        pnlSplit.setResizeWeight(0.5d);
        pnlSplit.setDividerLocation(0.5d);

        return pnlSplit;
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
    public void handleFileSelectedEvent(FileSelectedEvent event) {
        logger.info("File selected and displayed: " + event.getFile().getName());
    }
}
