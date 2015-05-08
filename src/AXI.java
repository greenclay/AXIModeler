import java.util.ArrayList;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

public class AXI {

	// Check for Cons when Ante
	public static void parseAndPrint(Tree tree) {

		AntCon result = checkWhen(tree);
		if(result == null) {
			ArrayList<String> treeWords = getTreeWords(tree);
			result = new AntCon(treeWords, new ArrayList<String>());
		}
		checkAnd(result);
		result.printAnt();
		result.printCon();
		
		analyzeVerb(result.ant);
		analyzeVerb(result.ant2);
		analyzeVerb(result.con);
		analyzeVerb(result.con2);
	
		System.out.println();
		result.fillSignals();
		result.printSignals();
	
		result.makeVerilog();
		// result.makeSignals(phrase)
		// result.searchRWOTable();
		// result.searchTransactionTable();
		System.out.println("***********************************************************");
	}

	public static void analyzeVerb(ArrayList<String> phrase) {
		// Look for phrases like is LOW
		for (int i = 0; i < phrase.size()-1; i++) {
			if(phrase.get(i).equals("is")) {
				if(phrase.get(i+1).equals("LOW")) {
					phrase.set(i, "is LOW");
				}
				else if (phrase.get(i+1).equals("HIGH")) {
					phrase.set(i, "is HIGH");
				}
			}
			if(phrase.get(i).equals("are")) {
				if(phrase.get(i+1).equals("also") && phrase.get(i+2).equals("LOW")) {
					phrase.set(i, "are also LOW");
				}
			}
			
			// Check for cluster 3
			if(phrase.get(i).equals("remains")) {
				if(phrase.get(i+1).equals("asserted") && phrase.get(i+2).equals("until")) {
					phrase.set(i, "remains asserted until");
				}
			}
			// Check for cluster 5
			if(phrase.get(i).equals("is")) {
				if(phrase.get(i+1).equals("not") && phrase.get(i+2).equals("permitted")) {
					phrase.set(i, "is not permitted");
				}
			}
			// Check cluster 1
			if(phrase.get(i).equals("AWUSER")) {
				if(phrase.get(i+1).equals("WIDTH")){
					phrase.set(i, "AWUSER WIDTH");
				}
			}
			// cluster 1
			if(phrase.get(i).equals("be")) {
				if(phrase.get(i+1).equals("greater") && phrase.get(i+2).equals("than")){
					phrase.set(i, "be greater than");
				}
			}
		}
	}

	public static void checkAnd(AntCon antcon) {
		AntCon thisAntcon = antcon;
		if (checkAnd(antcon.getAnt())) {
			ArrayList<ArrayList<String>> split1 = splitOnAnd(antcon.getAnt());
			antcon.setAnt(split1.get(0));
			antcon.setAnt2(split1.get(1));
		}

		if (checkAnd(antcon.getCon())) {
			ArrayList<ArrayList<String>> split2 = splitOnAnd(antcon.getCon());
			antcon.setCon(split2.get(0));
			antcon.setCon2(split2.get(1));
		}
	}

	public static AntCon checkWhen(Tree tree) {
		// String[] anteCons = new String[2];
		AntCon antCon = null;
		ArrayList<String> antArr = new ArrayList<String>();
		ArrayList<String> conArr = new ArrayList<String>();

		if (doesTreeMatchPattern(tree, "SBAR << (WRB << when)")
				|| doesTreeMatchPattern(tree, "SBAR << (WRB << When)")) {
			if (doesTreeMatchPattern(tree, "SBAR << (RB << then)")
					|| doesTreeMatchPattern(tree, "SBAR << (RB << Then)")) {
				// Has form When Ante then Cons

				ArrayList<String> treeWords = getTreeWords(tree);
				int newLine = 0;
				for (int i = 0; i < treeWords.size(); i++) {
					String str = treeWords.get(i);

					if (str.toLowerCase().equals("then")) {
						newLine = i + 1;
						break;
					}
					antArr.add(str);
				}

				for (int i = newLine; i < treeWords.size(); i++) {
					String str = treeWords.get(i);
					conArr.add(str);
				}

				antCon = new AntCon(antArr, conArr);
				return antCon;
			}

			ArrayList<String> treeWords = getTreeWords(tree);

			int newLine = 0;
			for (int i = 0; i < treeWords.size(); i++) {
				String str = treeWords.get(i);

				if (str.toLowerCase().equals("when")) {
					newLine = i + 1;
					break;
				}
				conArr.add(str);
			}

			for (int i = newLine; i < treeWords.size(); i++) {
				String str = treeWords.get(i);

				antArr.add(str);
			}
			antCon = new AntCon(antArr, conArr);
		}
		return antCon;
	}

	public static ArrayList<ArrayList<String>> splitOnAnd(
			ArrayList<String> strArr) {
		int newLine = 0;
		ArrayList<String> before = new ArrayList<String>();

		ArrayList<String> after = new ArrayList<String>();
		for (int i = 0; i < strArr.size(); i++) {
			if (strArr.get(i).toLowerCase().equals("and")) {
				newLine = i + 1;
				break;
			}
			before.add(strArr.get(i));
		}
		for (int i = newLine; i < strArr.size(); i++) {
			after.add(strArr.get(i));
		}
		ArrayList<ArrayList<String>> returnArr = new ArrayList<ArrayList<String>>();
		returnArr.add(before);
		returnArr.add(after);
		return returnArr;
	}

	// Check if the string has an And in it
	public static boolean checkAnd(ArrayList<String> strArr) {
		for (String str : strArr) {
			if (str.equals("and")) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> getTreeWords(Tree tree) {
		ArrayList<String> treeWords = new ArrayList<String>();

		ArrayList<Label> treeYield = tree.yield();

		for (Label label : treeYield) {
			treeWords.add(label.toString());
		}

		return treeWords;
	}

	private static boolean doesTreeMatchPattern(Tree tree, String pattern) {
		TregexPattern patternMW = TregexPattern.compile(pattern);
		TregexMatcher matcher = patternMW.matcher(tree);
		Tree match = null;
		if (matcher.findNextMatchingNode()) {
			match = matcher.getMatch();
			return true;
		} else {
			return false;
		}
	}
}