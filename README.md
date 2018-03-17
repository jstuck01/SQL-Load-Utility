# SQL-Load-Utility
The SQL Load Utility is a Db2 tool to execute SQL statements in batch and capture the corresponding times associated with each SQL execution process.  Both serial and parallel execution processes are supported.  Serial execution can be used to simply measure query processor performance.  Parallel execution allows for the simulation of multiple users executing SQL against a repository and measure the query times under multi-user workloads.



## Supported Databases
Currently, the SQL Load Utility supports Db2 based repositories such as the following:
* Db2
* Db2 Warehouse
* BigSQL

Both on premise and cloud based Db2 repositories are supported.


## Technical Details
**Measuring Query Times**
* Elapsed time is measured from the client machine
* Elapsed time starts when the SQL query is executed from the client.  
	* Client connection overhead is NOT included in elapsed time as connection is made prior to the start of elapsed time
* Elapsed time ends when the client receives response from server

**Defining SQL Queries**
 * SQL queries are executed from files contained in a directory
 * Utility will look in the configured directory for SQL files
 * Only one SQL statement per file supported
 * SQL statements should not be terminated with ";"
 
 **Query Response Time Process Reports**
 * Results are stored in a CSV file which can easily be opened with Excel for analysis
 * Report file names have the following naming conventions:

   		*Results_<Serial or Parallel>_<Threads Used>_<Date>_<Time>.csv*
 	
 * Examples of report names after SQL execution:
 
 		*Results_Parellel_Threads-10_12072016_042940.csv*
 	
 		*Results_Serial_12082016_111624.csv*

## Getting Started
Pull the repository and execute the ant build script.  This will result in a ZIP file being created in the "dist" directory.  Expand the ZIP file.  After expanding the ZIP file, fill out the configuration file (see below) and execute the shell script appropriate for your platform. 

 
## Distribution and Packaging
 The SQL Load Utility is distributed as a ZIP file.  After you decompress the ZIP file, you will find the following:
 * config.props (Required configuration file)
 * SQL_Load_Utility.jar (Executable jar file)
 * run.sh (bash shell script)
 * run.bat (Windows batch file) 

## Configuration
Before executing the SQL Load Utility, a configuration needs to be completed.  Below is a table that defines the configuration file (config.props) parameters.

Config Variable | Example Value | Description | Required
----------------|---------------|-------------|----------
jdbc_driver | com.ibm.db2.jcc.DB2Driver | JDBC Driver Class Name. | YES  
database_host_name | myDb2Warehouse.ibmcloud.com | The host name of the database. | YES
database_port | 50000 | The service port number of the database. | YES
database_name | BLUDB | The name of the database. | YES
database_username | myUserName | The user name to use when connecting to the database. | YES
database_password | myUserPassword | The password of the user connecting to the database. | YES
use_ssl_connection | true | TRUE will use SSL / TLS.  False will use clear text. | YES
parallel_execution | false | False is serial execution.  True is parallel execution. | YES
parallel_execution_max_connections | 5 | The number that represents the MAXIMUM number of threads that will be used | YES
sql_file_directory | SQL-DIRECTORY | Directory name where SQL files are located.  Absolute or relative path.  The relative path root is the directory from which the utility was executed. | YES

  
 	




 