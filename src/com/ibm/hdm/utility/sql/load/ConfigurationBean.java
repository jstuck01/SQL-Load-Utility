package com.ibm.hdm.utility.sql.load;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
 * Class which represents the configuration of the application
 * as defined in the configuration properties file. 
 * 
 * @author Jeff Tuck
 */

public class ConfigurationBean {
	
	//Config Variables
	private String jdbcDriver = "";
	private String hostName = "";
	private String port = "";
	private boolean useSSL = false;
	private String dbName = "";
	private String dbUserName = "";
	private String dbPassword = "";
	private boolean useThreads = false;
	private int maxThreadCount = 10;
	private String sqlDirectory = "";

	
	/**
	 * Default constructor 
	 */
	public ConfigurationBean() {
		super();
		init();
	}
	
	/**
	 * Initialize variables with values from properties file.
	 */
	private void init() {
		Properties props = new Properties();
		try {
			InputStream inputStream = new FileInputStream("config.props");  
			props.load(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.jdbcDriver = props.getProperty("jdbc_driver");
		this.hostName = props.getProperty("database_host_name");
		this.port = props.getProperty("database_port");
		this.useSSL = Boolean.parseBoolean(props.getProperty("use_ssl_connection"));
		this.dbName = props.getProperty("database_name");
		this.dbUserName = props.getProperty("database_username");
		this.dbPassword = props.getProperty("database_password");
		this.useThreads = Boolean.parseBoolean(props.getProperty("parallel_execution"));
		this.maxThreadCount = Integer.parseInt(props.getProperty("parallel_execution_max_connections"));
		this.sqlDirectory = props.getProperty("sql_file_directory");
	}
	
	
	/**
	 * Get the fully qualified class name of the configured JDBC driver.
	 * @return JDBC class name.
	 */
	protected String getJdbcDriver() {
		return jdbcDriver;
	}

	
	/**
	 * Sets the JDBC driver class name.
	 * @param jdbcDriver is the fully qualified class name of the JDBC driver.
	 */
	protected void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	/**
	 * Get the host name for the database to be used in the connection.
	 * @return Database host name.
	 */
	protected String getHostName() {
		return hostName;
	}

	/**
	 * Sets the host name of the database to connect to.
	 * @param hostName of the database to connect to.
	 */
	protected void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Get the port number for the configured database.
	 * @return the port number.
	 */
	protected String getPort() {
		return port;
	}

	/**
	 * Set the port for the configured database.
	 * @param port The port number of the database.
	 */
	protected void setPort(String port) {
		this.port = port;
	}

	/**
	 * Gets the name of the configured database.
	 * @return The database name.
	 */
	protected String getDbName() {
		return dbName;
	}

	/**
	 * Sets the configured database name.
	 * @param dbName The name of the database.
	 */
	protected void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * Gets the configured database user name.
	 * @return The database user name.
	 */
	protected String getDbUserName() {
		return dbUserName;
	}

	/**
	 * Sets the database use name.
	 * @param dbUserName The database user name.
	 */
	void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	/**
	 * Gets the configured database user password.
	 * @return The database user password.
	 */
	protected String getDbPassword() {
		return dbPassword;
	}

	/**
	 * Sets the database use password.
	 * @param dbPassword The database user password.
	 */
	protected void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	/**
	 * Indicates if the configuration is set to used threads or not.  Threads are used
	 * to simulate concurrent users.
	 * @return True if using threads.  False if not using threads.
	 */
	protected boolean isUseThreads() {
		return useThreads;
	}

	/**
	 * Set configuration to use threads or not.
	 * @param useThreads True to use threads.  False to not use threads.
	 */
	protected void setUseThreads(boolean useThreads) {
		this.useThreads = useThreads;
	}

	/**
	 * The maximum number of threads to use.
	 * @return The maximum number of threads to be used.
	 */
	protected int getMaxThreadCount() {
		return maxThreadCount;
	}

	/**
	 * Sets the maximum number of threads to be used.
	 * @param maxThreadCount The maximum number of threads that will be used.
	 */
	protected void setMaxThreadCount(int maxThreadCount) {
		this.maxThreadCount = maxThreadCount;
	}

	/**
	 * Gets the directory where SQL files are located.
	 * @return The directory location where SQL files are located.
	 */
	protected String getSqlDirectory() {
		return sqlDirectory;
	}

	/**
	 * Sets the directory where SQL files are located.
	 * @param sqlDirectory The relative (to utility execution directory) or absolute path where 
	 * the SQL files are located.  
	 */
	protected void setSqlDirectory(String sqlDirectory) {
		this.sqlDirectory = sqlDirectory;
	}

	/**
	 * Indicates if SSL / TLS based connections should be used.
	 * @return True if SSL / TLS will be used.  False if no SSL / TLS is required. 
	 */
	protected boolean isUseSSL() {
		return useSSL;
	}

	/**
	 * Set the configuration to use SSL / TLS.
	 * @param useSSL True to use SSL / TLS.  False to use clear text.
	 */
	protected void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}
}
