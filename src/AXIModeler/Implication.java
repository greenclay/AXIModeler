package AXIModeler;

import java.util.ArrayList;

import edu.stanford.nlp.trees.Tree;
import java.util.logging.*;

public class Implication {

	public class Clause {

		public ArrayList<String> words;
		public Signal signal;
		public Tree tree;

		public Clause(ArrayList<String> words) {
			this.words = words;
			findTree();
//			if (words.size() > 0) System.out.println(tree);
		}

		public void setSignal(Signal signal) {
			this.signal = signal;
		}

		public ArrayList<String> getWords() {
			return words;
		}

		public Signal getSignal() {
			return signal;
		}

		public void setWords(ArrayList<String> words) {
			this.words = words;
			findTree();
		}

		private void findTree() {
			String sentence = "";
			for (String str : words) {
				if (str.equals(".")) continue;
				if (str.equals("when")) continue;
				if (str.equals("When")) continue;
				sentence = sentence + " " + str;
			}
			sentence = sentence + ".";

			// if (sentence.equals(".")) sentence = "";
			tree = Modeler.getTree(sentence);
		}

		public Tree getTree() {
			return tree;
		}
	}

	public Clause getAnt1() {
		return ant1;
	}

	public Clause getAnt2() {
		return ant2;
	}

	public Clause getCon1() {
		return con1;
	}

	public Clause getCon2() {
		return con2;
	}

	private static final Logger log = Logger.getLogger( Implication.class.getName() );

	private Clause ant1;
	private Clause ant2;
	private Clause con1;
	private Clause con2;

	ArrayList<String> antArr1;
	ArrayList<String> antArr2;
	ArrayList<Signal> antSignals;
	Signal antSignal;

	ArrayList<String> conArr1;
	ArrayList<String> conArr2;
	ArrayList<Signal> conSignals;
	VerilogMaker verilog;
	
	private boolean doubleImplication = false;
	
	public Implication(ArrayList<String> ant, ArrayList<String> con) {

		// Fresh and new Clause constructor
		ant1 = new Clause(ant);
		ant2 = new Clause(new ArrayList<String>());
		con1 = new Clause(con);
		con2 = new Clause(new ArrayList<String>());

		// Old Implication constructor
		this.antArr1 = ant;
		this.conArr1 = con;
		antArr2 = new ArrayList<String>();
		conArr2 = new ArrayList<String>();
		conSignals = new ArrayList<Signal>();
		antSignals = new ArrayList<Signal>();
	}

	public void printSignalsOld() {
		for (Signal signal : conSignals) {
			signal.print();
		}
		for (Signal signal : antSignals) {
			signal.print();
		}
	}

	public void printSignals() {
		if (ant1.signal != null) ant1.signal.print();
		if (ant2.signal != null) ant2.signal.print();

		if (con1.signal != null) con1.signal.print();
		if (con2.signal != null) con2.signal.print();
	}

	public void makeVerilog() {
		if (con1.signal != null) conSignals.add(con1.signal);
		if (con2.signal != null) conSignals.add(con2.signal);
		if (ant1.signal != null) antSignals.add(ant1.signal);
		if (ant2.signal != null) antSignals.add(ant2.signal);

		if (conSignals.size() > 0 || antSignals.size() > 0) {

			verilog = new VerilogMaker(antSignals, conSignals);

//			for (String str : verilog.getVerliog()) {
//				System.out.println(str);
//				log.warning(str);
//			}
		}
	}

	public void makeVerilogOld() {
		if (conSignals.size() > 0 || antSignals.size() > 0) {
			verilog = new VerilogMaker(antSignals, conSignals);

			for (String str : verilog.getVerliog()) {
				System.out.println(str);
			}
		}
	}

	public void fillSignals() {
		// make Signals out of the ant/ant2 and con/con2 phrases
		Signal signal = makeAntSignals(antArr1);
		if (signal != null) {
			antSignals.add(signal);
		}

		signal = makeAntSignals(antArr2);
		if (signal != null) {
			antSignals.add(signal);
		}

		signal = makeConSignals(conArr1);
		if (signal != null) {
			conSignals.add(signal);
		}

		signal = makeConSignals(conArr2);
		if (signal != null) {
			conSignals.add(signal);
		}
	}

	public Signal makeAntSignals(ArrayList<String> phrase) {

		Signal signal = null;
		// System.out.println(phrase);

		// Look for an RWO in the table that matches the phrase
		for (String str : phrase) {
			RWO rwo = RWOTable.searchInput(str);
			// System.out.println(str + " " + rwo);

			// If matching rwo was found in the rwotable
			if (rwo != null) {
				for (String str2 : phrase) {
					String assignment = TransactionTable.searchTrans(str2);
					// System.out.println(str2 + " " + assignment);
					if (assignment != null) {
						signal = new Signal(rwo, str2, assignment);
						break;
					}
				}
			}
		}
		return signal;
	}

	public Signal makeConSignals(ArrayList<String> phrase) {
		Signal signal = null;
		// System.out.println(phrase);
		// Look for an RWO in the table that matches the phrase

		for (String str : phrase) {
			RWO rwo = RWOTable.searchOutput(str);
			// System.out.println(str + " " + rwo);
			if (rwo != null) {
				for (String str2 : phrase) {
					String assignment = TransactionTable.searchTrans(str2);
					// System.out.println(str2 + " " + assignment);
					if (assignment != null) {
						signal = new Signal(rwo, str2, assignment);
						break;
					}
				}
			}
		}
		return signal;
	}

	public void searchRWOTable() {
		// Search ant
		for (String str : antArr1) {
			RWO rwo = RWOTable.searchInput(str);
			if (rwo != null) {
//				rwo.print();
			}
			rwo = RWOTable.searchOutput(str);
			if (rwo != null) {
//				rwo.print();
			}
		}

		// Search con
		for (String str : conArr1) {
			RWO rwo = RWOTable.searchInput(str);
			if (rwo != null) {
//				rwo.print();
			}
			rwo = RWOTable.searchOutput(str);
			if (rwo != null) {
//				rwo.print();
			}
		}
	}

	public void searchTransactionTable() {
		// Search ant
		for (String str : antArr1) {
			String signalValue = TransactionTable.searchTrans(str);
			if (signalValue != null) {
				System.out.println(signalValue);
			}
		}

		// Search con
		for (String str : conArr1) {
			String signalValue = TransactionTable.searchTrans(str);
			if (signalValue != null) {
				System.out.println(signalValue);
			}
		}
	}

	public void setDoubleImplifcation() {
		doubleImplication = true;
	}
	
	public void setAntArr(ArrayList<String> ant) {
		this.antArr1 = ant;
	}

	public void setConArr(ArrayList<String> con) {
		this.conArr1 = con;
	}

	public void setAntArr2(ArrayList<String> ant) {
		this.antArr2 = ant;
	}

	public void setConArr2(ArrayList<String> con) {
		this.conArr2 = con;
	}

	public ArrayList<String> getAnt() {
		return antArr1;
	}

	public ArrayList<String> getCon() {
		return conArr1;
	}

	public void printAntArr() {
		System.out.println("--Antecedent--");
		String str1 = "";
		for (String s : antArr1) {
			str1 = str1 + s + " ";
		}
		System.out.println(str1);

		if (antArr2.size() > 0) {
			String str2 = "";
			for (String s : antArr2) {
				str2 = str2 + s + " ";
			}
			System.out.println(str2);
		}

	}

	public void printConArr() {
		System.out.println("--Consequent--");
		String str1 = "";
		for (String s : conArr1) {
			str1 = str1 + s + " ";
		}
		System.out.println(str1);

		if (antArr2.size() > 0) {
			String str2 = "";
			for (String s : conArr2) {
				str2 = str2 + s + " ";
			}
			System.out.println(str2);
		}
	}

	public void printImplication() {
		System.out.println("-- Antecedent --");
		String str = "";
		for (String s : ant1.words) {
			str = str + s + " ";
		}
		System.out.println(str);
		System.out.println(ant1.getTree());

		if (ant2.words.size() > 0) {
			str = "";
			for (String s : ant2.words) {
				str = str + s + " ";
			}
			System.out.println(str);
			System.out.println(ant2.getTree());
		}

		System.out.println("-- Consequent --");
		str = "";
		for (String s : con1.words) {
			str = str + s + " ";
		}
		System.out.println(str);
		System.out.println(con1.getTree());
		
		if (con2.words.size() > 0) {
			str = "";
			for (String s : con2.words) {
				str = str + s + " ";
			}
			System.out.println(str);
			System.out.println(con2.getTree());
		}
	}

}