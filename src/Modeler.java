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

public class Modeler {
	private static ArrayList<Sentence> sentenceList;
	private static ArrayList<InputOutput> ioList;

	public static void main(String[] args) {
		LexicalizedParser lp = LexicalizedParser
				.loadModel("C:\\c\\englishPCFG.caseless.ser.gz");

		ioList = new ArrayList<InputOutput>();
		RWOTable.init();
		sentenceList = new ArrayList<Sentence>();
		readFile("axi.txt");
		parseSentences(lp);

		// ioList.add(new InputOutput("light", "A"));
		// for (InputOutput io : ioList) {
		// io.print();
		// }

		System.out.println("Antecedent: A, pressed");
		System.out.println("Consequent: A, turns on");
	}

	private static void parseSentences(LexicalizedParser lp) {

		int numOfParses = 5;
		int parseType = 10;
		LexicalizedParserQuery lpq = lp.lexicalizedParserQuery();
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		List<? extends HasWord> sent;
		Tokenizer<? extends HasWord> toke;
		Sentence gottenSentence;

		for (int i = 0; i < sentenceList.size(); i++) {
			gottenSentence = sentenceList.get(i);

			// If the sentence starts with "#" then ignore it as it is a comment
			if (gottenSentence.getSentenceString().startsWith("#")) {
				continue;
			}

			toke = tlp.getTokenizerFactory().getTokenizer(
					new StringReader(gottenSentence.sent));
			sent = toke.tokenize();
			lpq.parse(sent);
			gottenSentence.kBest = lpq.getKBestPCFGParses(numOfParses);
			gottenSentence.numParses = numOfParses;

			int kBestNum = 0;

			System.out.println("SENTENCE - "
					+ gottenSentence.getSentenceString());
			System.out.println(gottenSentence.kBest.get(0).object());
			Tree tree = gottenSentence.kBest.get(0).object();
			// /////////////////////////////////////// FOPC PARSER SPECIAL
			// CLASS:
//			InputOutput io = ModelParser.parse(gottenSentence.kBest.get(0),
//					gottenSentence);
//			if (io != null) {
//				ioList.add(io);
//			}

			// Start AXI Parsing
			
			AXI.parseAndPrint(tree);
			System.out.println();
		}

	}

	private static void readFile(String s) {
		int n = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(s))) {
			String line = br.readLine();
			while (line != null) {

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
