package nb_classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IO {
	
	/**
	 * @param filename
	 * @return a list of strings read from a file using newline as delimiter
	 * @throws IOException
	 * @throws FileNotFoundException			*/
	public static List<String> getFileContents(String filename) throws IOException, FileNotFoundException {
		File file = new File(filename);

		List<String> list = new ArrayList<>();
		StringBuilder contents = new StringBuilder();
		BufferedReader input = new BufferedReader(new FileReader(file));

		try {
			String line;

			while ((line = input.readLine()) != null) {
				contents.append(line);
				//contents.append(System.getProperty("line.separator")); // adds a new line character
				list.add(contents.toString());
				contents = new StringBuilder(); // clear the StringBuilder to start on the next line.
			}
		} finally {
			input.close();
		}

		return list;
	}

	/**
	 * Prints a representation of an int[] array
	 * @param v			 */
	public static void printVector(int[] v) {
		System.out.print("[");
		for(int i = 0; i < v.length; i++) {
			if(i != v.length - 1) {
				System.out.print(v[i] + ", ");
			}
			else {
				System.out.print(v[i] + "]");
			}
		}

		System.out.println();
	}

	/**
	 * Prints a representation of a double[] array
	 * @param v					 */
	public static void printVector(double[] v) {
		System.out.print("[");
		for(int i = 0; i < v.length; i++) {
			double curValue = (int) (v[i] * 1000); // For rounding to 3 decimal places
			if(i != v.length - 1) {
				System.out.format((curValue) / 1000.0  + ", ");
			}
			else {
				System.out.print(( curValue) / 1000.0 + "]");
			}
		}

		System.out.println();
	}

	/**
	 * Writes strings to a text file in the specified location
	 * @param data			 */
	public static void writeToFile(String data)
	{
		try {
			FileWriter fw = new FileWriter("C:\\Users\\user\\Desktop\\movieReviewTestResult.txt", true);
			fw.write(data);//appends the string to the file
			//fw.write("\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * @param v
	 * @return a String representation of an int[] array.	*/
	public static String vectorToString(int[] v) {

		StringBuilder sb = new StringBuilder("");

		sb.append("[");
		for(int i = 0; i < v.length; i++) {
			if(i != v.length - 1) {
				sb.append(v[i] + ", ");
			}
			else {
				sb.append(v[i] + "]");
			}
		}
		String s = sb.toString();

		return s;
	}
	
	/**
	 * @param v
	 * @return a String representation of a double[] array.	*/
	public static String vectorToString(double[] v) {

		StringBuilder sb = new StringBuilder("");

		sb.append("[");
		for(int i = 0; i < v.length; i++) {
			double curValue = (int) (v[i] * 1000); // For rounding to 3 decimal places
			if(i != v.length - 1) {
				sb.append((curValue) / 1000.0  + ", ");
			}
			else {
				sb.append((curValue) / 1000.0  + "]");
			}
		}
		String s = sb.toString();

		return s;
	}
}
