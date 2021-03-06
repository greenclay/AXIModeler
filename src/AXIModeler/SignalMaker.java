package AXIModeler;
import java.util.ArrayList;

import edu.stanford.nlp.trees.Tree;

public class SignalMaker {
	private Clause clause;
	private Tree tree;
	private ArrayList<String> words;
	private String verb;
	private String valueOf; // holds value of like "X" of signal ie "AWADDR has value of X"
	
	public SignalMaker(Clause clause) {
		this.clause = clause;
		this.tree = clause.getTree();
		words = clause.getWords();
		clause.setValueOf(valueOf);
		makeSignal();
	}
	
	public void makeSignal() {
		RWO rwo = findRWO();
//		String verb = findVerb();
		findVerb();
		
		String assignment = TransactionTable.searchTrans(verb);
		
//		OutputWriter.write(rwo + " - " + verb + " - " + assignment);
		
		Signal signal = new Signal(rwo, verb, assignment);	
		
//		clause.setSignal(signal);
		
		if(rwo != null && verb != null && assignment != null) {
			signal = new Signal(rwo, verb, assignment);	
			clause.setSignal(signal);
		}
	}
	
	private void findVerb() {
		findIsHigh();
		findIsLow();
		findIsAsserted();
		findRemainsStable(); // cluster 8
		findIsNotPermitted();
		findRemainsAsserted();
		findGreaterThanOrEqualOne(); // cluster 1
		findNotInReset(); // cluster 4
		findChangeFromHighToLow(); // cluster 11
		findGoesHigh(); // cluster 6
		findAssertedWithin(); // cluster 10
		
		findValueOf();
	}

	private void findGreaterThanOrEqualOne() {
		// Cluster 1 
		// Matches "be greater than or equal to one" of "Parameter AWUSER_WIDTH must be greater than or equal to one . "

		if(AXIParser.doesTreeMatchPattern(tree, "(VB < be) .. (ADJP << greater) .. (ADJP << than) .. (ADJP << equal) .. (TO << to) .. (NP << one)")) {
			verb = "be greater than or equal to one";
			return;
		}
	}
	
	private void findRemainsAsserted() {
		// Matches "remains asserted" of "it remains asserted until AWREADY is HIGH"
		// Cluster 3 When AWVALID is asserted then it remains asserted until AWREADY is HIGH.

		if(AXIParser.doesTreeMatchPattern(tree, "(VBZ < remains) .. (VBD < asserted)")) {
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
		if(AXIParser.doesTreeMatchPattern(tree, "ADJP $ (VBZ < is)")) {
			String matchYield = AXIParser.getTreeMatchPatternYield(tree, "ADJP $ (VBZ < is)");
			if(matchYield.equals("HIGH")) {
				verb = "is HIGH";
				return;
			}
		}
	}
	
	// cluster 4 SENTENCE - A value of X on AWVALID is not permitted when not in reset.
	// "not in reset"
	private void findNotInReset() {
		if(AXIParser.doesTreeMatchPattern(tree, "(RB < not) .. (NP << reset)")) {
			verb = "not in reset";
			return;
		}
	}

	// cluster 6
	// goes HIGH
	private void findGoesHigh() {
		if(AXIParser.doesTreeMatchPattern(tree, "(VBZ < goes) $ (NP << HIGH)")) {
			verb = "goes HIGH";
			return;
		}
	}
	
	private void findIsNotPermitted() {
		// Matches the "is asserted" of these sentences
		// cluster5 A value of X on RDATA valid byte lanes is not permitted when RVALID is HIGH.
		if(AXIParser.doesTreeMatchPattern(tree, "VP $ ((RB < not) $ (VBZ < is))")) {
			String matchYield = AXIParser.getTreeMatchPatternYield(tree, "VP $ ((RB < not) $ (VBZ < is))");
			if(matchYield.equals("permitted")) {
				verb = "is not permitted";
				return;
			}
		}
	}
	
	
	private void findIsAsserted() {
		// Matches the "is asserted" of these sentences
		// AWADDR remains stable when AWVALID is asserted and AWREADY is LOW.

		// detect "being asserted"
		// cluster 10 - Recommended that AWREADY is asserted within MAXWAITS cycles of AWVALID being asserted. 
		
		String pattern1 = "(VP << asserted) $ (VBZ < is)";
		String pattern2 = "(VP << asserted) $ (VBG < being)";
		if(AXIParser.doesTreeMatchPattern(tree, pattern1) || AXIParser.doesTreeMatchPattern(tree, pattern2)) {
			verb = "is asserted";
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
		// AWID must remain stable when AWVALID is asserted and AWREADY is LOW. cluster 8
		if(AXIParser.doesTreeMatchPattern(tree, "ADJP $ (VBZ < remains)")) {
			String matchYield = AXIParser.getTreeMatchPatternYield(tree, "ADJP $ (VBZ < remains)");
			if(matchYield.equals("stable")) {
				verb = "stable";
				return;
			}
		} else if (AXIParser.doesTreeMatchPattern(tree, "(ADJP << stable) $ (VB < remain)")) {
			verb = "stable";
			return;
		}
	}

	private void isLowFirstCycle() {
		String pattern = "ADVP $ (VBZ < is) .. (NP << first) .. (NP << cycle)";
		if(AXIParser.doesTreeMatchPattern(tree, pattern)) {
			verb = "is LOW for the first cycle";
			return;
		}
	}
	
	private void findIsLow() {
		// Matches the "is LOW" of these sentences
		// AWADDR remains stable when AWVALID is asserted and AWREADY is LOW.
		// when AWVALID is HIGH and AWCACHE1 is LOW then AWCACHE32 are also LOW.
		String pattern = "ADVP $ (VBZ < is)";
		if(AXIParser.doesTreeMatchPattern(tree, pattern)) {
			String matchYield = AXIParser.getTreeMatchPatternYield(tree, pattern);
			if(matchYield.equals("LOW")) {
				verb = "is LOW";
				isLowFirstCycle(); // exception for "AWVALID is LOW for the first cycle after ARESETn goes HIGH." 
									// checks for "for the first cycle"
				return;
			}
		}
		
		pattern = "(ADVP << LOW) $ (VBP < are)";
		if(AXIParser.doesTreeMatchPattern(tree, pattern)) {
			String matchYield = AXIParser.getTreeMatchPatternYield(tree, pattern);
			if(matchYield.equals("LOW")) {
				verb = "is LOW";
				return;
			}
		}
		
		pattern = "(ADVP << LOW) $ (VB < be)";
		if(AXIParser.doesTreeMatchPattern(tree, pattern)) {
			verb = "is LOW";
			return;
		}
	}
	
	// cluster 10
	// Recommended that AWREADY is asserted within MAXWAITS cycles of AWVALID being asserted.
	// Detect asserted within
	private void findAssertedWithin() {
		// "(VP << asserted)"
		if(AXIParser.doesTreeMatchPattern(tree, "(VP << asserted) << within")) {
			verb = "asserted within";
			return;
		}
	}
	
	// Cluster 11
	// SENTENCE - CSYSREQ is only permitted to change from HIGH to LOW when CSYSACK is HIGH.
	// Parses " change from HIGH to LOW"
	private void findChangeFromHighToLow() {
		if(AXIParser.doesTreeMatchPattern(tree, "(VB < change) .. (NP << HIGH) .. (NP << LOW)")) {
			verb = "change from HIGH to LOW";
		}
	}
	
	private void findValueOf() {
		if(AXIParser.doesTreeMatchPattern(tree, "(NP << value) $ (PP << of)")) {
			String pattern = "NP [< NNP | <NNS] & > NP & >> (PP << of & $ (NP << value))";
			String matchYield = AXIParser.getTreeMatchPatternYield(tree, pattern);
			valueOf = matchYield;
			OutputWriter.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~VALUE OF " + valueOf);
		}
	}
	private RWO findRWO() {
		for (String str : words) {
			RWO rwo = RWOTable.searchInput(str);
//			OutputWriter.write(str + " " + rwo);
			if (rwo != null) return rwo;
		}
		for (String str : words) {
			RWO rwo = RWOTable.searchOutput(str);
//			OutputWriter.write(str + " " + rwo);
			if (rwo != null) return rwo;
		}
		return null;
	}
	
	public Signal makeAntSignals() {

		Signal signal = null;
//		OutputWriter.write(phrase);
		
		// Look for an RWO in the table that matches the phrase
		for (String str : words) {
			RWO rwo = RWOTable.searchInput(str);
//			OutputWriter.write(str + " " + rwo);
			
			// If matching rwo was found in the rwotable
			if (rwo != null) {
				for (String str2 : words) {
					String assignment = TransactionTable.searchTrans(str2);
//					OutputWriter.write(str2 + " " + assignment);
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
