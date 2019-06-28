package de.nukaboy.eAquaToGephi;

import java.awt.EventQueue;

import de.nukaboy.eAquaToGephi.gui.MainWindow;

public class MainClass {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
