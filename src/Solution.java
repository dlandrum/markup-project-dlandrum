//This file written by Don Landrum on October 28th, 2017

import java.io.LineNumberReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Solution {
  public static final int THREAD_COUNT = 4;
  //thread count can easily be manipulated based on the number of cores on the machine
  public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  public static final String DB_URL = "jdbc:mysql://localhost/dldb";
  public static void main(String[] args) {
    String fileName = args[0];
    String userName = args[1];
    String passWord = args[2];
    int firstUnderscore = fileName.indexOf('_');
    String fileId = fileName.substring(0,firstUnderscore);
    int lastSlash = fileId.lastIndexOf('/');
    if (lastSlash != -1) { //there is a slash
      fileId = fileId.substring(lastSlash+1);
    }
    String fileDate = fileName.substring(firstUnderscore+1,fileName.lastIndexOf('.'));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
    Date d;
    long fileTime = 0;
    try {
      d = sdf.parse(fileDate); //create Date object
      fileTime = d.getTime(); //convert it to a long
    }
    catch (Exception e) {
      System.out.println("Date unparseable");
    }
    try {
      LineNumberReader lnr = new LineNumberReader(new FileReader(fileName));
      long charsInFile = lnr.skip(Long.MAX_VALUE); //returns number of chars in the file
      if (charsInFile == Long.MAX_VALUE) {
        System.out.println("Too many chars in file to process");
      }
      int lastLine = lnr.getLineNumber();
      lnr.close();
      long offset = charsInFile/(THREAD_COUNT); //number of chars each thread should handle
      lnr = new LineNumberReader(new FileReader(fileName));
      MarkupThread[] threads = new MarkupThread[THREAD_COUNT];
      for (int i = 0; i < THREAD_COUNT; ++i) {
        lnr.skip(offset); //skip to the starting position
        threads[i] = new MarkupThread(fileName, i*offset, lnr.getLineNumber(), i);
        threads[i].start(); //begin the new thread
      }
      lnr.close();
      int score = 0;
      for (int i = 0; i < threads.length; ++i) {
        threads[i].join(); //wait for thread to finish
        score += threads[i].getScore(); //add its score to the final total
      }
      //put all of this information in the database
      Class.forName(JDBC_DRIVER);
      Connection conn = DriverManager.getConnection(DB_URL, userName, passWord);
      String query = "INSERT INTO markup(id,timestamp,date,score) VALUES (?,?,?,?)";
      PreparedStatement prepStmt = conn.prepareStatement(query);
      prepStmt.setString(1, fileId);
      prepStmt.setLong(2, fileTime);
      prepStmt.setString(3, fileDate);
      prepStmt.setInt(4, score);
      prepStmt.execute();
      conn.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
