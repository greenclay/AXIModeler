package AXIModeler;
// Rename to InputOutput
public class InputOutput {
	private String name = ""; // name of Input or Output object
	private RWO rwo;

	public InputOutput(String direction, String name) {
		name = name.substring(0, 1);
		
		if (direction.equals("IN")) {
			rwo = RWOTable.tableInputs.get(name);
		} else {
			rwo = RWOTable.tableOutputs.get(name);
		}
	}

	// Print the object and signal of the AbstractModel
	public void print() {
		System.out.println("name: " + rwo.getName() + ", numer of signals: " + rwo.getNumSignals() + ", type: "
				+ rwo.getType() + ", direction: " + rwo.getDirection());
	}

}
