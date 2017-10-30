//This file written by Don Landrum on October 28th, 2017

import java.io.LineNumberReader;
import java.io.FileReader;

public class MarkupThread extends Thread {
  private String fileName;
  private long startPosition;
  private int endLine;
  private int score;
  private int name;
  public MarkupThread(String fileName, long startPosition, int endLine, int name) {
    this.fileName = fileName;
    this.startPosition = startPosition;
    this.endLine = endLine;
    this.score = 0;
    this.name = name;
  }
  public void run() { //runs when the thread is started
    try {
      LineNumberReader lnr = new LineNumberReader(new FileReader(this.fileName));
      lnr.skip(this.startPosition); //moves to the startPosition
      while (this.startPosition != 0) { //excludes the beginning of the file
        char c = (char) lnr.read(); //reads until the end of the line
        if (c == '\n' || c == '\r') {
          break;
        }
      }
      while (lnr.getLineNumber() < this.endLine+1) {
        //reads up to the endLine, inclusive
        String line = lnr.readLine();
        if (line == null) {
          break;
        }
        this.scoreString(line); //scores the line
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  private void scoreString(String s) {
    //assumes that we have been given valid HTML code
    int openIndex = s.indexOf('<');
    int closeIndex = s.indexOf('>');
    if (openIndex == -1) { //no tags
      return;
    }
    if (s.charAt(openIndex+1) != '/') { //open tag, actually score it
      String contents = s.substring(openIndex+1,closeIndex);
      int space = contents.indexOf(' ');
      if (space != -1) { //there is a space
        contents = contents.substring(0,space);
      }
      contents = contents.toLowerCase();
      //at this point, contents is the tag
      if (contents.contains("frame")) {
        this.score -= 5;
      }
      else if (contents.equals("tt") || contents.equals("big") || contents.equals("center"))  {
        this.score -= 2;
      }
      else if (contents.equals("strike") || contents.equals("font")) {
        this.score -= 1;
      }
      else if (contents.equals("p")) {
        this.score += 1;
      }
      else if (contents.equals("h2")) {
        this.score += 2;
      }
      else if (contents.equals("div") || contents.equals("h1")) {
        this.score += 3;
      }
      else if (contents.equals("html") || contents.equals("body")) {
        this.score += 5;
      }
      else if (contents.equals("header") || contents.equals("footer")) {
        this.score += 10;
      }
      else {
        System.out.println("Invalid tag name");
      }
    }
    if (s.length() > closeIndex+1) { //more stuff follows, keep trying to score it
      String sub = s.substring(closeIndex+1);
      scoreString(sub);
    }
  }
  public int getScore() { //return the score
    return this.score;
  }
}
