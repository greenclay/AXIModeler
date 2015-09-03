package AXIModeler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class AXIModeler {
	private static ArrayList<Sentence> sentenceList;
	private static LexicalizedParserQuery lpq;
	private static TreebankLanguagePack tlp;
	private static String englishPCFGModelPath = "lib/englishPCFG.ser.gz";
	
	public static void main(String[] args) {
		mainMethod();
	}
	
	private static void testMethod(String inputFilename) {
		LexicalizedParser lp = LexicalizedParser.loadModel(englishPCFGModelPath);
		
		RWOTable.init();
		sentenceList = new ArrayList<Sentence>();
		
		String testinput1 = "axi-test-input.txt";
		
		readFile(testinput1); // THE INPUT FILE
		parseSentences(lp);
		
	}
	
	private static void mainMethod() {
//		OutputWriter.init();
		
		long startTime = System.nanoTime();

		LexicalizedParser lp = LexicalizedParser.loadModel(englishPCFGModelPath);

		RWOTable.init();
		sentenceList = new ArrayList<Sentence>();
		
		String fileall81 = "axi-all81.txt";
		String file11 = "axi-11clusters.txt";
		readFile(file11); // THE INPUT FILE
		parseSentences(lp);
		
		long endTime = System.nanoTime();

		double duration = (endTime - startTime) / (double) 1000000000;  //divide by 1000000 to get milliseconds.
//		System.err.println("Execution took : " + duration + " seconds");
//		OutputWriter.write("Execution took : " + duration + " seconds");
		
		OutputWriter.printAll();
	}
	
	private static void parseSentences(LexicalizedParser lp) {

		int numOfParses = 1;
		lpq = lp.lexicalizedParserQuery();
		tlp = new PennTreebankLanguagePack();
		List<? extends HasWord> sent;
		Tokenizer<? extends HasWord> toke;
		Sentence gottenSentence;
		for (int i = 0; i < sentenceList.size(); i++) {
			gottenSentence = sentenceList.get(i);


			toke = tlp.getTokenizerFactory().getTokenizer(new StringReader(gottenSentence.sent));
			sent = toke.tokenize();
			lpq.parse(sent);
			gottenSentence.kBest = lpq.getKBestPCFGParses(numOfParses);
			gottenSentence.numParses = numOfParses;

			int kBestNum = 0;
			OutputWriter.write("Sentence number " + (i+1));
			
			OutputWriter.write("SENTENCE - " + gottenSentence.getSentenceString() + "\n");
			OutputWriter.write(gottenSentence.kBest.get(0).object() + "\n");
			Tree tree = gottenSentence.kBest.get(0).object();
			// /////////////////////////////////////// FOPC PARSER SPECIAL
			// CLASS:
			// InputOutput io = ModelParser.parse(gottenSentence.kBest.get(0),
			// gottenSentence);
			// if (io != null) {
			// ioList.add(io);
			// }

			// Start AXI Parsing
			RWOTable.init();
			TransactionTable.init();
			AXIParser.parseAndPrint(tree);
		}

	}

	public static Tree getTree(String sentence) {
		Tokenizer<? extends HasWord> toke = tlp.getTokenizerFactory().getTokenizer(new StringReader(sentence));
		lpq.parse(toke.tokenize());
		Tree tree = lpq.getKBestPCFGParses(1).get(0).object();
		return tree;
	}

	private static void readFile(String s) {
		int n = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(s))) {
			String line = br.readLine();
			while (line != null) {
				
				// If the sentence starts with "#" then ignore it as it is a comment
				if (line.startsWith("#")) {
					line = br.readLine();
					continue;
				}

				
				if (!line.equals("")) {
					sentenceList.add(new Sentence(line, n));
					n++;
				}

				line = br.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
