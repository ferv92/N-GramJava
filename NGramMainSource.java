package Homework5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class NGramMainSource {
	
	public static void main (String [] args) throws FileNotFoundException {
		
		Scanner scan = new Scanner(System.in);
		
		String sentence = "";
		String word ="";
		String input;
		String inputCopy;
		String wordToCompare;
		String subString;
		int count = 0;
		double count2 = 0.0;
		int space;
		int nGramLength;
		int minNGramFreq;
		double frequency;
		int trackABList = 0;
		
		WordFreq sentenceToTry;
		WordFreq sentenceInTree;
		WordFreq lastSentenceToTry;
		WordFreq lastSentenceInTree;
		
		BinarySearchTree <WordFreq> tree = new BinarySearchTree<WordFreq>();
		BinarySearchTree <WordFreq> secondTree = new BinarySearchTree<WordFreq>();
		ABList <String> ABList = new ABList<String>(500000);
		ABList <String> ABList2 = new ABList<String>();
		
		//TheAdventuresOfSherlockHolmes.txt
		//Shakespeare18thSonnet.txt
		File txtFile = new File("./Homework5/TheAdventuresOfSherlockHolmes.txt");
		
		Scanner wordsIn = new Scanner(new FileReader(txtFile));
		wordsIn.useDelimiter("[^a-zA-Z']+");
		
		System.out.print("n-gram length> ");
		nGramLength = scan.nextInt();
		System.out.print("Minimum n-gram frequency> ");
		minNGramFreq = scan.nextInt();
		
		while(wordsIn.hasNext()) {
			word = wordsIn.next();
			word = word.toLowerCase();
			word.trim();
			if(!word.contentEquals("'")) {
				ABList.add(word);
			}
		}

		while(ABList.size() != trackABList) {
			for(int i = 0; i < nGramLength; i++) {
				sentence += ABList.get(trackABList);
				trackABList++;
				if(i != nGramLength) {
					sentence += " ";
				}
			}
			sentenceToTry = new WordFreq(sentence);
			sentenceInTree = tree.get(sentenceToTry);
			if(sentenceInTree != null) {
				sentenceInTree.inc();
			} else {
				sentenceToTry.inc();
				tree.add(sentenceToTry);
			}
			
			if((ABList.size() - (trackABList + 1)) == nGramLength) {
				break;
			}

			trackABList = trackABList - nGramLength + 1;
			sentence = "";
		}
	
		System.out.println("\nFreq\t4-gram");
		System.out.println("-----  -----------------");

		for(WordFreq sentenceFromTree : tree) {
			if(sentenceFromTree.getFreq() >= minNGramFreq)
				System.out.println(sentenceFromTree);
		}
		
		System.out.print("\nEnter " + nGramLength + " words (X to STOP): ");
		input = scan.next();
		input += scan.nextLine();
		inputCopy = input;
		inputCopy = inputCopy + " ";
		
		if(input.equalsIgnoreCase("x")){
			System.exit(0);
		}

		while(input.contains(" ")) {
			space = input.indexOf(" ");
			subString = input.substring(0, space);
			input = input.substring(space + 1, input.length());
			subString.strip();
			ABList2.add(subString);
		}
		input.strip();
		ABList2.add(input);

		trackABList = 0;
		
		if(ABList2.size() == nGramLength) {
			String sentenceWithWord;
			while(ABList.size() != trackABList) {
				wordToCompare = ABList.get(trackABList);			
				for(int i = 0; i < nGramLength; i++) {
					trackABList++;
					if(wordToCompare.equals(ABList2.get(i))) {
						count++;
						wordToCompare = ABList.get(trackABList);
						if(count == nGramLength) {
							count2++;
							sentenceWithWord = inputCopy + wordToCompare;
							lastSentenceToTry = new WordFreq(sentenceWithWord);
							lastSentenceInTree = secondTree.get(lastSentenceToTry);
							if(lastSentenceInTree != null) {
								lastSentenceInTree.inc();
							} else {
								lastSentenceToTry.inc();
								secondTree.add(lastSentenceToTry);
							}
						}
					}
					else {
						count = 0;
						break;
					}
				}
			}
		
			for(WordFreq print : secondTree) {
				frequency = ((double)print.getFreq() / count2) * 100;
				System.out.printf(print + "\t %.2f", frequency);
				System.out.println("%");
			}	
		}
		else {
			System.out.println("Error, you entered a longer a shorter sentence");
		}
		
		scan.close();

	}

}
