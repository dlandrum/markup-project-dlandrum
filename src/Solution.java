import java.io.LineNumberReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Solution {
  public static final int THREAD_COUNT = 4;
  public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  public static final String DB_URL = "jdbc:mysql://localhost/mydb";
  public static final String USER = "dummyuser";
  public static final String PASS = "dummypass";
  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    String fileName = args[0];
    int firstUnderscore = fileName.indexOf('_');
    String fileId = fileName.substring(0,firstUnderscore);
    int lastSlash = fileId.lastIndexOf('/');
    if (lastSlash != -1) {
      fileId = fileId.substring(lastSlash+1);
    }
    String fileDate = fileName.substring(firstUnderscore+1,fileName.lastIndexOf('.'));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
    Date d;
    long fileTime = 0;
    try {
      d = sdf.parse(fileDate);
      fileTime = d.getTime();
    }
    catch (Exception e) {
      System.out.println("Date unparseable");
    }
    try {
      LineNumberReader lnr = new LineNumberReader(new FileReader(fileName));
      long charsInFile = lnr.skip(Long.MAX_VALUE);
      if (charsInFile == Long.MAX_VALUE) {
        System.out.println("Too many chars in file to process");
      }
      int lastLine = lnr.getLineNumber();
      lnr.close();
      long offset = charsInFile/(THREAD_COUNT);
      lnr = new LineNumberReader(new FileReader(fileName));
      MarkupThread[] threads = new MarkupThread[THREAD_COUNT];
      for (int i = 0; i < THREAD_COUNT; ++i) {
        lnr.skip(offset);
        threads[i] = new MarkupThread(fileName, i*offset, lnr.getLineNumber(), i);
        threads[i].start();
      }
      lnr.close();
      int score = 0;
      for (int i = 0; i < threads.length; ++i) {
        threads[i].join();
        score += threads[i].getScore();
      }
//put it in the database
      Class.forName(JDBC_DRIVER);
      Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String query = "INSERT INTO markup(id,timestamp,score) VALUES (?,?,?)";
      PreparedStatement prepStmt = conn.prepareStatement(query);
      prepStmt.setString(1, fileId);
      prepStmt.setLong(2, fileTime);
      prepStmt.setInt(3, score);
      prepStmt.execute();
      conn.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
