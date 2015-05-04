import java.util.ArrayList;

public class AntCon {

	ArrayList<String> ant;
	ArrayList<String> ant2;
	ArrayList<Signal> antSignals;

	ArrayList<String> con;
	ArrayList<String> con2;
	ArrayList<Signal> conSignals;
	Verilog verilog;

	public AntCon(ArrayList<String> ant, ArrayList<String> con) {
		this.ant = ant;
		this.con = con;
		ant2 = new ArrayList<String>();
		con2 = new ArrayList<String>();
		conSignals = new ArrayList<Signal>();
		antSignals = new ArrayList<Signal>();
		verilog = new Verilog(conSingals, antSignals);
	}
	
	public void printSignals() {
		for (Signal signal : conSignals) {
			signal.print();
		}
		for (Signal signal : antSignals) {
			signal.print();
		}
	}
	
	public void printVerilog() {
		for (int i=0; i< antSignals.size(); i++) {
			antSignals.get(i).verilogAnt(i);
		}
		for (int i=0; i< conSignals.size(); i++) {
			conSignals.get(i).verilogCon(i);
		}

	}
	
	public void fillSignals() {
		// make Signals out of the ant/ant2 and con/con2 phrases
		Signal signal = makeSignals(ant);
		if(signal != null) {
			antSignals.add(signal);
		}
		
		signal = makeSignals(ant2);
		if(signal != null) {
			antSignals.add(signal);
		}
		
		signal = makeSignals(con);
		if(signal != null) {
			conSignals.add(signal);
		}
		
		signal = makeSignals(con2);
		if(signal != null) {
			conSignals.add(signal);
		}
	}
	
	public Signal makeSignals(ArrayList<String> phrase) {
		Signal signal = null;
		
		// Look for an RWO in the table that matches the phrase

		for (String str : phrase) {
			RWO rwo = RWOTable.searchInput(str);
			if (rwo != null) {
				for (String str2 : phrase) {
					String assignment = TransactionTable.searchTrans(str2);
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
		for (String str : ant) {
			RWO rwo = RWOTable.searchInput(str);
			if (rwo != null) {
				rwo.print();
			}
			rwo = RWOTable.searchOutput(str);
			if (rwo != null) {
				rwo.print();
			}
		}

		// Search con
		for (String str : con) {
			RWO rwo = RWOTable.searchInput(str);
			if (rwo != null) {
				rwo.print();
			}
			rwo = RWOTable.searchOutput(str);
			if (rwo != null) {
				rwo.print();
			}
		}
	}

	public void searchTransactionTable() {
		// Search ant
		for (String str : ant) {
			String signalValue = TransactionTable.searchTrans(str);
			if (signalValue != null) {
				System.out.println(signalValue);
			}
		}

		// Search con
		for (String str : con) {
			String signalValue = TransactionTable.searchTrans(str);
			if (signalValue != null) {
				System.out.println(signalValue);
			}
		}
	}

	public void setAnt(ArrayList<String> ant) {
		this.ant = ant;
	}

	public void setCon(ArrayList<String> con) {
		this.con = con;
	}

	public void setAnt2(ArrayList<String> ant) {
		this.ant2 = ant;
	}

	public void setCon2(ArrayList<String> con) {
		this.con2 = con;
	}

	public ArrayList<String> getAnt() {
		return ant;
	}

	public ArrayList<String> getCon() {
		return con;
	}

	public void printAnt() {
		System.out.println("--Antecedent--");
		String str1 = "";
		for (String s : ant) {
			str1 = str1 + s + " ";
		}
		System.out.println(str1);

		if (ant2.size() > 0) {
			String str2 = "";
			for (String s : ant2) {
				str2 = str2 + s + " ";
			}
			System.out.println(str2);
		}

	}

	public void printCon() {
		System.out.println("--Consequent--");
		String str1 = "";
		for (String s : con) {
			str1 = str1 + s + " ";
		}
		System.out.println(str1);

		if (ant2.size() > 0) {
			String str2 = "";
			for (String s : con2) {
				str2 = str2 + s + " ";
			}
			System.out.println(str2);
		}

	}
}