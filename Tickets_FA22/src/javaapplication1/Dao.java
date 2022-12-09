package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	  
	}
	
	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();
			
			String createTicketsTable = "CREATE TABLE IF NOT EXISTS lmanz_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200))";
			String createUsersTable = "CREATE TABLE IF NOT EXISTS lmanz_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";


			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "INSERT INTO lmanz_users(uname,upass,admin) " + 
						"VALUES('" + rowData.get(0) + "', " + " '" + rowData.get(1) + "', '" + rowData.get(2) + "');";
						statement.executeUpdate(sql);
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String ticketName, String ticketDesc) {
		int id = 0;
		try {
			statement = getConnection().createStatement();
			statement.executeUpdate("INSERT INTO lmanz_tickets" + "(ticket_issuer, ticket_description) VALUES(" + " '"
					+ ticketName + "','" + ticketDesc + "')", Statement.RETURN_GENERATED_KEYS);

			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;

	}

	//view records 
	public ResultSet readRecords() {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM lmanz_tickets");
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}
	
	//update records
	public void updateRecords(String ticketId, String ticketDescription) { 
		  ResultSet rs = null;
		  
		  try { 
		  //begin connection
		  Statement statement = getConnection().createStatement(); 
		  //execute query 
		  rs = statement.executeQuery("SELECT ticket_description FROM lmanz_tickets WHERE ticket_id = " + ticketId); 
		  getConnection().close(); 
		  String Results = null;
		  
		  while (rs.next()) {
			  Results = rs.getString("ticket_description");
		 }
		  
		  PreparedStatement ps = getConnection().prepareStatement("UPDATE lmanz_tickets SET ticket_description = ? WHERE ticket_id = ?"); 
		  
		  String ticketDesc = Results + "\nUpdate: " + ticketDescription;
		  //retrive fields from table 
		  ps.setString(1, ticketDesc); 
		  ps.setString(2, ticketId); 
		  ps.executeUpdate(); 
		  ps.close(); //close db connection
		  
		  } catch (SQLException e1) { 
			  //handle errors
			  System.out.println("Could not update the record");
			  e1.printStackTrace(); 
			  } 
		  }
		
	//delete a record
	public int deleteRecords(int ticketId){ 
		
	  try { 
		  //begin connection
		  Statement statement = getConnection().createStatement(); 
		  //execute query 
		  statement.executeUpdate("DELETE FROM lmanz_tickets WHERE ticket_id = '" + ticketId + "'");
	  
	  }catch(SQLException e1) { 
		  //handle errors
		  System.out.println("Could not delete the record");
		  e1.printStackTrace(); 
		 } 
	  return ticketId; 
	}
	
	//close a record
	public void closeRecord(String ticketId) {
		ResultSet resultSet = null;
		try {
			//begin connection
			Statement statement = getConnection().createStatement();
			//execute query 
			resultSet = statement.executeQuery("SELECT ticket_description FROM lmanz_tickets WHERE ticket_id = " + ticketId);
			getConnection().close();
			String results = null;
			
			PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE lmanz_tickets SET ticket_description = ? WHERE ticket_id = ?");
			String ticketDesc = "Closed";
			//retrieve fields from table 
			preparedStatement.setString(1, ticketDesc);
			preparedStatement.setString(2, ticketId);
			preparedStatement.executeUpdate();
			preparedStatement.close(); //close db connection
			
		} catch (SQLException e1) {
			//handle errors
			System.out.println("Could not close the record");
			e1.printStackTrace();
		}
	}

}
