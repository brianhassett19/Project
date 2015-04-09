package nb_classifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class BagOfWordsTest {
	public static void main(String[] args) throws FileNotFoundException, IOException {

		String file = "C:\\Users\\Brian\\Desktop\\bowExample.txt";

		BagOfWords bow = new BagOfWords(file);

		// Print dictionary:
		System.out.println("Dictionary:");
		System.out.println(bow.dictionary); // Print the dictionary.

		// Print term frequency matrix:
		System.out.println("\nDocument vectors (size " + bow.dictionary.size() + "):");
		for(Map<Integer, Double> doc: bow.termDocumentMatrix) {
			System.out.println(doc);
		}
		
		
		// Print weighted term frequency matrix:
		System.out.println("\nWeighted document vectors (size " + bow.dictionary.size() + "):");
		for(Map<Integer, Double> doc: bow.weightedTermFrequencyMatrix) {
			System.out.println(doc);
		}

		
		// Compare cosine similarities of two sentences:
		int x = 8;
		int y = 9;
		System.out.println("\nCosine similarity of sentence " + x + " and " + y + ":");
		System.out.print("Unweighted: ");
		System.out.print(bow.cosineSimilarity(bow.termDocumentMatrix.get(x - 1), bow.termDocumentMatrix.get(y - 1)));
		System.out.print("\nWeighted:   ");
		System.out.println(bow.cosineSimilarity(bow.weightedTermFrequencyMatrix.get(x - 1), bow.weightedTermFrequencyMatrix.get(y - 1)));

	}
}
