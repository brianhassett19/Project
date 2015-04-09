package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * This code is used to access the database on the localhost. So far it can display the records on one table. It needs to be edited to store
 * data in the table also.
 */

public class DB
{
	private static String url = "jdbc:mysql://localhost:3306/se_project";
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String username = "root";
	private static String password = "";
	private static Connection con;

	public static void main(String[] args) throws Exception
	{
		
		// Accessing driver from the JAR file:
		///Class.forName("com.mysql.jdbc.Driver");
				
		// Creating a variable for the connection called 'con':
		///Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/se_project","root","");
		
		// Connect to DB:
		con = getConnection();
		
		// Query:
		PreparedStatement statement = con.prepareStatement("select * from tweet");
		
		// Creating a variable to execute the query:
		ResultSet result = statement.executeQuery();
		
		// Display results:
		while(result.next())
		{
			System.out.println(result.getString(1) + " " + result.getString(2));
		}
		
		insertTweet(con, "test tweet");
	}
	
	public static Connection getConnection()
	{
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return con;
	}
	
	public static void insertTweet(Connection con, String s) throws Exception
	{
		/*String query = "INSERT INTO `se_project`.`tweet` (`tweet`) VALUES (' " + s + "')";
		
		PreparedStatement p = con.prepareStatement(query);
		
		p.executeUpdate();*/
		
		PreparedStatement p = con.prepareStatement("INSERT INTO `se_project`.`tweet` (`tweet`) VALUES (?)"); // use prepared statement to escape problem characters (e.g. apostrophes)
		p.setString(1, s);
		p.executeUpdate();
		//return numberOfRowsChanged;
	}
	
	public static void insertProbabilityEntry(Connection con, int id, String term, double neg, double pos) throws Exception
	{
		/*String query = "INSERT INTO `se_project`.`tweet` (`tweet`) VALUES (' " + s + "')";
		
		PreparedStatement p = con.prepareStatement(query);
		
		p.executeUpdate();*/
		
		PreparedStatement p = con.prepareStatement("INSERT INTO `se_project`.`probabilities` (`id`, `term`, `negative`, `positive`) VALUES (?, ?, ?, ?)"); // use prepared statement to escape problem characters (e.g. apostrophes)
		p.setString(1, Integer.toString(id));
		p.setString(2, term);
		p.setString(3, Double.toString(neg));
		p.setString(4, Double.toString(pos));
		p.executeUpdate();
		//return numberOfRowsChanged;
	}
	
	public static int findTerm(Connection con, String s) throws SQLException {
		PreparedStatement p = con.prepareStatement("SELECT COUNT(*) FROM `se_project`.`probabilities` WHERE `term`=?"); // use prepared statement to escape problem characters (e.g. apostrophes)
		p.setString(1, s);
		ResultSet result = p.executeQuery();
		
		int i = 0;
		while(result.next()) {
			i = Integer.parseInt(result.getString(1));
		}
		
		return i;
	}
	
	public static int getIndex(String s) throws SQLException {
		PreparedStatement p = con.prepareStatement("SELECT `ìd` FROM `se_project`.`probabilities` WHERE `term`=?"); // use prepared statement to escape problem characters (e.g. apostrophes)
		p.setString(1, s);
		ResultSet result = p.executeQuery();
		
		if(result.next())
			return Integer.parseInt(result.getString(1));
		
		return -1;
	}
	
	public static double getPositiveProbability(String s) throws SQLException {
		PreparedStatement p = con.prepareStatement("SELECT `positive` FROM `se_project`.`probabilities` WHERE `term`=?"); // use prepared statement to escape problem characters (e.g. apostrophes)
		p.setString(1, s);
		ResultSet result = p.executeQuery();
		
		if(result.next())
			return Double.parseDouble(result.getString(1));
		
		return -1;
	}
	
	public static double getNegativeProbability(String s) throws SQLException {
		PreparedStatement p = con.prepareStatement("SELECT `negative` FROM `se_project`.`probabilities` WHERE `term`=?"); // use prepared statement to escape problem characters (e.g. apostrophes)
		p.setString(1, s);
		ResultSet result = p.executeQuery();
		
		if(result.next())
			return Double.parseDouble(result.getString(1));
		
		return -1;
	}
}
