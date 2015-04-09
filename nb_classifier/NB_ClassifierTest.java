package nb_classifier; // FINAL

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Brian
 */
public class NB_ClassifierTest {

	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {

		// Record training start time:
		long start = System.currentTimeMillis();
		
		// Provide locations of training data:
		String reviewsData = "C:\\Users\\Brian\\Desktop\\reviews.txt"; // Text file containing training sentences separated by new lines
		String sentimentsData = "C:\\Users\\Brian\\Desktop\\sentiments.txt"; // // Text file containing integer sentiments separated by new lines (sentiments should match categories below)
		
		
		// Create categories:
		/* Any number of categories can be created, but each category must have an int id and a String name	*/
		List<Category> categories = new ArrayList<>();
		categories.add(new Category(0, "negative"));
		categories.add(new Category(1, "positive"));
		
		
		// Instantiate classifier:
		NB_Classifier nb = new NB_Classifier(reviewsData, sentimentsData, categories);
				
		
		// record training end time:
		long end = System.currentTimeMillis();
		
		
		// Print training time:
		System.out.println("Training time: " + (end - start) + " milliseconds\n");
		
		
		// Use scanner to read in sentences and classify them:
		Scanner userInput = new Scanner(System.in);
		String text = "";
		while(true) {
			System.out.println("Enter text:");
			text = userInput.nextLine();
			System.out.println("Sentiment: " + nb.classify(text) + "\n");
		}
		
		
		
		/* Alternatively, provide a set of test documents to be classified. The output
		 * file is specified in IO.writeToFIle().
		 */
		/*int i = 1;
		File file = new File("C:\\Users\\user\\Desktop\\testData.txt");

		BufferedReader input = new BufferedReader(new FileReader(file));

		StringBuilder contents = new StringBuilder();
		
		try {
			String line;
			while ((line = input.readLine()) != null) {
				contents.append(line);
				IO.writeToFile(nb.classify(contents.toString()) + "\n");
				contents = new StringBuilder(); // clear the StringBuilder to start on the next line.
				i++;
				if(i % 1000 == 0)
					System.out.println("i:" + i);
			}
		} finally {
			input.close();
		}*/
		
		//userInput.close();
	}
}
