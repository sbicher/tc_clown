package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.event.Whiteboard;
import de.sbicher.tc_clown.event.impl.NavigateToDirEvent;
import de.sbicher.tc_clown.event.impl.NavigateToPackedContentEvent;
import de.sbicher.tc_clown.event.impl.StartFileEvent;
import de.sbicher.tc_clown.model.TcFileInfo;
import de.sbicher.tc_clown.settings.TcSettings;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.inject.Inject;
import javax.swing.JTable;

/**
 * Key listener for the file navigation and execution
 */
public class FileNavigationListener extends KeyAdapter {

    private final TcSettings settings;
    private final Whiteboard whiteboard;

    @Inject
    public FileNavigationListener(TcSettings settings, Whiteboard whiteboard) {
        this.settings = settings;
        this.whiteboard = whiteboard;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        JTable source = (JTable) e.getSource();
        TcFileTableModel fileTableModel = (TcFileTableModel) source.getModel();

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            TcFileInfo fileInfo = fileTableModel.getFileInfo(source.getSelectedRow());
            navigateToNextFolderOrStartFile(fileTableModel, fileInfo);
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (!e.isControlDown()) {
                navigateToNextFolderOrStartFile(fileTableModel, TcFileInfo.NAVIGATE_UP);
                return;
            }

            // navigate to top
            File topFile = fileTableModel.getCurrentDirectory();
            while (topFile.getParentFile() != null) {
                topFile = topFile.getParentFile();
            }
            navigateToNextFolderOrStartFile(fileTableModel, new TcFileInfo(topFile));
        }
    }

    private void navigateToNextFolderOrStartFile(TcFileTableModel fileTableModel, TcFileInfo fileInfo) {
        File currentDir = fileTableModel.getCurrentDirectory();

        if (fileInfo == TcFileInfo.NAVIGATE_UP) {
            whiteboard.fireEvent(new NavigateToDirEvent(currentDir, currentDir.getParentFile(), this));
            return;
        }
        if (fileInfo.getFile().isDirectory()) {
            whiteboard.fireEvent(new NavigateToDirEvent(currentDir, fileInfo.getFile(), this));
            return;
        }

        if (fileInfo.getName().toLowerCase().endsWith(".zip")) {
            // zip-File
            whiteboard.fireEvent(new NavigateToPackedContentEvent(fileInfo.getFile(), this));
            return;
        }

        // Datei starten
        whiteboard.fireEvent(new StartFileEvent(fileInfo, this));
    }
}
