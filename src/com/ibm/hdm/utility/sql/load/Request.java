package com.ibm.hdm.utility.sql.load;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.Callable;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Implementation to process requests to execute SQL statements.  Depending on 
 * configuration variables, the request will be processed either independently or 
 * as part of a FutureTask. 
 * 
 * @author Jeff Tuck
 */

public class Request implements Callable<HashMap> {
	
	private ConfigurationBean config = null;
	private Connection connection = null;
	private String sql = "";
	private String fileName = "";
	private int executionSequence = 0;
	
	/**
	 * Constructor for Request
	 * @param sql - The SQL to be executed.
	 * @param fileName - The name of the file that contains the SQL.
	 * @param executionSequence - The sequence with which the SQL was executed. 
	 * @param config - The configuration for SQL Load.
	 */
	public Request(String sql, String fileName, int executionSequence, ConfigurationBean config) {
		super();
		this.config = config;
		this.sql = sql;
		this.fileName = fileName;
		this.executionSequence = executionSequence;
	}
	
	
	/**
	 * Execute the SQL that was used when creating the Request object.
	 * @return A Hashmap containing the results of the SQL execution such as start time, end time
	 * SQL file name that contained the SQL executed, etc...
	 */
	protected HashMap executeSQL() {
		HashMap<String, String> resultsMap = new HashMap<String, String>();
		int returnCode = 0;
		String status = "success";
		String stackTrace = "";
		long startTime = 0;
		long endTime = 0;
		long elapsedTime = 0;
		System.out.println("#########################################################################################");
		System.out.println("*****  EXECUTING SQL FOR FILE " + this.fileName + "  *****");
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		Statement statement;
		this.connection = getConnection();
		try {
			statement = this.connection.createStatement();
			//statement.executeUpdate("set schema \"admin\"");
			startTime = System.currentTimeMillis();
			System.out.println("PROCESS START TIME: " + sdf.format(startTime) + System.lineSeparator());
			ResultSet resultSet = statement.executeQuery(this.sql);
			endTime = System.currentTimeMillis();
			elapsedTime = endTime - startTime;
			System.out.println("*****  END SQL EXECUTION FOR FILE " + this.fileName + "  *****");
			System.out.println("");
			//printResultSet(resultSet);
			System.out.println("");
			System.out.println("PROCESS END TIME: " + sdf.format(endTime));
			System.out.println("TOTAL PROCESS TIME: " + elapsedToString(elapsedTime));
			
			statement.close();
			this.connection.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			endTime = System.currentTimeMillis();
			e.printStackTrace();
			System.out.println("Query Executed:  " + this.sql);
			returnCode = 1;
			status = "error";
			stackTrace = e.toString();
			
		}
		resultsMap.put("sqlFileName", this.fileName);
		resultsMap.put("executionSequence", Integer.toString(this.executionSequence));
		resultsMap.put("status", status);
		resultsMap.put("userName", config.getDbUserName());
		resultsMap.put("startTime", sdf.format(startTime));
		resultsMap.put("endTime", sdf.format(endTime));
		resultsMap.put("elapsedTime", elapsedToString(elapsedTime));
		resultsMap.put("sql", this.sql);
		resultsMap.put("stackTrace", stackTrace);
		
       
		System.out.println("");
		System.out.println("#########################################################################################");
		return resultsMap;
	}
	
	/**
	 * Gets a Db2 formatted JDBC URL based on the configuration.
	 * @return The Db2 JDBC URL.
	 */
	private String getJdbcUrl() {
		String jdbcUrl = "jdbc:db2://" + config.getHostName() + ":" + config.getPort() + "/" + config.getDbName();
		if(config.isUseSSL()) {
			jdbcUrl = jdbcUrl + ":sslConnection=true;";
		}
		return jdbcUrl;
	}
	
	/**
	 * Gets a JDBC connection to the configured database.
	 * @return The JDBC connection.
	 */
	private Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(config.getJdbcDriver());
			connection = DriverManager.getConnection(getJdbcUrl(), config.getDbUserName(), config.getDbPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return connection;
	}
	
	/**
	 * Formats elapsed system time to human readable format.
	 * @param elapsedTimeMillis - Elapsed time to be formatted.
	 * @return
	 */
	private static String elapsedToString(long elapsedTimeMillis) {
	    long seconds = (elapsedTimeMillis) / 1000; // round
	    long minutes = seconds / 60;
	    long hours = minutes / 60;
	    return String.format("%1$02d:%2$02d:%3$02d",
	        hours,
	        minutes % 60,
	        seconds % 60);
	}
	
	/**
	 * Convenience method for printing result sets.  See commented out line in executeSQL() to 
	 * quickly leverage and see results in output.
	 * @param resultSet - The result to be printed.
	 * @throws SQLException 
	 */
	private void printResultSet(ResultSet resultSet) throws SQLException {
		ResultSetMetaData rsmd = resultSet.getMetaData();
	    //System.out.println("SQL Statement: " + this.sql);
		System.out.println("**************************************************************************");
		
	   int columnsNumber = rsmd.getColumnCount();
	   while (resultSet.next()) {
	       for (int i = 1; i <= columnsNumber; i++) {
	           if (i > 1) System.out.print(" = ");
	           String columnValue = resultSet.getString(i);
	           System.out.print(columnValue + " " + rsmd.getColumnName(i) + " | ");
	       }
	       System.out.println("");
	   }
	   
	   System.out.println("**************************************************************************");
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public HashMap call() throws Exception {
		HashMap resultsMap = executeSQL(); 
		return resultsMap;
	}

}
