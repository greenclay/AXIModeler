package AXIModeler;


import java.util.List;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.ScoredObject;

public class Sentence {

	public String sent;
	public boolean isQuestion = false;
	public boolean isSoft = false;

	
	public List<ScoredObject<Tree>> kBest;
	public boolean detectAsQ = false;
	public boolean detectAsSoft = false;

	public String tags = "";
	public int detectionCount = 0;
	public int sentenceNumber = 0;
	
	public int numParses = 0;
	public boolean softCommand = false;
	public boolean hardCommand = false;
	
	public String softVerb = "";
	public String softNoun = "";
	public String hardVerb = "";
	public String hardNoun = "";
	
	public int detectedKBest = -1;
	
	public String anaphoraResolution;
	 
	public Sentence(String s, int n) {
		sent = s;
		sentenceNumber = n;
	}

	public String getSentenceString() {		return sent;	}

}
