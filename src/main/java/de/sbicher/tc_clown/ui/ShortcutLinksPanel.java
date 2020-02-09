package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.event.Whiteboard;
import de.sbicher.tc_clown.event.impl.FileSelectedEvent;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Panel for the shortcut to certain directories
 */
public class ShortcutLinksPanel extends JPanel implements ActionListener {

    private final Whiteboard whiteboard;

    private static final String SELECT_FILE_PREFIX = "open:";

    @Inject
    public ShortcutLinksPanel(Whiteboard whiteboard) {
        super (new FlowLayout(10));

        this.whiteboard = whiteboard;

        initShortcuts();
    }

    /**
     * Initializes the buttons for the shortcuts
     */
    private void initShortcuts() {
        String homeDir = System.getProperty("user.home");
       this.add(createShortcutButton("Downloads", homeDir + "/Downloads"));
        this.add(createShortcutButton("Home", homeDir));
    }

    /**
     * Creates the shortcut buttons for a certain path
     * @param displayName  Name which is displayed on the button
     * @param path Absolute or relative path to the file, where the button should lead to
     * @return Created button
     */
    private JButton createShortcutButton(String displayName, String path) {
        JButton btnShortcut = new JButton(displayName);
        btnShortcut.addActionListener(this);
        btnShortcut.setActionCommand(SELECT_FILE_PREFIX + path);

        return btnShortcut;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.startsWith(SELECT_FILE_PREFIX)) {
            String path = command.substring(SELECT_FILE_PREFIX.length());
            whiteboard.fireEvent(new FileSelectedEvent(this,new File(path)));
        }
    };

}
