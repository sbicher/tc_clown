package de.sbicher.tc_clown;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.sbicher.tc_clown.i18n.TcNames;
import de.sbicher.tc_clown.ui.TcMainWindow;
import javax.swing.JFrame;

public class TcClownMain {

	public static void main (String[] args) {
		Injector injector = Guice.createInjector(new TcModule());

		TcMainWindow mainWindow = injector.getInstance(TcMainWindow.class);

		mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
}
