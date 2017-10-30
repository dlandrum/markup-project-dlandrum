This folder contains the vast majority of the important code in this project.
The three java files contain the code that moves the project along.
MarkupProject.java is a thread class that processes a certain portion of the html file and scores it.
Solution.java handles one file at a time, divides the work required among threads, and stores the generated score in the database.
Interaction.java runs after everything has been computed and provides interaction with the database and requested queries.
Furthermore, we have a makefile, which compiles the code, and the runCode.sh bash script, which runs our project.

Languages and Tools Used:
MySQL 5.7.20
Java JDK 1.8.0_144
Atom as my IDE/Text Editor
MacOS Sierra 10.12.6

Instructions:
1. Start a local SQL server (`mysql.server start` should work).
2. From within the src folder, type `./runCode.sh (username) (password)` where (username) and (password) are the credentials to mysql that you would like to use. If you are made uncomfortable by passing your password in as a parameter to the code, you could first create a new login by accessing mysql with root privileges (`mysql --user=root mysql` on my computer) and then typing the command `CREATE USER 'dummy'@'localhost' IDENTIFIED BY 'password';` followed by `GRANT ALL PRIVILEGES ON *.* TO 'dummy'@'localhost' WITH GRANT OPTION;`. You can now exit the mysql window and run the project using the command `./runCode.sh dummy password`.
3. Lastly, re-enter this same password when prompted, and after the files have been scored, follow the instructions on the screen to interact with the database.

Notes:
There is a global variable in the Solution.java class that determines the number of threads created. I have this number
currently set at 4, which is the number of cores that my computer has. Feel free to change it to fit your needs.
This bash script attempts to drop any old versions of the database before running. I named the database dldb (Don Landrum Data Base). If you happen to have a database already using this name, you will want to change 'dldb' to some other name throughout the code (this should only be hard-coded into the markup.sql schema, Solution.java, and Interaction.java).
