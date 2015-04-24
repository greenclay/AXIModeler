import java.io.FileWriter;
import java.io.IOException;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.ScoredObject;


public class SignalParser {
	static private Sentence sent;
	public static void parse(ScoredObject<Tree> obj, Sentence paramSent) {

		sent = paramSent;
		Tree tree = obj.object();
		signalParse(tree);
	}
	
	private static Tree findTreeMatchingPattern(Tree tree, String pattern) {
		// Given the pattern return the first match
		// Helper function
		TregexPattern patternMW = TregexPattern.compile(pattern); 
		TregexMatcher matcher = patternMW.matcher(tree);
		Tree match = null;
		if(matcher.findNextMatchingNode()) {
			match = matcher.getMatch();
		}
		return match;
	}
	
	private static boolean doesTreeMatchPattern(Tree tree, String pattern, String yield) {
		// If the Tree matches the pattern and the word equals yield than return true
		// Helper function
		TregexPattern patternMW = TregexPattern.compile(pattern); 
		TregexMatcher matcher = patternMW.matcher(tree);
		Tree match = null;
		if(matcher.findNextMatchingNode()) {
			match = matcher.getMatch();
		}
		if(match.yield().get(0).toString().toLowerCase().equals(yield)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static void signalParse(Tree tree) {

			Tree match = findTreeMatchingPattern(tree, "(VBN >> VP) > VP");
			if( match == null) return;
			if( match.yield().get(0).toString().equals("set")) {
				// Parse the sentence "Signal is set to 1"
				Tree match2 = findTreeMatchingPattern(tree, "NNP > NP");
				
				if(match2.yield().get(0).toString().toLowerCase().equals("signal")) {
					if(doesTreeMatchPattern(tree,"CD > (NP > PP)", "1")) {
						writeToFile("ObjectPropertyAssertion(SignalOntology:Equals SignalOntology:x SignalOntology:One)");
					}
				}
			}
			else if ( match.yield().get(0).toString().equals("asserted") ) {
				
			}
			else if (match.yield().get(0).toString().equals("assigned")) {
				
			}		
	}
	
	private static void writeToFile(String str) {
		try {
			FileWriter out = new FileWriter("Signal.owl",true);
			out.write(str + '\n' + '\n');
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
