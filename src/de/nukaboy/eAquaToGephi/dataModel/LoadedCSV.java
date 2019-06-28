package de.nukaboy.eAquaToGephi.dataModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoadedCSV {
	public Row[] rows;

	public LoadedCSV(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String st;
		st = br.readLine();
		String[] rs = st.split(";");
		rows = new Row[rs.length];
		for (int i = 0; i < rs.length; i++) {
			rows[i] = new Row();
			rows[i].name = rs[i];
			rows[i].position = i;
		}
		while ((st = br.readLine()) != null) {
			rs = st.split(";");
			for (int i = 0; i < rs.length; i++) {
				rows[i].data.add(rs[i]);
			}
		}
		br.close();
	}
}
