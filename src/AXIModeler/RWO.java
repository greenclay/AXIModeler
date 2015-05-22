package AXIModeler;
public class RWO {
	// Real World Object - RWO class
	// 
	private String name; // name of the Real World Object
	private int numSignals; // number of signals of the RWO
	private String type; // Type of the signal, ie - boolean, real, integer
	private String direction; // Direction is either IN or OUT so eiterh an input or output
	
	public RWO(String name, int numSignals, String type, String direction) {
		this.name = name;
		this.numSignals = numSignals;
		this.type = type;
		
		if(!direction.equals("in") || !direction.equals("out")) {
//			throw new IllegalArgumentException("direction must either be \"in\" or \"out\"");
		}
		this.direction = direction;
	}
	
	public void print() {
		String str = "name: " + name + ", direction: " + direction  + ", type: " + type + ", num signals: " + numSignals;
		System.out.println(str);
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumSignals() {
		return numSignals;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDirection() {
		return direction;
	}
	
}
