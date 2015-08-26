package AXIModeler;

import java.util.ArrayList;

import edu.stanford.nlp.trees.Tree;

public class Clause {

	public ArrayList<String> words;
	public Signal signal;
	public Tree tree;
	private String valueOf;
	
	public Clause(ArrayList<String> words) {
		this.words = words;
		findTree();
	}

	public void setSignal(Signal signal) {
		this.signal = signal;
	}

	public ArrayList<String> getWords() {
		return words;
	}

	public Signal getSignal() {
		return signal;
	}

	public void setWords(ArrayList<String> words) {
		this.words = words;
		findTree();
	}

	private void findTree() {
		String sentence = "";
		for (String str : words) {
			if (str.equals(".")) continue;
			if (str.equals("when")) continue;
			if (str.equals("When")) continue;
			sentence = sentence + " " + str;
		}
		sentence = sentence + ".";

		// if (sentence.equals(".")) sentence = "";
		tree = AXIModeler.getTree(sentence);
	}

	public Tree getTree() {
		return tree;
	}
	
	public void setValueOf(String str) {
		valueOf = str;
	}
}