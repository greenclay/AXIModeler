package AXIModeler;
import java.util.*;

public class RWOTable {
	public static HashMap<String, RWO> tableInputs;
	public static HashMap<String, RWO> tableOutputs;

	public static void init() {
		tableInputs = new HashMap<String, RWO>();
		tableOutputs = new HashMap<String, RWO>();
		
		// Inputs table
		tableInputs.put("button", new RWO("button", 1, "boolean", "in"));
		tableInputs.put("switch", new RWO("switch", 1, "boolean", "in"));
		tableInputs.put("switch", new RWO("switch", 1, "boolean", "in"));
		tableInputs.put("switch", new RWO("switch", 1, "boolean", "in"));
		tableInputs.put("AWVALID", new RWO("AWVALID", 1, "boolean", "in"));
		tableInputs.put("AWREADY", new RWO("AWREADY", 1, "boolean", "in"));
		tableInputs.put("CSYSACK", new RWO("CSYSACK", 1, "boolean", "in"));
		tableInputs.put("AWCACHE1", new RWO("AWCACHE1", 1, "boolean", "in"));
		tableInputs.put("AWUSER_WIDTH", new RWO("AWUSER_WIDTH", 1, "boolean", "in"));
		tableInputs.put("RVALID", new RWO("RVALID", 1, "boolean", "in"));
		
		// Output table
		tableOutputs.put("light", new RWO("light", 1, "boolean", "out"));
		tableOutputs.put("AWADDR", new RWO("AWADDR", 1, "boolean", "out"));
		tableOutputs.put("AWCACHE32", new RWO("AWCACHE32", 1, "boolean", "out"));
		tableOutputs.put("AWID", new RWO("AWID", 1, "boolean", "out"));
		tableOutputs.put("CSYSREQ", new RWO("CSYSREQ", 1, "boolean", "out"));
		tableOutputs.put("AWREADY", new RWO("AWREADY", 1, "boolean", "out"));
		tableOutputs.put("RDATA", new RWO("AWREADY", 1, "boolean", "out"));
	}

	public static RWO searchInput(String str) {
		RWO rwo = tableInputs.get(str);
		return rwo;
	}
	public static RWO searchOutput(String str) {
		RWO rwo = tableOutputs.get(str);
		return rwo;
	}
}
