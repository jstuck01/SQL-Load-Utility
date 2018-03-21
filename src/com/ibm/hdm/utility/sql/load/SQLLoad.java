package com.ibm.hdm.utility.sql.load;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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
 * SQLLoad is the main entry class for the SQL Load Utility.   
 * 
 * @author Jeff Tuck
 */

public class SQLLoad {
	
	public static void main(String args[]) {
		
		int successCount = 0;
		int errorCount = 0;
		int executionSequence = 1;
		ConfigurationBean config = new ConfigurationBean();
		HashMap<String, String> sqlStatments = getSqlStatements(config.getSqlDirectory());
		Iterator<Entry<String, String>> sqlIterator = sqlStatments.entrySet().iterator();
		ArrayList<HashMap<String, String>> resultsArrayList = new ArrayList<HashMap<String, String>>();
		
		if(config.isUseThreads()) {
			ExecutorService pool = Executors.newFixedThreadPool(config.getMaxThreadCount());
		    Set<Future<HashMap<String, String>>> set = new HashSet<Future<HashMap<String, String>>>();
			while(sqlIterator.hasNext()) {
				Entry<String, String> sql = sqlIterator.next();
				Callable<HashMap<String, String>> callable = new Request(sql.getValue(), sql.getKey(), executionSequence++, config);
				Future<HashMap<String, String>> future = (Future<HashMap<String, String>>)pool.submit(callable);
				set.add(future);
			}
			
			HashMap<String, String> result = null;
			
		    for (Future<HashMap<String, String>> future : set) {
		    		try {
			    		result = (HashMap<String, String>) future.get();
			    		resultsArrayList.add(result);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
		    	
				if(result.get("status").equals("error")){
					errorCount = errorCount + 1;
				} else {
					successCount = successCount + 1;
				}
		    }
			pool.shutdown();	
			
		} else {		
			while(sqlIterator.hasNext()) {
				Entry<String, String> sql = sqlIterator.next();
				Request request = new Request(sql.getValue(), sql.getKey(), executionSequence++, config);
				HashMap<String, String> result = request.executeSQL();
				resultsArrayList.add(result);
				if(result.get("status").equals("error")){
					errorCount = errorCount + 1;
				} else {
					successCount = successCount + 1;
				}
			}
		}
		String fileName = getReportFileName(config.isUseThreads(), config.getMaxThreadCount());
		writeResults(resultsArrayList, fileName);
		
		System.out.println("=========================================================");
		System.out.println("Total Success: " + successCount);
		System.out.println("Total Error: " + errorCount);
		System.out.println("Results Written To: " + fileName);
		
	}
	
	/**
	 * Writes results of processed SQL executions to a CSV file.
	 * @param results - The results from the SQL executions.
	 * @param fileName - The name of the file to write the results to.
	 */
	private static void writeResults(ArrayList<HashMap<String, String>> results, String fileName) {
		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		HashMap<String, String> result = null;
		
		try {
			fileOutputStream = new FileOutputStream(fileName);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			outputStreamWriter.write("SQL Filename, Status, User Name, Execution Sequence, Start Time, End Time, Elapsed Time, Stack Trace" + System.lineSeparator());
			Iterator<HashMap<String, String>> iterator = results.iterator();
			while(iterator.hasNext()){
				result = iterator.next();
				String resultString =  result.get("sqlFileName") + ","
						+ result.get("status") + ","
						+ result.get("userName") + ","
						+ result.get("executionSequence") + ","
						+ result.get("startTime") + ","
						+ result.get("endTime") + ","		
						+ result.get("elapsedTime") + ","		
						//+ result.get("sql") + ","
						+ "\" " + result.get("stackTrace") + "\"" +
						System.lineSeparator();
				outputStreamWriter.write(resultString);
			}
			outputStreamWriter.close();
			fileOutputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Report filenames have a specific nomenclature which indicates when the SQL report
	 * was created, if threads / concurrent users were used, and if thread were used the 
	 * maximum number of threads that was in place.
	 * @param useThreads - True if threads were used.  False otherwise.
	 * @param maxThreadCount - The maximum thread count that was in place at the time of execution.
	 * @return The report file name.
	 */
	private static String getReportFileName(boolean useThreads, int maxThreadCount) {
		String fileName = "";
		String processTypeIdentifier = "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyy_hhmmssa");
		String date = simpleDateFormat.format(System.currentTimeMillis());
		if(useThreads) {
			processTypeIdentifier = "Parallel_Threads-" + maxThreadCount;
		} else {
			processTypeIdentifier = "Serial";
		}
		fileName = "Results_" + processTypeIdentifier + "_" + date + ".csv";
		return fileName;
	}
	
	/**
	 * Get the SQL statements from the files within the configured SQL directory.
	 * @param sqlDirectory - The directory where the SQL files are located.
	 * @return - SQL statements.
	 */
	private static HashMap<String, String> getSqlStatements(String sqlDirectory) {
		HashMap<String, String> sqlStamements = new HashMap<String, String>();
		SQLBuilder sqlBuilder = new SQLBuilder();
		File folder = new File(sqlDirectory);
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles == null) {
			System.out.println("*****  DIRECTORY NOT FOUND!  *****");
			System.out.println("The directory \"" + sqlDirectory + "\" was not found.  Edit the file config.props to correct.");
			System.exit(-1);
		}
		for(File file : listOfFiles) {
			if(file.isFile()) {
				sqlStamements.put(file.getName(), sqlBuilder.readSQLFromFile(sqlDirectory + "/" + file.getName()));
			} else if(file.isDirectory()) {
				System.out.println("Found a Directory: " + file.getName());
			}
		}
		return sqlStamements;
	}
}
