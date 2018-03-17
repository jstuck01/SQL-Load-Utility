# SQL-Load-Utility
The SQL Load Utility is a tool to execute SQL statements in batch and capture the corresponding times associated with each SQL execution process.  Both serial and parallel execution processes are supported.  Serial execution can be used to simply measure query processor performance.  Parallel execution allows for the simulation of multiple users executing SQL against a repository and measure the query times under multi-user workloads.



## Supported Databases
Currently, the SQL Load Utility supports Db2 based repositories such as the following:
* Db2
* Db2 Warehouse
* BigSQL

Both on premise and cloud based Db2 repositories are supported.

## Technical Details
** Measuring Query Times **
* Elapsed time is measured from the client machine
* Elapsed time starts when the SQL query is executed from the client.  Client connection overhead is NOT included in elapsed time as connection is made prior to the start of elapsed time
* Elapsed time ends when the client receives response from server

** Defining SQL Queries **
 * SQL queries are executed from files contained in a directory
 * Utility will look in the configured directory for SQL files
 * Only one SQL statement per file supported
 * SQL statements should not be terminated with ";"
 
 ** Query Response Time Process Reports **
 * Results are stored in a CSV file which can easily be opened with Excel for analysis
 * Report file names have the following naming conventions:
 	*Results_<Serial or Parallel>_<Threads Used>_<Date>_<Time>.csv*
 	*Examples:
 		*Results_Parellel_Threads-10_12072016_042940.csv*
 		*Results_Serial_12082016_111624.csv*
 
 
 ## Distribution and Packaging
 The SQL Load Utility is distributed as a ZIP file.  After decompressing the ZIP file, you will find the following:
 * config.props 
 	- Required configuration file
 * 


## Getting Started
Pull the repository and execute the ant build script.  This will result in a ZIP file being created in the "dist" directory.  Expand the ZIP file.


 