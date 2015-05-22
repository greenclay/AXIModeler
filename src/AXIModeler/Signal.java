package AXIModeler;
public class Signal {
	private RWO rwo;
	private String verb;
	private String assignment;
	
	public Signal(RWO rwo, String verb, String assignment) {
		// EX rwo is verb - AWVALID is asserted
		this.rwo = rwo;
		this.verb = verb;
		this.assignment = assignment;
	}
	
	public void print() {
		if (rwo != null) rwo.print();
		System.out.println("Verb: " + verb  + ", action: " + assignment + "\n");
	}
	
	public void verilogAnt(int tab) {
		String str = null;
		if(tab == 0){
			str = "assert (" + rwo.getName() + assignment + ");";
		}
		else if (tab == 1) {
			str = "    assert (" + rwo.getName() + assignment + ");";
		}
		System.out.println(str);
	}
	
	public void verilogCon(int tab) {
		String str = null;
		if(tab == 0){
			str = "$display(" + rwo.getName() + " " + assignment + ");";
		}
		else if (tab == 1) {
			str = "$display(" + rwo.getName() + " " + verb + ");";
		}
		
		System.out.println(str);
	}

	public RWO getRWO() {
		return rwo;
	}

	public String getVerb() {
		return verb;
	}

	public String getAssignment() {
		return assignment;
	}
	
	public boolean isFullSignal() {
		if(rwo != null && verb != null && assignment != null) return true;
		else return false;
	}
}
