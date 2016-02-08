/*
 * This class get information and store information into a SQlite database
 * 1. createTable 	- create a table with a name as input
 * 2. tableNames 	- return a HashSet with the table names
 * 3. insertRecords	- insert a record into a named column
 */

package com.panzoto.common;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Sqlite {

	// initialize the table with a given table name
	public Sqlite() throws ClassNotFoundException {

	}

	public void createTable(String tableName) throws ClassNotFoundException {
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:brain.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			statement.executeUpdate("CREATE TABLE " + tableName
					+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT)");
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found

			if (e.getMessage().equals("table Reddit already exists")) {
				// handles errors if table exist
			} else {
				System.err.println(e.getMessage());
			}
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}

	public Set<String> tableNames() throws ClassNotFoundException {

		Set<String> tableNames = new HashSet<String>();
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:brain.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			DatabaseMetaData md = connection.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);

			while (rs.next()) {
				// read the result set
				tableNames.add(rs.getString(3));
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
		return tableNames;
	}

	public void insertRecord(String tableName, String columnName, ArrayList<String> record)
			throws ClassNotFoundException {

		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:brain.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			// statement.executeUpdate("ALTER TABLE " + tableName +
			// " ADD COLUMN "+ columnName + " VARCHAR(20)");

			for (int i = 0; i < record.size(); i++) {
				statement.executeUpdate("INSERT INTO " + tableName + " ("
						+ columnName + ")" + " VALUES('" + record.get(i) + "')");
			}

		} catch (SQLException e) {

			// if the error message is "out of memory",
			// it probably means no database file is found
			if (e.getMessage().equals("UNIQUE constraint failed: Reddit.Topic")) {
				// handle records with duplicates
			} else {
				System.err.println(e.getMessage());
			}
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}

	public void example() throws ClassNotFoundException {

		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			statement.executeUpdate("drop table if exists person");
			statement
					.executeUpdate("create table person (id integer, name string)");
			statement.executeUpdate("insert into person values(1, 'leo')");
			statement.executeUpdate("insert into person values(2, 'yui')");
			ResultSet rs = statement.executeQuery("select * from person");
			while (rs.next()) {
				// read the result set
				System.out.println("name = " + rs.getString("name"));
				System.out.println("id = " + rs.getInt("id"));
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			if (e.getMessage().equals("UNIQUE constraint failed: Reddit.Topic")) {
				// handle records with duplicates
			} else {
				System.err.println(e.getMessage());
			}
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException {
		Sqlite database = new Sqlite();
		database.createTable("names");
		System.out.println(database.tableNames());

	}
}