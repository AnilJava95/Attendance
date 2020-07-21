/*
 * Author's name and email: Michael, michaeljava95@gmail.com
 * Program description: This class is used by Menu class to do database operations.
 * Latest version: 4:04 PM, 5/6/2019. Carried c and stmt variables to field. Also, 
 *  changed getClassList method so now it doesnt create a fixed 10 length array.
 *  Now, it first finds length of table class list.
 * Older versions: 5:27 PM, 5/3/2019. Now the program has multiple classes.
 *  2:44 PM, 4/25/2019.
 */

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

final public class Database
{
	public void operation(int studentID, String weekNo, String className, String 
		operation)
	{
		Connection c;
		Statement stmt;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
				"root", "");
			//System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			if ("update".equals(operation))
			{
				stmt.executeUpdate("UPDATE " + className + " SET " + weekNo + " = 1 " 
					+ "WHERE studentID = " + studentID);
			}
			else if ("insert".equals(operation))
			{
				stmt.executeUpdate("INSERT INTO " + className + " (studentID , " + 
					weekNo + ")" + " VALUES ('" + studentID + "', '" + 1 + "');");
			}
			
			stmt.close();
			c.close();
			//System.out.println("Operation done successfully");
		} 
		catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			//System.out.println("Operation failed.");
		}
	}
	
	public boolean isRegistered(int studentID, String className)
	{
		Connection c;
		Statement stmt;
		
      try {
         Class.forName("com.mysql.jdbc.Driver");
         c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
            "root", "");
         //System.out.println("Opened database successfully");
      
         stmt = c.createStatement();
         ResultSet rs = stmt.executeQuery( "SELECT * FROM " + className + ";" );
			
         while ( rs.next() ) {
            String currentStudentID = rs.getString("studentID");
				if (studentID == Integer.parseInt(currentStudentID))
				{
					rs.close();
					stmt.close();
					c.close();
					return true;
				}
         }
			
			rs.close();
			stmt.close();
			c.close();
			return false;
      }
		catch ( Exception e ) {
         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
         //System.out.println("Operation failed.");
      }
		return false;
	}
	
	public void toText()
	{
		Connection c;
		Statement stmt;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
				"root", "");
			//System.out.println("Opened database successfully");
			
			// Getting class list.
			String[] classList = getClassList();
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + classList[0] + ";");
			
			for (byte counter = 0; counter < classList.length; counter++)
			{
				//stmt = c.createStatement();
				rs = stmt.executeQuery("SELECT * FROM " + classList[counter] + ";");
				File file = new File("D:\\" + classList[counter] + ".txt"); 
				PrintWriter writer = new PrintWriter(file);

				while (rs.next())
				{
					String studentID = rs.getString("studentID");
					int participationCounter = Integer.parseInt(rs.getString("week1"))
						+ Integer.parseInt(rs.getString("week2"))
						+ Integer.parseInt(rs.getString("week3"))
						+ Integer.parseInt(rs.getString("week4"))
						+ Integer.parseInt(rs.getString("week5"))
						+ Integer.parseInt(rs.getString("week6"))
						+ Integer.parseInt(rs.getString("week7"))
						+ Integer.parseInt(rs.getString("week8"))
						+ Integer.parseInt(rs.getString("week9"))
						+ Integer.parseInt(rs.getString("week10"))
						+ Integer.parseInt(rs.getString("week11"))
						+ Integer.parseInt(rs.getString("week12"));
					writer.printf("%s: %2d%n", studentID, participationCounter);
				}

				writer.close();
			}
			
			rs.close();
			stmt.close();
			c.close();
		}
		catch ( Exception e ) {
         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
         //System.out.println("Operation failed.");
      }
	}
	
	public void toExcel()
	{
		Connection c;
		Statement stmt;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
				"root", "");
			//System.out.println("Opened database successfully");
			
			// Getting class list.
			String[] classList = getClassList();
			
			stmt = c.createStatement();
			
			for (byte counter = 0; counter < classList.length; counter++)
			{
				String filename = "D:/" + classList[counter] + ".xls";
				String tablename = classList[counter];
				//stmt = c.createStatement();
				stmt.executeQuery("SELECT * INTO OUTFILE \"" + filename + "\" "
					+ "FROM " + tablename);
			}
			
			stmt.close();
			c.close();
		}
		catch ( Exception e ) {
         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
         //System.out.println("Operation failed.");
      }
	}
	
	public String[] getClassList()
	{
		Connection c;
		Statement stmt;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",
				"root", "");
			//System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM class_list;" );
			
			byte length = 0;
         while (rs.next() && rs.getString("class_name") != null) {
				length++;
         }
			
			rs = stmt.executeQuery( "SELECT * FROM class_list;" );
			
			byte counter = 0;
			String [] classList = new String[length];
         while (rs.next()) {
            String className = rs.getString("class_name");
				classList[counter] = className;
				counter++;
         }
			
			rs.close();
			stmt.close();
			c.close();
			return classList;
		}
		catch ( Exception e ) {
         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
         //System.out.println("Operation failed.");
      }
		return null;
	}
}
