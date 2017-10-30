import java.util.Scanner;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Interaction {
  public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  public static final String DB_URL = "jdbc:mysql://localhost/mydb";
  public static void main(String[] args) {
    String userName = args[0];
    String passWord = args[1];
    Scanner in = new Scanner(System.in);
    System.out.println("The scores have been calculated!");
    try {
      Class.forName(JDBC_DRIVER);
      Connection conn = DriverManager.getConnection(DB_URL, userName, passWord);
      boolean loop = true;
      while (loop) {
        System.out.println("Please type the integer corresponding to one of the following options:\n"+
          "1. Retrieve all scores for a unique ID\n"+
          "2. Retrieve all scores run in the system for a custom date range\n"+
          "3. Retrieve highest scored unique id\n"+
          "4. Retrieve lowest scored unique id\n"+
          "5. Find the average score for all runs\n"+
          "6. Exit");
        int input = in.nextInt();
        switch(input) {
          case 1:
            Statement stmt1 = conn.createStatement();
            in.nextLine();
            System.out.println("Please enter the ID");
            String id = in.nextLine();
            String sql1 = "SELECT score FROM markup WHERE id IN (\'"+id+"\')";
            ResultSet rs1 = stmt1.executeQuery(sql1);
            while(rs1.next()) {
              System.out.println(rs1.getInt("score"));
            }
            rs1.close();
            stmt1.close();
            break;
          case 2:
            Statement stmt2 = conn.createStatement();
            in.nextLine();
            System.out.println("Please enter the lower date limit in the format yyyy_mm_dd");
            String lower = in.nextLine();
            System.out.println("Please enter the upper date limit in the format yyyy_mm_dd");
            String upper = in.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
            Date l = sdf.parse(lower);
            Date u = sdf.parse(upper);
            String sql2 = "SELECT score FROM markup WHERE timestamp > "+l.getTime()+" AND timestamp < "+u.getTime();
            ResultSet rs2 = stmt2.executeQuery(sql2);
            while(rs2.next()) {
              System.out.println(rs2.getInt("score"));
            }
            rs2.close();
            stmt2.close();
            break;
          case 3:
            Statement stmt3 = conn.createStatement();
            String maxId = "";
            int maxScore = Integer.MIN_VALUE;
            String sql3 = "SELECT DISTINCT id FROM markup";
            ResultSet rs3 = stmt3.executeQuery(sql3);
            while (rs3.next()) {
              Statement stmt30 = conn.createStatement();
              String temp = rs3.getString("id");
              String sql30 = "SELECT AVG(score) FROM markup WHERE id IN (\'"+temp+"\')";
              ResultSet rs30 = stmt30.executeQuery(sql30);
              while(rs30.next()) {
                int avg = rs30.getInt("AVG(score)");
                if (avg > maxScore) {
                  maxScore = avg;
                  maxId = temp;
                }
              }
              stmt30.close();
              rs30.close();
            }
            rs3.close();
            stmt3.close();
            System.out.println(maxId);
            break;
          case 4:
            Statement stmt4 = conn.createStatement();
            String minId = "";
            int minScore = Integer.MAX_VALUE;
            String sql4 = "SELECT DISTINCT id FROM markup";
            ResultSet rs4 = stmt4.executeQuery(sql4);
            while (rs4.next()) {
              Statement stmt40 = conn.createStatement();
              String temp = rs4.getString("id");
              String sql40 = "SELECT AVG(score) FROM markup WHERE id IN (\'"+temp+"\')";
              ResultSet rs40 = stmt40.executeQuery(sql40);
              while(rs40.next()) {
                int avg = rs40.getInt("AVG(score)");
                if (avg < minScore) {
                  minScore = avg;
                  minId = temp;
                }
              }
              rs40.close();
              stmt40.close();
            }
            rs4.close();
            stmt4.close();
            System.out.println(minId);
            break;
          case 5:
            Statement stmt5 = conn.createStatement();
            String sql5 = "SELECT AVG(score) FROM markup";
            ResultSet rs5 = stmt5.executeQuery(sql5);
            while (rs5.next()) {
              System.out.println(rs5.getInt("AVG(score)"));
            }
            rs5.close();
            stmt5.close();
            break;
          default:
            loop = false;
        }
      }
      conn.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
