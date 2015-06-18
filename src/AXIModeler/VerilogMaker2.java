package AXIModeler;

import java.text.MessageFormat;
import java.util.ArrayList;

public class VerilogMaker2 {
	ArrayList<String> verilog;
	ArrayList<Signal> antSignals;
	ArrayList<Signal> conSignals;

	public VerilogMaker2(ArrayList<Signal> antSignals, ArrayList<Signal> conSignals) {
		verilog = new ArrayList<String>();
		this.antSignals = antSignals;
		this.conSignals = conSignals;

		String line1 = "assert property (@(posedge clock)";
		verilog.add(line1);
		if (antSignals.size() > 0) antVerilog(antSignals);
		if (conSignals.size() > 0) conVerilog();
	}

	private void conVerilog() {
		Signal signal1 = conSignals.get(0);
		String line = null;

		if (conSignals.size() == 1) {

			// if
			if (signal1.getAssignment().equals("$stable")) {
				line = "|-> " + signal1.getAssignment() + "(" + signal1.getRWO().getName() + ");";
			} else if (signal1.getAssignment().equals(" == 1") || signal1.getAssignment().equals(" == 0")) {
				line = "|-> " + "(" + signal1.getRWO().getName() + signal1.getAssignment() + ");";
			} else if (signal1.getAssignment().equals("cluster3")) {
				line = MessageFormat.format("|-> (##1 $stable(<{0}>) [*1:$] ##1 (<{1}> == <{2}>)));", conSignals.get(0).getRWO().getName(), signal1.getRWO().getName(), "1");

				// Used exception X, rewrite it later to pick X out of sentence
			} else if (signal1.getAssignment().equals("cluster5")) {
				line = MessageFormat.format("|-> ({0} != {1}));", conSignals.get(0).getRWO().getName(), "X");
			} else if (signal1.getAssignment().equals("cluster11")) {
				line = MessageFormat.format(" (({0} == 1) && (##1 {1} == 0)) |->", conSignals.get(0).getRWO().getName(), conSignals.get(0).getRWO().getName());
			}
		}
		// 2 phrases/signals
		else {
			Signal signal2 = conSignals.get(1);
			line = "";
		}
		verilog.add(line);
	}

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
			verilog.add(line);
		}
		// cluster 1 exception made
		else if (signal1.getAssignment().equals("cluster1")) {
			line = MessageFormat.format("({0} >= {1});", antSignals.get(0).getRWO().getName(), "1");
			verilog.add(line);
		}
		// Ant consists of 1 phrase
		else {
			line = str1;
			verilog.add(line);
		}

	}

	public ArrayList<String> getVerliog() {
		return verilog;
	}
}
