package nb_classifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import util.DB;

/*
 * This is an implementation of the Naive Bayes text classifier as described
 * in Dan Jurafsky's (Stanford) natural language processing lecture notes, see [1].
 * 
 * References:	
 * [1]	Stanford - Text Classification and Naive Bayes
 * 		https://web.stanford.edu/class/cs124/lec/naivebayes.pdf		
 * [2]	Wikipedia - Naive Bayes Classifier
 * 		http://en.wikipedia.org/wiki/Naive_Bayes_classifier
 * [3]	NYU - Bag of Words Methods for Text Mining
 * 		http://cs.nyu.edu/courses/spring14/CSCI-GA.2590-001
 * [4]	Oregon State - Naive Bayes
 * 		http://inst.eecs.berkeley.edu/~cs188/sp12/slides/cs188%20lecture%2020%20--%20naive%20bayes%206PP.pdf
 */

/**
 * @author Brian */
public class NB_Classifier {

	String textTrainingDataFileLocation; // Location of text file containing text training examples
	String sentimentTrainingDataFileLocation; // Location of text file containing labels for training examples
	BagOfWords bow; // Bag of Words model generated from the text training examples
	List<Document> trainingData = new ArrayList<>(); // List of training documents
	List<String> trainingLabels; // A list of  labels (classes) for the training data
	List<int[]> wordOccurrences = new ArrayList<>(); // count(occurrences of w_i in all docs labelled c_j)
	List<double[]> wordFrequencies = new ArrayList<>(); // P(w_i | c_j)
	List<Category> categories = new ArrayList<>(); // List of categories appearing in the training data
	PreProcessor pp; // Preprocessor for cleaning text before classifying

	/**
	 * @param textData
	 * @param sentimentData
	 * @param categories
	 * @throws FileNotFoundException
	 * @throws IOException				 */
	public NB_Classifier(String textData, String sentimentData, List<Category> categories) throws FileNotFoundException, IOException {
		this.textTrainingDataFileLocation = textData;
		this.sentimentTrainingDataFileLocation = sentimentData;
		this.bow = new BagOfWords(textData);
		this.categories = categories;
		this.trainingData = getTextTrainingData(bow.termDocumentMatrix);
		assignTrainingLabels();
		setCategoryFrequencies();
		this.wordFrequencies = train(trainingData, bow.dictionary.size());
		this.pp = new PreProcessor();
		//commitTermFrequencyMatrix();
	}

	/**
	 * @param text
	 * @return c_NB, i.e. the category chosen by the classifier for the given text.	 
	 * @throws SQLException */
	public String classify(String text) throws SQLException {

		// Use argmax function to find c_NB, the most probable (MAP) class:
		//String c_NB = argmax_c_j(text).name;
		String c_NB = argmax_c_j_fromDB(text).name;
		return c_NB;
	}

	/**
	 * @param trainingData
	 * @param size
	 * @return wordFrequencies: a list of arrays representing P(w_i | c_j).	 */
	private List<double[]> train(List<Document> trainingData, int size) {

		// Find the word occurrences for each word w_i in each category c_j:
		wordOccurrences = getWordOccurrences(trainingData, size);

		// Find P(w_i | c_j) for each word:
		for(Category category : categories) {
			double[] curWordFrequency = new double[size];
			int N = category.megaDocumentSize;
			int i = 0; // array index
			for(int count : wordOccurrences.get(category.class_id)) {
				curWordFrequency[i++] = ((double) (count + 1)) / (N + size);
			}
			wordFrequencies.add(curWordFrequency);
		}

		return wordFrequencies;
	}


	/**
	 * Commits the trained Naive Bayes classifier's parameters to the database.	 */
	private void commitTermFrequencyMatrix() {
		
		/* Connect to DB and write search results:		*/
		Connection con = DB.getConnection();
		
		// Sort the map:
		Map<String, Integer> sortedDicitionary = new TreeMap<String, Integer>(bow.dictionary);
		
		// Commit the terms and their respective posterior probabilities to the DB:
		int i;
		for(String term : sortedDicitionary.keySet()) {
			i = bow.dictionary.get(term);
			double pos = wordFrequencies.get(0)[i - 1];
			double neg = wordFrequencies.get(1)[i - 1];
			
			try {
				DB.insertProbabilityEntry(con, i, term, pos, neg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
														
	/**
	 * @param trainingData
	 * @param size
	 * @return  wordOccurrences: a list of arrays representing: 
	 * 				count(occurrences of w_i in all docs labelled c_j) 	 */
	private List<int[]> getWordOccurrences(List<Document> trainingData, int size) {		

		/* curWordOccurrence is an int array representing the numerator
		 * of the P(w_i | c_j) formula:
		 * 		count(occurrences of w_i in all docs labelled c_j). 
		 * It's used to create the wordFrequencies array. 				*/

		for(Category category : categories) {
			int[] curWordOccurrence = new int[size];
			for(Document doc : trainingData) {
				if(doc.label == category.class_id) {
					category.megaDocumentSize += count(doc.termFrequencyVector);
					for(int i : doc.termFrequencyVector.keySet()) {
						curWordOccurrence[i - 1] += doc.termFrequencyVector.get(i);
					}
				}
			}

			wordOccurrences.add(curWordOccurrence);
		}

		return wordOccurrences;
	}

	/**
	 * @param vector
	 * @return count of total number of words in a frequency vector (i.e. sum of a map's values).	 */
	private int count(Map<Integer, Double> vector) {
		//System.out.println(vector);
		int count = 0;

		for(int i : vector.keySet()) {
			count += vector.get(i);
		}

		return count;
	}

	/**
	 * @param termFrequencyMatrix
	 * @return A list of Documents representing the text training data 
	 * @throws FileNotFoundException
	 * @throws IOException				 */
	private List<Document> getTextTrainingData(List<Map<Integer, Double>> termFrequencyMatrix) throws FileNotFoundException, IOException {

		int id = 0;
		for(Map<Integer, Double> vector: bow.termDocumentMatrix) {
			Document doc = new Document(id, vector);
			trainingData.add(doc);
			id++;
		}

		return trainingData;
	}

	/**
	 * Reads the training labels from a text file and assigns them to the Document representing
	 * each training example in trainingData.	
	 * @throws FileNotFoundException
	 * @throws IOException	 			*/
	private void assignTrainingLabels() throws FileNotFoundException, IOException {
		trainingLabels = IO.getFileContents(sentimentTrainingDataFileLocation);
		String[] trainingLablesArray = new String[trainingLabels.size()];
		trainingLablesArray = trainingLabels.toArray(trainingLablesArray);

		int curDoc = 0;
		for(Document doc: trainingData) {
			doc.label = Integer.parseInt(trainingLablesArray[curDoc]);			
			categories.get(doc.label).numDocs += 1;
			curDoc++; 
		}
	}

	/**
	 * Assigns P(c) for each category	 */
	private void setCategoryFrequencies() {
		for(Category category : categories) { // OPTIONAL
			System.out.println("category: " + category.name); // OPTIONAL
			System.out.println("numDocs:" + category.numDocs); // OPTIONAL
			System.out.println("totalDocuments: " + Document.totalDocuments); // OPTIONAL
			category.frequency = (double) category.numDocs / Document.totalDocuments;
			System.out.println("frequency: " + category.frequency + "\n"); // OPTIONAL
		}
	}

	/**
	 * @param text
	 * @return the most probable category for the given text	 */
	private Category argmax_c_j(String text) {

		// Clean the text and clear the probability data in each category:
		text = pp.clean(text);
		clearCategoryProbabilities();

		for(String s: text.split(" ")) {
			if(bow.dictionary.containsKey(s)) {
				int index = bow.dictionary.get(s);
				for(Category category : categories) {
					category.p += Math.log10(wordFrequencies.get(category.class_id)[index - 1]);					
				}
			}
		}

		// For each category add log(P(c)) to its probability as calculated so far:
		for(Category category : categories) {
			category.p += Math.log10(category.frequency); // frequency = P(c)
			System.out.println("P(" + category.name + "):" + category.p); // OPTIONAL
		}

		// Once all the probabilities have been calculated, find the category with the largest:
		return maxProbability(categories);
	}
	
	/**
	 * @param text
	 * @return the most probable category for the given text using the values from the DB 
	 * @throws SQLException */
	private Category argmax_c_j_fromDB(String text) throws SQLException {
		Connection con = DB.getConnection();
		
		// Clean the text and clear the probability data in each category:
		text = pp.clean(text);
		clearCategoryProbabilities();

		for(String s: text.split(" ")) {
			if(DB.findTerm(con, s) == 1) {
				double pos = DB.getPositiveProbability(s);
				double neg = DB.getNegativeProbability(s);
				categories.get(0).p += Math.log10(neg);	
				categories.get(1).p += Math.log10(pos);
			}
		}

		// For each category add log(P(c)) to its probability as calculated so far:
		for(Category category : categories) {
			category.p += Math.log10(category.frequency); // frequency = P(c)
			System.out.println("P(" + category.name + "):" + category.p); // OPTIONAL
		}

		// Once all the probabilities have been calculated, find the category with the largest:
		return maxProbability(categories);
	}

	/**
	 * Resets the probability of each category to zero	 */
	private void clearCategoryProbabilities() {
		for(Category category : categories) {
			category.p = 0;
		}
	}

	/**
	 * Runs through the categories to find the one with the max value for its probability.
	 * @param categories
	 * @return a category representing c_NB for the text being classified	 */
	private Category maxProbability(List<Category> categories) {

		Iterator<Category> it = categories.iterator();

		Category curCategory, maxCategory;

		curCategory = it.next();
		maxCategory = curCategory;

		while(it.hasNext()) {
			curCategory = it.next();
			if(curCategory.p > maxCategory.p) {
				maxCategory = curCategory;
			}
		}

		return maxCategory;
	}
}
