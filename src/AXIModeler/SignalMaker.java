package AXIModeler;
import java.util.ArrayList;

import AXIModeler.Implication.Clause;
import edu.stanford.nlp.trees.Tree;

public class SignalMaker {
	private Clause clause;
	private Tree tree;
	private ArrayList<String> words;
	private String verb;
	
	public SignalMaker(Clause clause) {
		this.clause = clause;
		this.tree = clause.getTree();
		words = clause.getWords();
		makeSignal();
	}
	
	public void makeSignal() {
		RWO rwo = findRWO();
//		String verb = findVerb();
		findVerb();
		
		String assignment = TransactionTable.searchTrans(verb);
		
//		System.out.println(rwo + " - " + verb + " - " + assignment);
		
		Signal signal = new Signal(rwo, verb, assignment);	
		
//		clause.setSignal(signal);
		
		if(rwo != null && verb != null && assignment != null) {
			signal = new Signal(rwo, verb, assignment);	
			clause.setSignal(signal);
		}
		if (rwo != null && rwo.getName().equals("AWVALID")) {
			System.err.println(verb);
		}
	}
	
	private void findVerb() {
		findIsHigh();
		findIsLow();
		findIsAsserted();
		findRemainsStable();
		findIsNotPermitted();
		findRemainsAsserted();
	}
	
	private void findRemainsAsserted() {
		// Matches "remains asserted" of "it remains asserted until AWREADY is HIGH"
		// Cluster 3 When AWVALID is asserted then it remains asserted until AWREADY is HIGH.

		if(AXI.doesTreeMatchPattern(tree, "(VBZ < remains) .. (VBD < asserted)")) {
			verb = "remains asserted";
			return;
		}
	}
	
	private void findIsHigh() {
		// Matches the "is HIGH" of these sentences
		// value of X on RDATA valid byte lanes is not permitted when RVALID is HIGH.
		// when AWVALID is asserted then it remains asserted until AWREADY is HIGH.
		// A value of X on AWID is not permitted when AWVALID is HIGH.
		// CSYSREQ is only permitted to change from HIGH to LOW when CSYSACK is HIGH.
		if(AXI.doesTreeMatchPattern(tree, "ADJP $ (VBZ < is)")) {
			String matchYield = AXI.getTreeMatchPatternYield(tree, "ADJP $ (VBZ < is)");
			if(matchYield.equals("HIGH")) {
				verb = "is HIGH";
				return;
			}
		}
	}
	
	private void findIsNotPermitted() {
		// Matches the "is asserted" of these sentences
		// cluster5 A value of X on RDATA valid byte lanes is not permitted when RVALID is HIGH.
		if(AXI.doesTreeMatchPattern(tree, "VP $ ((RB < not) $ (VBZ < is))")) {
			String matchYield = AXI.getTreeMatchPatternYield(tree, "VP $ ((RB < not) $ (VBZ < is))");
			if(matchYield.equals("permitted")) {
				verb = "is not permitted";
				return;
			}
		}
	}
	
	private void findIsAsserted() {
		// Matches the "is asserted" of these sentences
		// AWADDR remains stable when AWVALID is asserted and AWREADY is LOW.
		if(AXI.doesTreeMatchPattern(tree, "(VP << asserted) $ (VBZ < is)")) {
			verb = "asserted";
			return;
		}
//		if(AXI.doesTreeMatchPattern(tree, "VP $ (VBZ < is)")) {
//			String matchYield = AXI.getTreeMatchPatternYield(tree, "VP $ (VBZ < is)");
//			if(matchYield.equals("asserted")) {
//				verb = "asserted";
//				return;
//			}
//		}
	}
	
	private void findRemainsStable() {
		// Matches the "remains stable" of these sentences
		// AWADDR remains stable when AWVALID is asserted and AWREADY is LOW.
		if(AXI.doesTreeMatchPattern(tree, "ADJP $ (VBZ < remains)")) {
			String matchYield = AXI.getTreeMatchPatternYield(tree, "ADJP $ (VBZ < remains)");
			if(matchYield.equals("stable")) {
				verb = "stable";
				return;
			}
		}
	}
	
	private void findIsLow() {
		// Matches the "is LOW" of these sentences
		// AWADDR remains stable when AWVALID is asserted and AWREADY is LOW.
		// when AWVALID is HIGH and AWCACHE1 is LOW then AWCACHE32 are also LOW.
		String pattern = "ADVP $ (VBZ < is)";
		if(AXI.doesTreeMatchPattern(tree, pattern)) {
			String matchYield = AXI.getTreeMatchPatternYield(tree, pattern);
			if(matchYield.equals("LOW")) {
				verb = "is LOW";
				return;
			}
		}
		
		pattern = "(ADVP << LOW) $ (VBP < are)";
		if(AXI.doesTreeMatchPattern(tree, pattern)) {
			String matchYield = AXI.getTreeMatchPatternYield(tree, pattern);
			if(matchYield.equals("LOW")) {
				verb = "is LOW";
				return;
			}
		}
		
	}
	
	private RWO findRWO() {
		for (String str : words) {
			RWO rwo = RWOTable.searchInput(str);
//			System.out.println(str + " " + rwo);
			if (rwo != null) return rwo;
		}
		for (String str : words) {
			RWO rwo = RWOTable.searchOutput(str);
//			System.out.println(str + " " + rwo);
			if (rwo != null) return rwo;
		}
		return null;
	}
	
	public Signal makeAntSignals() {

		Signal signal = null;
//		System.out.println(phrase);
		
		// Look for an RWO in the table that matches the phrase
		for (String str : words) {
			RWO rwo = RWOTable.searchInput(str);
//			System.out.println(str + " " + rwo);
			
			// If matching rwo was found in the rwotable
			if (rwo != null) {
				for (String str2 : words) {
					String assignment = TransactionTable.searchTrans(str2);
//					System.out.println(str2 + " " + assignment);
						if (assignment != null) {
							signal = new Signal(rwo, str2, assignment);
							break;
						}
				}
			}
		}		
		return signal;
	}
}
