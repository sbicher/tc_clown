package de.sbicher.tc_clown;

import de.sbicher.tc_clown.i18n.TcNames;
import de.sbicher.tc_clown.ui.TcMainWindow;
import javax.swing.JFrame;

public class TcClownMain {

	public static void main (String[] args) {
		TcNames names = new TcNames();
		TcMainWindow mainWindow = new TcMainWindow(names);

		mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
}
