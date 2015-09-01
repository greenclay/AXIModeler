package AXIModeler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OutputWriter {

	public static ArrayList<String> output;
	
	static {
		output = new ArrayList<String>();
	}
	
	public static void log(String str) {
		output.add(str);
		
	}
	
	static void printVerilog(Implication imp) {
		if (imp.verilog != null) {
			for (String str : imp.verilog.getVerliog()) {
				System.out.println(str);
			}
		}
	}

	static void printSignals(Implication imp) {
		if (imp.getAnt1().signal != null) imp.getAnt1().signal.print();
		if (imp.getAnt2().signal != null) imp.getAnt2().signal.print();

		if (imp.getCon1().signal != null) imp.getCon1().signal.print();
		if (imp.getCon2().signal != null) imp.getCon2().signal.print();
	}

	static void writeToFile() {
		try {

			String content = "This is the content to write into file";

			File file = new File("results.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
