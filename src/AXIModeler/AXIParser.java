package AXIModeler;

import java.util.ArrayList;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

public class AXIParser {

	public static String treeMatchPatternYield;
	private static int clusterNumber = 0;

	// Check for Cons when Ante
	public static void parseAndPrint(Tree tree) {

		Implication imp = checkWhen(tree);
		if( imp == null ) imp = checkAfter(tree);
		if( imp == null ) imp = checkWithinOf(tree); // for cluster 10
		if (imp == null) {
			ArrayList<String> treeWords = getTreeWords(tree);
			imp = new Implication(treeWords, new ArrayList<String>());
		}
		checkAnd(imp); // check for and in the clause and split sentences to two
						// clauses, before the "and" and after the "and"
		// imp.printAntArr();
		// imp.printConArr();

		imp.printImplication();
		OutputWriter.write();
		makeSignals(imp);

//		imp.printSignals();

		// analyzeVerb(imp.antArr1);
		// analyzeVerb(imp.antArr2);
		// analyzeVerb(imp.conArr1);
		// analyzeVerb(imp.conArr2);
		//
		// OutputWriter.write();
		// imp.fillSignals();
		// imp.printSignalsOld();
		//
		// imp.makeVerilogOld();
		imp.makeVerilog();
		OutputWriter.printVerilog(imp);
		// result.makeSignals(phrase)
		// result.searchRWOTable();
		// result.searchTransactionTable();
		OutputWriter.write("\n***********************************************************\n");
	}

	private static void makeSignals(Implication imp) {
		new SignalMaker(imp.getAnt1());
		if (imp.getAnt2() != null) new SignalMaker(imp.getAnt2());
		new SignalMaker(imp.getCon1());
		if (imp.getCon2() != null) new SignalMaker(imp.getCon2());
	}

	// change ArrayList<String> like "is, LOW" into "is LOW, LOW" for easier
	// string matching
	public static void analyzeVerb(ArrayList<String> phrase) {
		// Look for phrases like is LOW
		for (int i = 0; i < phrase.size() - 1; i++) {
			if (phrase.get(i).equals("is")) {
				if (phrase.get(i + 1).equals("LOW")) {
					phrase.set(i, "is LOW");
				} else if (phrase.get(i + 1).equals("HIGH")) {
					phrase.set(i, "is HIGH");
				}
			}
			if (phrase.get(i).equals("are")) {
				if (phrase.get(i + 1).equals("also") && phrase.get(i + 2).equals("LOW")) {
					phrase.set(i, "are also LOW");
				}
			}

			// Check for cluster 3
			if (phrase.get(i).equals("remains")) {
				if (phrase.get(i + 1).equals("asserted") && phrase.get(i + 2).equals("until")) {
					phrase.set(i, "remains asserted until");
				}
			}
			// Check for cluster 5
			if (phrase.get(i).equals("is")) {
				if (phrase.get(i + 1).equals("not") && phrase.get(i + 2).equals("permitted")) {
					phrase.set(i, "is not permitted");
				}
			}
			// Check cluster 1
			if (phrase.get(i).equals("AWUSER")) {
				if (phrase.get(i + 1).equals("WIDTH")) {
					phrase.set(i, "AWUSER WIDTH");
				}
			}
			// cluster 1
			if (phrase.get(i).equals("be")) {
				if (phrase.get(i + 1).equals("greater") && phrase.get(i + 2).equals("than")) {
					phrase.set(i, "be greater than");
				}
			}

			// cluster 11) CSYSREQ is only permitted to change from HIGH to LOW
			// when CSYSACK is HIGH. [4]
			if (phrase.get(i).equals("change")) {
				if (phrase.get(i + 1).equals("from") && phrase.get(i + 2).equals("HIGH") && phrase.get(i + 3).equals("to") && phrase.get(i + 4).equals("LOW")) {
					phrase.set(i, "change from HIGH to LOW");
				}
			}
		}
	}

	public static void checkAnd(Implication imp) {
		Implication thisAntcon = imp;
		if (checkAnd(imp.getAnt())) {
			ArrayList<ArrayList<String>> split1 = splitOnAndOrThen(imp.getAnt());
			imp.setAntArr(split1.get(0));
			imp.setAntArr2(split1.get(1));
			imp.getAnt1().setWords(split1.get(0));
			imp.getAnt2().setWords(split1.get(1));
		}

		if (checkAnd(imp.getCon())) {
			ArrayList<ArrayList<String>> split2 = splitOnAndOrThen(imp.getCon());
			imp.setConArr(split2.get(0));
			imp.setConArr2(split2.get(1));
			imp.getCon1().setWords(split2.get(0));
			imp.getCon2().setWords(split2.get(1));
		}
	}

	// cluster 6
	// AWVALID is LOW for the first cycle after ARESETn goes HIGH.
	private static Implication checkAfter(Tree tree) {
		Implication antCon = null;
		ArrayList<String> antArr = new ArrayList<String>();
		ArrayList<String> conArr = new ArrayList<String>();

		if (doesTreeMatchPattern(tree, "SBAR << after")) {
			ArrayList<String> treeWords = getTreeWords(tree);
			int newLine = 0;
			for (int i = 0; i < treeWords.size(); i++) {
				String str = treeWords.get(i);

				if (str.toLowerCase().equals("after")) {
					newLine = i + 1;
					break;
				}
				conArr.add(str);
			}

			for (int i = newLine; i < treeWords.size(); i++) {
				String str = treeWords.get(i);
				antArr.add(str);
			}

			antCon = new Implication(antArr, conArr);
			return antCon;
		}
		return antCon;
	}

	private static Implication checkWhen(Tree tree) {
		// String[] anteCons = new String[2];
		Implication antCon = null;
		ArrayList<String> antArr = new ArrayList<String>();
		ArrayList<String> conArr = new ArrayList<String>();

		if (doesTreeMatchPattern(tree, "SBAR << (WRB << when)") || doesTreeMatchPattern(tree, "SBAR << (WRB << When)")) {
			if (doesTreeMatchPattern(tree, "SBAR << (IN << if)") || doesTreeMatchPattern(tree, "SBAR << (IN << If)")) {
				// Has form When Ante then Cons

				ArrayList<String> treeWords = getTreeWords(tree);
				int newLine = 0;
				for (int i = 0; i < treeWords.size(); i++) {
					String str = treeWords.get(i);

					if (str.toLowerCase().equals("if")) {
						newLine = i + 1;
						break;
					}
					antArr.add(str);
				}

				for (int i = newLine; i < treeWords.size(); i++) {
					String str = treeWords.get(i);
					conArr.add(str);
				}
				
				antCon = new Implication(antArr, conArr);
				return antCon;
			}
			else if (doesTreeMatchPattern(tree, "SBAR << (RB << then)") || doesTreeMatchPattern(tree, "SBAR << (RB << Then)")) {
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

				antCon = new Implication(antArr, conArr);
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
			antCon = new Implication(antArr, conArr);
		}
		return antCon;
	}

	// cluster 10
	//SENTENCE - Recommended that AWREADY is asserted within MAXWAITS cycles of AWVALID being asserted.

	private static Implication checkWithinOf(Tree tree) {
		Implication antCon = null;
		ArrayList<String> antArr = new ArrayList<String>();
		ArrayList<String> conArr = new ArrayList<String>();

		if (doesTreeMatchPattern(tree, "(IN < within) .. (PP < (IN < of))")) {
			ArrayList<String> treeWords = getTreeWords(tree);
			int newLine = 0;
			for (int i = 0; i < treeWords.size(); i++) {
				String str = treeWords.get(i);

				if (str.toLowerCase().equals("of")) {
					newLine = i + 1;
					break;
				}
				conArr.add(str);
			}

			for (int i = newLine; i < treeWords.size(); i++) {
				String str = treeWords.get(i);
				antArr.add(str);
			}

			antCon = new Implication(antArr, conArr);
			return antCon;
		}
		return antCon;
	}
	
	
	// cluster 9 the "then" in "ARCACHE1 is LOW then ARCACHE32 must also be LOW."
	public static ArrayList<ArrayList<String>> splitOnAndOrThen(ArrayList<String> strArr) {
		int newLine = 0;
		ArrayList<String> before = new ArrayList<String>();

		ArrayList<String> after = new ArrayList<String>();
		for (int i = 0; i < strArr.size(); i++) {
			if (strArr.get(i).toLowerCase().equals("and") || strArr.get(i).toLowerCase().equals("then")) {
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
			if (str.equals("and") || str.equals("then")) {
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

	public static boolean doesTreeMatchPattern(Tree tree, String pattern) {
		TregexPattern patternMW = TregexPattern.compile(pattern);
		TregexMatcher matcher = patternMW.matcher(tree);
		Tree match = null;
		if (matcher.findNextMatchingNode()) {
			match = matcher.getMatch();
			String treeMatchPatternYield = match.yield().get(0).toString();
			return true;
		} else {
			return false;
		}
	}

	public static String getTreeMatchPatternYield(Tree tree, String pattern) {
		TregexPattern patternMW = TregexPattern.compile(pattern);
		TregexMatcher matcher = patternMW.matcher(tree);
		Tree match = null;
		if (matcher.findNextMatchingNode()) {
			match = matcher.getMatch();
			String treeMatchPatternYield = match.yield().get(0).toString();
			return treeMatchPatternYield;
		} else {
			return null;
		}
	}
}