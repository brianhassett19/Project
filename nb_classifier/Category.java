package nb_classifier;


public class Category {
	
	int class_id; // An int representing the category. This id is used for labelling training documents.
	String name;
	int numDocs;
	double frequency; // Count of doc's in this class, divided by the total number of doc's (prior probability).
	double p; // The posterior probability to be calculated in the NB_Classifier class.
	int megaDocumentSize = 0; // The size of the document combining all documents from the this category (Initialize to zero).
	
	public Category() {
		this.class_id = 0;
		this.name = "";
		this.numDocs = 0;
		this.frequency = 0;
	}
	
	/**
	 * @param id
	 * @param name			 	*/
	public Category(int id, String name) {
		this.class_id = id;
		this.name = name;
		this.numDocs = 0;
	}
	
	/**
	 * @param id
	 * @param name
	 * @param frequency			 */
	public Category(int id, String name, double frequency) {
		this.class_id = id;
		this.name = name;
		this.frequency = frequency;
		this.numDocs = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
