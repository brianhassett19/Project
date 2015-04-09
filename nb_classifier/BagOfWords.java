package nb_classifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.DB;

public class BagOfWords {

	private String file; // Location of file to be used for bag of words model.
	private List<String> preProcessesedStrings;
	public Map<String, Integer> dictionary = new HashMap<>();  // Key: word; Value: integer index.
	public List<Map<Integer, Double>> termDocumentMatrix = new ArrayList<>(); // Key: integer index; Value: word count.
	public List<Map<Integer, Double>> weightedTermFrequencyMatrix = new ArrayList<>(); // Key: integer index; Value: td-idf weight.
	
	/**
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException					 */
	public BagOfWords(String file) throws FileNotFoundException, IOException {
		this.file = file;
		this.preProcessesedStrings = preProcessStrings(this.file);
		this.dictionary = createDictionary(preProcessesedStrings);
		this.termDocumentMatrix = createTermFrequencyMatrix(preProcessesedStrings, dictionary);
		this.weightedTermFrequencyMatrix = WeightApplyer.apply_tfidf_Weights(this.dictionary.size(), termDocumentMatrix); // Static reference to WeightApplyer
	}

	/**
	 * @param preProcessesedStrings
	 * @return a map representation of the dictionary based on the text data provided
	 * @throws FileNotFoundException
	 * @throws IOException							 */
	private Map<String, Integer> createDictionary(List<String> preProcessesedStrings) throws FileNotFoundException, IOException {
		
		/* For each preprocessed string, split it up into words
		 * and add them to the dictionary with an index as the
		 * value (no duplicates)	 */

		int i = 1; // Unique index (value)
		for(String sentence: preProcessesedStrings) {
			for(String s: sentence.split(" ")) {
				if(!dictionary.containsKey(s))
					dictionary.put(s, i++); // s is the unique key which represents each word, i is the unique index for each word (value)
			}
		}
		
		System.out.println("Dictionary size = " + dictionary.size()); // Optional
		
		return dictionary;
	}

	/**
	 * @param file
	 * @return a list of preprocessed strings
	 * @throws FileNotFoundException
	 * @throws IOException					 */
	private List<String> preProcessStrings(String file) throws FileNotFoundException, IOException {
		
		// Read in the strings from the file:
		List<String> stringList = IO.getFileContents(file);		

		// Convert the strings to StringBuilders, preprocess to remove punctuation, and finally prepare a list of cleaned strings:
		List<StringBuilder> stringBuilderList = new ArrayList<>();
		for(String s: stringList) {
			stringBuilderList.add(new StringBuilder(s));
		}

		PreProcessor pp = new PreProcessor();

		List<String> cleanStrings = new ArrayList<>();
		for(StringBuilder sb: stringBuilderList) {
			sb = pp.clean(sb);
			cleanStrings.add(sb.toString());
		}

		return cleanStrings;
	}

	/**
	 * @param preProcessesedStrings
	 * @param dictionary
	 * @return a list of term frequency vectors compiled from the preprocessed strings and dictionary		 */
	private List<Map<Integer, Double>> createTermFrequencyMatrix(List<String> preProcessesedStrings, Map<String, Integer> dictionary) {	
		
		for(String s: preProcessesedStrings) {
			termDocumentMatrix.add(createDocumentVector(s, dictionary));
		}
		
		System.out.println("tfm size = " + termDocumentMatrix.size()); // Optional
		
		return termDocumentMatrix;
	}
	
	
	/**
	 * @param sentence
	 * @param dictionary
	 * @return a frequency vector representing a sentence in the training data					 */
	private Map<Integer, Double> createDocumentVector(String sentence, Map<String, Integer> dictionary) {
		
		Map<Integer, Double> doc = new HashMap<>();
		
		for(String s : sentence.split(" ")) {
			int curKey = dictionary.get(s); // get the dictionary's index for the current word (to be used as a key in 'doc')
			
			/* If the current word vector already contains an entry for this word,
			increment the count (i.e. the value for key 'curKey'). 
			Otherwise, add the word to the vector with value 1:						*/
			if(doc.containsKey(curKey)) {
				double v = doc.get(curKey);
				doc.put(curKey, ++v);
			}
			else {
				doc.put(curKey, (double) 1);
			}
		}

		return doc;
	}
	
	
	public double cosineSimilarity(Map<Integer, Double> v1, Map<Integer, Double> v2) {

		double cosineSimilarity = 0; // To be returned

		// Calculate the numerator:
		double dotProd = 0; // v1 x v2 (dot product of v1 and v2)
		for(int i = 0; i < v1.size(); i++) {
			dotProd += (v1.get(i) * v2.get(i));
		}

		// Calculate the denominator:
		double magV1_sqrd = 0; // |v1|^2
		double magV2_sqrd = 0; // |v2|^2

		for(int i = 0; i < v1.size(); i++) {
			magV1_sqrd += Math.pow(v1.get(i), 2);
		}

		for(int i = 0; i < v2.size(); i++) {
			magV2_sqrd += Math.pow(v2.get(i), 2);
		}

		double magV1 = Math.pow(magV1_sqrd, 0.5); // |v1|
		double magV2 = Math.pow(magV2_sqrd, 0.5); // |v2|

		double magProd = magV1 * magV2; // |v1| x |v2|

		// Divide to get the cosine similarity:
		cosineSimilarity = dotProd / magProd;

		return cosineSimilarity;
	}
}
