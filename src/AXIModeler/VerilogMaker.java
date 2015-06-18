package AXIModeler;

import java.text.MessageFormat;
import java.util.ArrayList;

public class VerilogMaker {
	ArrayList<String> verilog;
	ArrayList<Signal> antSignals;
	ArrayList<Signal> conSignals;

	public VerilogMaker(ArrayList<Signal> antSignals, ArrayList<Signal> conSignals) {
		verilog = new ArrayList<String>();
		this.antSignals = antSignals;
		this.conSignals = conSignals;

		String line1 = "assert property (@(posedge clock)";
		verilog.add(line1);
		if (antSignals.size() > 0) antVerilog(antSignals);
		else System.out.println(antSignals.size());
		if (conSignals.size() > 0) conVerilog();
		addClosingParenthesis();
		
	}

	private void addClosingParenthesis() {
		String lastLine = verilog.get(verilog.size() - 1);
		lastLine = lastLine + ");";
		verilog.set(verilog.size()-1, lastLine);
	}
	
	// Make System Verilog for the consequent clauses
	private void conVerilog() {
		boolean switchConAnt = false; // If true switch the order of the antecdent and consequent verilog
		Signal signal1 = conSignals.get(0);
		String line = null;

		// Only 1 clause for the consequent
		if (conSignals.size() == 1) {
			if (signal1.getAssignment().equals("$stable")) {
				line = "|-> " + signal1.getAssignment() + "(" + signal1.getRWO().getName() + ")";
			} else if (signal1.getAssignment().equals(" == 1") || signal1.getAssignment().equals(" == 0")) {
				line = "|-> " + "(" + signal1.getRWO().getName() + signal1.getAssignment() + ")";
			} else if (signal1.getAssignment().
					equals("cluster3")) {
				line = MessageFormat.format("|-> (##1 $stable({0}) [*1:$] ##1 ({1} == {2}))", conSignals.get(0).getRWO().getName(), signal1.getRWO().getName(), "1");

				// Used exception X, rewrite it later to pick X out of sentence
			} 
			else if (signal1.getAssignment().equals("cluster5")) {
				line = MessageFormat.format("|-> ({0} != {1})", conSignals.get(0).getRWO().getName(), con);
			} else if (signal1.getAssignment().equals("cluster11")) {
				line = MessageFormat.format("(({0} == 1) && (##1 {1} == 0))", conSignals.get(0).getRWO().getName(), conSignals.get(0).getRWO().getName());
				
				// Switch the placement of lines of the consequent and antecedent in the verilog since this is a special case.
				switchConAnt = true;
			} else if (signal1.getAssignment().equals("cluster6")) {
				line = "|-> (({0} == {1}) ##1({2} == {3}))";
				line = MessageFormat.format(line, signal1.getRWO().getName(), "0", signal1.getRWO().getName(), "0");
			} else if (signal1.getAssignment().equals("cluster10")) {
				line = "|-> ##[1:<parameter1>]({0} == {1})";
				line = MessageFormat.format(line, "MAXWAITS", signal1.getRWO().getName());
			}
		}
		
		// 2 clauses for the consequent
		else {
			Signal signal2 = conSignals.get(1);
			if(signal1.getRWO().getName().equals("ARCACHE1") && signal2.getRWO().getName().equals("ARCACHE32")) {
				line = "|-> ((%s %s) |-> (%s %s))";
				line = String.format(line, signal1.getRWO().getName(), signal1.getAssignment(), signal2.getRWO().getName(), signal2.getAssignment());
			}
		}

		if (switchConAnt == true) {
			int conIndex = verilog.size() - 1;
			String tempStr = verilog.get(conIndex);
			verilog.set(conIndex, line);
			verilog.add("|-> " + tempStr);
		} else {
			verilog.add(line);
		}
	}
	
	// Make System Verilog for the antecedent statements
	private void antVerilog(ArrayList<Signal> antSignals) {
		Signal signal1 = antSignals.get(0);
		String str1 = "(" + signal1.getRWO().getName() + signal1.getAssignment() + ")";
		String str2;
		String line = str1;
		// If the ant consists of 2 phrases
		if (antSignals.size() == 2) {
			Signal signal2 = antSignals.get(1);
			str2 = "(" + signal2.getRWO().getName() + signal2.getAssignment() + ")";
			line = "(" + str1 + " && " + str2 + ")";
		}
		// cluster 1 exception made
		else if (signal1.getAssignment().equals("cluster1")) {
			line = MessageFormat.format("({0} >= {1})", antSignals.get(0).getRWO().getName(), "1");
		} else if(signal1.getAssignment().equals("cluster4")) {
			line = "RESET != 1";
		}
		// Ant consists of 1 phrase
		else {
			line = str1;
		}
		verilog.add(line);


	}

	public ArrayList<String> getVerliog() {
		return verilog;
	}
}
