import java.util.*;

public class RWOTable {
	public static HashMap<String, RWO> tableInputs;
	public static HashMap<String, RWO> tableOutputs;

	public static void init() {
		tableInputs = new HashMap<String, RWO>();
		tableOutputs = new HashMap<String, RWO>();
		tableInputs.put("button", new RWO("button", 1, "boolean", "in"));
		tableInputs.put("switch", new RWO("switch", 1, "boolean", "in"));
		tableOutputs.put("light", new RWO("light", 1, "boolean", "out"));
	}

}
