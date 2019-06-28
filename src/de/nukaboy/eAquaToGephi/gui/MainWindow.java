package de.nukaboy.eAquaToGephi.gui;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.nukaboy.eAquaToGephi.dataModel.LoadedCSV;
import de.nukaboy.eAquaToGephi.dataModel.Row;

public class MainWindow {

	public JFrame frame;
	JPanel list;
	HashSet<JPanel> listEntries = new HashSet<>();
	File fp = null;
	LoadedCSV loadedCSV;
	boolean toIntCh;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnSpeichern = new JMenu("Datei");
		menuBar.add(mnSpeichern);

		JMenuItem mntmLaden = new JMenuItem("Laden");
		mnSpeichern.add(mntmLaden);

		JMenuItem mntmSpeichern = new JMenuItem("Speichern");
		mnSpeichern.add(mntmSpeichern);

		list = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		list.add(new JPanel(), gbc);
		JScrollPane scrollPane = new JScrollPane(list);
		frame.getContentPane().add(scrollPane);

		JMenuBar menuBar_1 = new JMenuBar();
		scrollPane.setColumnHeaderView(menuBar_1);

		JLabel lblNewLabel = new JLabel("ID:");
		menuBar_1.add(lblNewLabel);

		JComboBox<ComboObject> comboBox = new JComboBox();
		menuBar_1.add(comboBox);

		JLabel lblNewLabel_1 = new JLabel("Source:");
		menuBar_1.add(lblNewLabel_1);

		JComboBox<ComboObject> comboBox_1 = new JComboBox();
		menuBar_1.add(comboBox_1);

		JLabel lblNewLabel_2 = new JLabel("Target:");
		menuBar_1.add(lblNewLabel_2);

		JComboBox<ComboObject> comboBox_2 = new JComboBox();
		menuBar_1.add(comboBox_2);

		JCheckBox chckbxCopyIdTo = new JCheckBox("ID zu Label");
		menuBar_1.add(chckbxCopyIdTo);
		JCheckBox chckbxClearInt = new JCheckBox("Zahlen bereinigen");
		menuBar_1.add(chckbxClearInt);

		mntmLaden.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Tabelle", "csv");
				chooser.addChoosableFileFilter(filter);
				int rv = chooser.showOpenDialog(null);
				if (rv == JFileChooser.APPROVE_OPTION) {
					try {
						clearList();
						comboBox.removeAllItems();
						comboBox_1.removeAllItems();
						comboBox_2.removeAllItems();
						fp = chooser.getSelectedFile();
						loadedCSV = new LoadedCSV(fp);
						for (Row row : loadedCSV.rows) {
							addDataRow(row);
							comboBox.addItem(new ComboObject(row));
							comboBox_1.addItem(new ComboObject(row));
							comboBox_2.addItem(new ComboObject(row));
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		mntmSpeichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fp == null)
					return;
				File fileN = new File(fp.getAbsolutePath() + "Nodes.csv");
				File fileE = new File(fp.getAbsolutePath() + "Edges.csv");
				toIntCh = chckbxClearInt.isSelected();
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(fileN));
					writer.append("ID");
					if (chckbxCopyIdTo.isSelected())
						writer.append(";Label");
					ArrayList<Row> nRows = new ArrayList<Row>();
					nRows.add(loadedCSV.rows[((ComboObject) comboBox.getSelectedItem()).row]);
					for (Row row : loadedCSV.rows) {
						if (row.toNode) {
							writer.append(";" + row.name);
							nRows.add(row);
						}
					}
					writer.append(System.lineSeparator());
					for (int i = 0; i < nRows.get(0).data.size(); i++) {
						writer.append(nRows.get(0).data.get(i));
						if (chckbxCopyIdTo.isSelected())
							writer.append(";" + nRows.get(0).data.get(i));
						for (int j = 1; j < nRows.size(); j++) {
							writer.append(";" + toInt(nRows.get(j).data.get(i)));
						}
						writer.append(System.lineSeparator());
					}
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					writer = new BufferedWriter(new FileWriter(fileE));
					writer.append("Source;Target");
					ArrayList<Row> eRows = new ArrayList<Row>();
					eRows.add(loadedCSV.rows[((ComboObject) comboBox_1.getSelectedItem()).row]);
					eRows.add(loadedCSV.rows[((ComboObject) comboBox_2.getSelectedItem()).row]);
					for (Row row : loadedCSV.rows) {
						if (row.toEdge) {
							writer.append(";" + row.name);
							eRows.add(row);
						}
					}
					writer.append(System.lineSeparator());
					for (int i = 0; i < eRows.get(0).data.size(); i++) {
						writer.append(eRows.get(0).data.get(i));
						for (int j = 1; j < eRows.size(); j++) {
							writer.append(";" + toInt(eRows.get(j).data.get(i)));
						}
						writer.append(System.lineSeparator());
					}
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	String toInt(String input) {
		if (!toIntCh || !input.matches("(\\,|[0-9]|\\.|\\-)*"))
			return input;
		else {
			return input.replaceAll("\\,[0-9]|\\.", "");
		}
	}

	public void addDataRow(Row r) {
		JPanel panel = new JPanel();
		panel.add(new JLabel(r.name));
		Checkbox c1 = new Checkbox("Zu Knoten hinzufügen");
		Checkbox c2 = new Checkbox("Zu Kanten hinzufügen");
		c1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				r.toNode = c1.getState();
			}
		});
		c2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				r.toEdge = c2.getState();
			}
		});
		panel.add(c1);
		panel.add(c2);
		panel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		list.add(panel, gbc, 0);
		listEntries.add(panel);
		frame.validate();
		frame.repaint();
	}

	public void clearList() {
		for (JPanel jPanel : listEntries) {
			list.remove(jPanel);
		}
		frame.validate();
		frame.repaint();
	}

	private class ComboObject {
		int row;
		String name;

		public ComboObject(Row row) {
			name = row.name;
			this.row = row.position;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
