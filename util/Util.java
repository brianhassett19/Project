package util;

import java.io.FileWriter;
import java.io.IOException;

public class Util
{	
	public static void writeToFile(String data)
	{
		try {
			FileWriter fw = new FileWriter("C:\\Users\\Brian\\Desktop\\Test.txt", true);
            fw.write(data);//appends the string to the file
            fw.write("\n");
            fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
