import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.ScoredObject;


public class ModelParser {
	static private Sentence sent;
	public static InputOutput parse(ScoredObject<Tree> obj, Sentence paramSent) {

		sent = paramSent;
		Tree tree = obj.object();
		//signalParse(tree);
		if(findInput(tree) == true) {
			
			

			InputOutput io = null;
			
			if(sent.getSentenceString().startsWith("The first input is button A")) {
				List<Tree> matchingNodes = getAllNodesMatchingPattern(tree,"NN");
				io = new InputOutput(matchingNodes.get(0).yield().get(0).toString(), matchingNodes.get(1).yield().get(0).toString());
			}
			
			if(sent.getSentenceString().startsWith("The first input is button B")) {
				List<Tree> matchingNodes = getAllNodesMatchingPattern(tree,"NN");
				io = new InputOutput(matchingNodes.get(0).yield().get(0).toString(), matchingNodes.get(1).yield().get(0).toString());
			}
			
			if(sent.getSentenceString().startsWith("The output is a light")) {
				List<Tree> matchingNodes = getAllNodesMatchingPattern(tree,"NN");
				io = new InputOutput(matchingNodes.get(0).yield().get(0).toString(), matchingNodes.get(1).yield().get(0).toString());
			}
			
			if(sent.getSentenceString().startsWith("If A is pressed then the light turns on.") || sent.getSentenceString().startsWith("If the button is pressed then the light goes on.") ||  sent.getSentenceString().startsWith("If the switch goes on then the output is one")) {
				List<Tree> matchingNodes = getAllNodesMatchingPattern(tree,"NN");
				io = null;
			}
			//InputOutput io = new InputOutput(matchingNodes.get(1).yield().get(0).toString(),matchingNodes.get(1).yield().get(1).toString());
			return io;
		}
		return null;
	}
	
	// Find the word "input" in the sentence tree and return true.
	private static boolean findInput(Tree tree) {
		List<Tree> matchingNodes = getAllNodesMatchingPattern(tree, "NN");
		for(Tree tr : matchingNodes) {
			if(tr.yield().get(0).toString().toLowerCase().equals("input")) {
				return true;
			}
		}
		return false;	
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
	
	private static List<Tree> getAllNodesMatchingPattern(Tree tree, String pattern) {
		// Given the pattern return all the matching nodes in the tree.
		// Helper function
		TregexPattern patternMW = TregexPattern.compile(pattern); 
		TregexMatcher matcher = patternMW.matcher(tree);
		Tree match = null;
		List<Tree> matchedNodes = new ArrayList<Tree>();
		while(matcher.findNextMatchingNode()) {
			matchedNodes.add(matcher.getMatch());
		}
		return matchedNodes;
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
