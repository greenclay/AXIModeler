package AXIModeler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.nlp.trees.Tree;

public class OutputWriter {

	private static ArrayList<String> log;
	
	static {
		log = new ArrayList<String>();
	}
	
	public static void write() {
		log.add("\n");
	}
	
	public static void write(String str) {
		log.add(str);
		
	}
	
	public static void printAll() {
		log.forEach((str) -> System.out.println(str)); 
	}
	
	static void printVerilog(Implication imp) {
		if (imp.verilog != null) {
			for (String str : imp.verilog.getVerliog()) {
				OutputWriter.write(str);
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

			OutputWriter.write("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(int size) {
		log.add(Integer.toString(size));
	}

	public static void write(Tree tree) {
		log.add(tree.toString());
	}
}
