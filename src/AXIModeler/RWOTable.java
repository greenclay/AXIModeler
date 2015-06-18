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
		tableInputs.put("reset", new RWO("reset", 1, "boolean", "in"));
		tableInputs.put("ARESETn", new RWO("ARESETn", 1, "boolean", "in"));
		tableInputs.put("ARVALID", new RWO("ARVALID", 1, "boolean", "in"));
		tableInputs.put("ARCACHE1", new RWO("ARCACHE1", 1, "boolean", "in"));
		tableInputs.put("", new RWO("", 1, "boolean", "in"));
		tableInputs.put("WVALID", new RWO("WVALID", 1, "boolean", "in"));
		tableInputs.put("WREADY", new RWO("WREADY", 1, "boolean", "in"));
		tableInputs.put("WID", new RWO("WID", 1, "boolean", "in"));
		tableInputs.put("WDATA", new RWO("WDATA", 1, "boolean", "in"));
		tableInputs.put("BVALID", new RWO("BVALID", 1, "boolean", "in"));
		tableInputs.put("BREADY", new RWO("BREADY", 1, "boolean", "in"));
		tableInputs.put("ARREADY", new RWO("ARREADY", 1, "boolean", "in"));
		tableInputs.put("ARREADY", new RWO("ARREADY", 1, "boolean", "in"));
		tableInputs.put("RREADY", new RWO("RREADY", 1, "boolean", "in"));
		tableInputs.put("AWUSERWIDTH", new RWO("AWUSERWIDTH", 1, "boolean", "in"));
		tableInputs.put("BUSERWIDTH", new RWO("BUSERWIDTH", 1, "boolean", "in"));
		tableInputs.put("RUSERWIDTH", new RWO("RUSERWIDTH", 1, "boolean", "in"));
		tableInputs.put("EXMONWIDTH", new RWO("EXMONWIDTH", 1, "boolean", "in"));
		tableInputs.put("IDWIDTH", new RWO("IDWIDTH", 1, "boolean", "in"));
		tableInputs.put("WDEPTH", new RWO("WDEPTH", 1, "boolean", "in"));
		tableInputs.put("MAXRBURSTS", new RWO("MAXRBURSTS", 1, "boolean", "in"));
		tableInputs.put("MAXWBURSTS", new RWO("MAXWBURSTS", 1, "boolean", "in"));
		tableInputs.put("", new RWO("", 1, "boolean", "in"));

		// Output table
		tableOutputs.put("light", new RWO("light", 1, "boolean", "out"));
		tableOutputs.put("AWADDR", new RWO("AWADDR", 1, "boolean", "out"));
		tableOutputs.put("AWCACHE32", new RWO("AWCACHE32", 1, "boolean", "out"));
		tableOutputs.put("ARCACHE32", new RWO("ARCACHE32", 1, "boolean", "out"));
		tableOutputs.put("AWID", new RWO("AWID", 1, "boolean", "out"));
		tableOutputs.put("CSYSREQ", new RWO("CSYSREQ", 1, "boolean", "out"));
		tableOutputs.put("AWREADY", new RWO("AWREADY", 1, "boolean", "out"));
		tableOutputs.put("RDATA", new RWO("RDATA", 1, "boolean", "out"));
		tableOutputs.put("AWLEN", new RWO("AWLEN", 1, "boolean", "out"));
		tableOutputs.put("AWSIZE", new RWO("AWSIZE", 1, "boolean", "out"));
		tableOutputs.put("AWBURST", new RWO("AWBURST", 1, "boolean", "out"));
		tableOutputs.put("", new RWO("", 1, "boolean", "out"));

		tableOutputs.put("AWCACHE", new RWO("AWCACHE", 1, "boolean", "out"));
		tableOutputs.put("AWPROT", new RWO("AWPROT", 1, "boolean", "out"));
		tableOutputs.put("AWVALID", new RWO("AWVALID", 1, "boolean", "out"));
		tableOutputs.put("AWLOCK", new RWO("AWLOCK", 1, "boolean", "out"));
		tableOutputs.put("WID", new RWO("WID", 1, "boolean", "out"));
		tableOutputs.put("WVALID", new RWO("WVALID", 1, "boolean", "out"));
		tableOutputs.put("WREADY", new RWO("WREADY", 1, "boolean", "out"));
		tableOutputs.put("WSTRB", new RWO("WSTRB", 1, "boolean", "out"));
		tableOutputs.put("WLAST", new RWO("WLAST", 1, "boolean", "out"));
		tableOutputs.put("BID", new RWO("BID", 1, "boolean", "out"));
		tableOutputs.put("BRESP", new RWO("BRESP", 1, "boolean", "out"));
		tableOutputs.put("ARID", new RWO("ARID", 1, "boolean", "out"));
		tableOutputs.put("ARADDR", new RWO("ARADDR", 1, "boolean", "out"));
		tableOutputs.put("ARLEN", new RWO("ARLEN", 1, "boolean", "out"));
		tableOutputs.put("ARSIZE", new RWO("ARSIZE", 1, "boolean", "out"));
		tableOutputs.put("ARBURST", new RWO("ARBURST", 1, "boolean", "out"));
		tableOutputs.put("ARLOCK", new RWO("ARLOCK", 1, "boolean", "out"));
		tableOutputs.put("ARCACHE", new RWO("ARCACHE", 1, "boolean", "out"));
		tableOutputs.put("ARPROT", new RWO("ARPROT", 1, "boolean", "out"));
		tableOutputs.put("RID", new RWO("RID", 1, "boolean", "out"));
		tableOutputs.put("RRESP", new RWO("RRESP", 1, "boolean", "out"));

		tableOutputs.put("RLAST", new RWO("RLAST", 1, "boolean", "out"));
		tableOutputs.put("CSYSACK", new RWO("CSYSACK", 1, "boolean", "out"));
		tableOutputs.put("CACTIVE", new RWO("CACTIVE", 1, "boolean", "out"));
		tableOutputs.put("AWUSERWIDTH", new RWO("AWUSERWIDTH", 1, "boolean", "out"));
		tableOutputs.put("", new RWO("", 1, "boolean", "out"));

		tableOutputs.put("", new RWO("", 1, "boolean", "out"));
		tableOutputs.put("", new RWO("", 1, "boolean", "out"));
		tableOutputs.put("", new RWO("", 1, "boolean", "out"));
		tableOutputs.put("", new RWO("", 1, "boolean", "out"));
		tableOutputs.put("", new RWO("", 1, "boolean", "out"));
		tableOutputs.put("", new RWO("", 1, "boolean", "out"));
		tableOutputs.put("", new RWO("", 1, "boolean", "out"));

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
