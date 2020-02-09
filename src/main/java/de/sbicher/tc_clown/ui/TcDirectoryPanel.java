package de.sbicher.tc_clown.ui;

import de.sbicher.tc_clown.i18n.TcNames;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel for the display of the files of a directory
 */
public class TcDirectoryPanel extends JPanel {

    private final Logger logger = LoggerFactory.getLogger(TcDirectoryPanel.class);

    private final TcFileTableModel fileTableModel;

    private final TcNames names;

    private final JTable fileTable = new JTable();

    public TcDirectoryPanel(TcNames names) {
        this.names = names;
         this.fileTableModel = new TcFileTableModel(names);

         initFileTable();

        JScrollPane scrContent = new JScrollPane(fileTable);
        scrContent.setBackground(Color.blue);
        scrContent.setMinimumSize(new Dimension(20,20));
     //   scrContent.setPreferredSize(new Dimension(4000,4000));
        // scrContent.add(fileTable);

        this.add(scrContent);
   }

    /**
     * Initializes the table for the directory contents
     */
    private void initFileTable() {
        //     fileTableModel.setColumnIdentifiers(new Object[] {names.get("fileName"),names.get("fileSize"),names.get("fileLastModified")});
        fileTable.setModel(fileTableModel);
    }

    public void setDirectory(File newDir) {
        this.showDirectoryContent(newDir);
    }

    private void showDirectoryContent(File newDir) {
        fileTableModel.setFiles(newDir.listFiles());

    }

}
