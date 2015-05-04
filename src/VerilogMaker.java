import java.util.ArrayList;


public class VerilogMaker {
	ArrayList<String> verilog;
	
	public VerilogMaker(ArrayList<Signal> conSignals, ArrayList<Signal> antSignals) {
		verilog = new ArrayList<String>();
		
		String line1 = "assert property (@(posedge clock)";
		verilog.add(line1);
		
		
	}
}
