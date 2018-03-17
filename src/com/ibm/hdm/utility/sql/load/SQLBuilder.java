package com.ibm.hdm.utility.sql.load;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


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
 * SQLBuilder reads SQL from a file and constructs a string that represents the SQL. 
 * 
 * @author Jeff Tuck
 */

public class SQLBuilder {

	/**
	 * Default Constructor
	 */
	public SQLBuilder() {
		super();
	}
	
	/**
	 * Reads lines of SQL from a file and constructs a string that contains the SQL. 
	 * @param fileName - The name of the file to read SQL from.
	 * @return A string that contains the SQL.
	 */
	protected String readSQLFromFile(String fileName) {
		String sql = "";
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = in.readLine()) != null) {
				sql = sql + line + System.lineSeparator();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sql;
	}
}
