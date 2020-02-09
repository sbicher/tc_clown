package de.sbicher.tc_clown.ui;

import com.google.common.collect.Sets;
import de.sbicher.tc_clown.event.TcEventHandler;
import de.sbicher.tc_clown.event.Whiteboard;
import de.sbicher.tc_clown.event.impl.NavigateToDirEvent;
import de.sbicher.tc_clown.event.impl.NavigateToPackedContentEvent;
import de.sbicher.tc_clown.event.impl.SearchChangedEvent;
import de.sbicher.tc_clown.event.impl.StartFileEvent;
import de.sbicher.tc_clown.model.TcFileInfo;
import de.sbicher.tc_clown.settings.TcSettings;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Set;
import javax.inject.Inject;
import javax.swing.JTable;

/**
 * Key listener for the file navigation and execution
 */
public class FileTableListener extends KeyAdapter implements TcEventHandler {

    private final TcSettings settings;
    private final Whiteboard whiteboard;

    // characters which are regular part of filenames
    private static final Set<Character> regularFileChars = Sets.newHashSet('-','+','$','%','&','!','(',')','?');

    private boolean searchRunning = false;

    @Inject
    public FileTableListener(TcSettings settings, Whiteboard whiteboard) {
        this.settings = settings;
        this.whiteboard = whiteboard;

        this.whiteboard.registerHandler(SearchChangedEvent.class, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        JTable source = (JTable) e.getSource();
        TcFileTableModel fileTableModel = (TcFileTableModel) source.getModel();

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // Suche hier abbrechen
            if (searchRunning) {
                searchRunning = false;
                whiteboard.fireEvent(new SearchChangedEvent('1',KeyEvent.VK_ESCAPE,this));
            }

            TcFileInfo fileInfo = fileTableModel.getFileInfo(source.getSelectedRow());
            navigateToNextFolderOrStartFile(fileTableModel, fileInfo);
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (searchRunning) {
                whiteboard.fireEvent(new SearchChangedEvent(e.getKeyChar(),e.getKeyCode(),this));
                return;
            }

            // handle navigation event
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

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
           if (this.searchRunning) {
               this.searchRunning = false;
               this.whiteboard.fireEvent(new SearchChangedEvent(e.getKeyChar(),e.getKeyCode(),this));
           }
            return;
        }

        char c = e.getKeyChar();
        if (Character.isJavaIdentifierPart(c) || Character.isWhitespace(c) || regularFileChars.contains(c)) {
            // Buchstabe eingetippt
            this.searchRunning = true;
            whiteboard.fireEvent(new SearchChangedEvent(c,e.getKeyCode(),this));
            return;
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

    public void handleSearchChangedEvent (SearchChangedEvent event) {
        if (event.getSource() == this) {
            // nix zu tun - wird hier vermerkt
        }

        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.searchRunning = false;
            return;
        }

        this.searchRunning = true;
    }
}
