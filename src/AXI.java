import java.util.ArrayList;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

public class AXI {

	// Check for Cons when Ante
	public static void parseAndPrint(Tree tree) {
		System.out.println();
		AntCon result = checkWhen(tree);
		
		result.printAnt();
		result.printCon();
		System.out.println("----");
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

	public ArrayList<ArrayList<String>> splitOnAnd(ArrayList<String> strArr) {

		
		return null;
	}

	// Check if the string has an And in it
	public boolean checkAnd(ArrayList<String> strArr) {
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