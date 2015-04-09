package nb_classifier;

import java.util.Map;

public class Document {

	static int totalDocuments = 0;
	int id;
	String text;
	Map<Integer, Double> termFrequencyVector;
	int label; // Corresponds to class_id in the corresponding category.

	/* Constructors for text as the content of the document:	*/

	/**
	 * @param id
	 * @param content
	 * @param label				*/
	public Document(int id, String content, int label) {
		this.id = id;
		this.text = content;
		this.label = label;
		totalDocuments++;
	}

	/**
	 * @param id
	 * @param content			 */
	public Document(int id, String content) {
		this.id = id;
		this.text = content;
		totalDocuments++;
	}

	/* Constructors for term frequency vector as the content of the document: 	*/
	
	/**
	 * @param id
	 * @param termFrequencyVector
	 * @param label				  */
	public Document(int id, Map<Integer, Double> termFrequencyVector, int label) {
		this.id = id;
		this.termFrequencyVector = termFrequencyVector;
		this.label = label;
		totalDocuments++;
	}

	/**
	 * @param id
	 * @param termFrequencyVector	*/
	public Document(int id, Map<Integer, Double> termFrequencyVector) {
		this.id = id;
		this.termFrequencyVector = termFrequencyVector;
		totalDocuments++;
	}

	/*
	public String toString() {
		// Text-based document:
		if(this.text != null) {
			StringBuilder doc = new StringBuilder("");

			doc.append("Doc ID " + this.id);
			doc.append((sentiment == 0) ? " (Negative): " : " (Positive): ");
			doc.append(this.text);

			String s = doc.toString();

			return s;
		}
		// Term frequency vector-based document:
		else {
			StringBuilder doc = new StringBuilder("");

			doc.append("Doc ID " + this.id);
			doc.append((sentiment == 0) ? " (Negative): " : " (Positive): ");
			doc.append(IO.vectorToString(this.termFrequencyVector));

			String s = doc.toString();

			return s;
		}
	}*/
}
