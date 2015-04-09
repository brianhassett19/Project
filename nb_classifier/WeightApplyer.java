package nb_classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightApplyer {

	/**
	 * @param numWords
	 * @param documentVectors
	 * @return a matrix of doubles representing the if-idf weighted values
	 * of a term frequency matrix.			 */
	public static List<Map<Integer, Double>> apply_tfidf_Weights(int numWords, List<Map<Integer, Double>> documentVectors) {

		double[] inverseDocumentFrequency = getInverseDocumentFrequency(numWords, documentVectors);
		
		List<Map<Integer, Double>> weightedDocumentVectors = new ArrayList<>();

		for(Map<Integer, Double> doc: documentVectors) {
			Map<Integer, Double> curDoc = new HashMap<Integer, Double>();
			for(int i : doc.keySet()) {
				double v = doc.get(i);
				curDoc.put(i, v * inverseDocumentFrequency[i - 1]);
			}

			weightedDocumentVectors.add(curDoc);
		}

		return weightedDocumentVectors;
	}


	/**
	 * @param numWords
	 * @param documentVectors
	 * @return a double[] array representing the inverse document frequency
	 * for each term in the current document.			 */
	private static double[] getInverseDocumentFrequency(int numWords, List<Map<Integer, Double>> documentVectors) {

		double[] inverseDocumentFrequency = new double[numWords];
		int N = documentVectors.size();
		double[] termCounts = termCount(numWords, documentVectors);

		for(int i = 0; i < inverseDocumentFrequency.length; i++) {
			inverseDocumentFrequency[i] = Math.log10((N / termCounts[i]));
		}

		return inverseDocumentFrequency;
	}

	
	/**
	 * @param numWords
	 * @param documentVectors
	 * @return the number of documents each word from the dictionary appears in.	 */
	private static double[] termCount(int numWords, List<Map<Integer, Double>> documentVectors) {
		double[] termCounts = new double[numWords];
		for(Map<Integer, Double> doc: documentVectors) {
			for(int i : doc.keySet()) {
					termCounts[i - 1]++; // for each word with index i, increment the termCounts array - i.e. one more document containing word with index 'i'
			}	
		}

		return termCounts;
	}
}
