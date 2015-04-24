import java.util.ArrayList;

public class AntCon {

	ArrayList<String> ant;
	ArrayList<String> con;

	public AntCon(ArrayList<String> ant, ArrayList<String> con) {
		this.ant = ant;
		this.con = con;
	}

	public ArrayList<String> getAnt() {
		return ant;
	}

	public ArrayList<String> getCon() {
		return con;
	}
	
	public void printAnt() {
		String antStr = "Antecedent: ";
		for(String str : ant) {
			antStr = antStr + str + " ";
		}
		System.out.println(antStr);
	}
	
	public void printCon() {
		String conStr = "Consequent: ";
		for(String str : con) {
			conStr = conStr + str + " ";
		}
		System.out.println(conStr);
	}
}
