package AXIModeler;

public class OutputWriter {
	
	
	
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

}
