package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.i18n.TcNames;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * Main window of the application - with the "classical" two-sided directories view
 */
public class TcMainWindow extends JFrame  {

    private final         TcDirectoryPanel leftDirPanel;

    private final TcDirectoryPanel rightDirPanel;

    private boolean isInLeftPanel = true;

    /**
     * Constructor
     */
    public TcMainWindow(TcNames names) {
        super ("TC Clown");

        leftDirPanel = new TcDirectoryPanel(names);
        rightDirPanel = new TcDirectoryPanel(names);

        JComponent shortCutPanel = initShortCutPanel();
        JComponent pnlDirectories = initDirectoriesPanel();

        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(shortCutPanel, new GridBagConstraints(0,0, GridBagConstraints.REMAINDER,1,0.1,0.1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(10,10,10,10),0,0));
        getContentPane().add(pnlDirectories, new GridBagConstraints(0,1, GridBagConstraints.REMAINDER,1,0.1,10000.1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,10,10,10),0,0));

        // Testdtaen
        leftDirPanel.setDirectory(new File("/home/bichi/ispace"));
        rightDirPanel.setDirectory(new File("/tmp"));

    }

    /**
     * Initializes the panels für the directories
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
     * @return short component
     */
    private JComponent initShortCutPanel() {
        JPanel pnlShortcuts = new JPanel(new FlowLayout(10));

        pnlShortcuts.add(new JButton("Downloads"));
        pnlShortcuts.add(new JButton ("Home"));

        pnlShortcuts.setPreferredSize(new Dimension(100,30));

        return pnlShortcuts;
    }
}
