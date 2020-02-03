package bstack;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


public class LogStream {
  private static final String fileRead = "samplelogfile.txt";
  public static long lastReadPosition;
  public static long lastModifiedTime;

  public static List<String> readFile(File file, Long fileLength) throws IOException {
    String line = null;
    List<String> response = new ArrayList<>();
    BufferedReader in = new BufferedReader(new java.io.FileReader(file));
    in.skip(fileLength);
    while ((line = in.readLine()) != null) {
      System.out.println(line);
      response.add(line);
    }
    in.close();
    return response;
  }

  public static String[] readLastNLinesFromAFile(int n) {
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(fileRead, "r")) {
      long lengthOfFile = randomAccessFile.length();
      long counterFromEnd = 1L;
      long newlineCounterGoal = n;
      int newlineCounter = 0;
      long tailPosition = 0L; // start of the end ;-)
      lastReadPosition = lengthOfFile;
      randomAccessFile.seek(lengthOfFile - 1L);
      char currentChar = (char) randomAccessFile.readByte();
      if (currentChar == '\n') {
        newlineCounterGoal++;
      }
      while (counterFromEnd <= lengthOfFile) {
        randomAccessFile.seek(lengthOfFile - counterFromEnd);
        if (randomAccessFile.readByte() == '\n') {
          newlineCounter++;
        }
        if (newlineCounter == newlineCounterGoal) {
          tailPosition = randomAccessFile.getFilePointer();
          break;
        }
        counterFromEnd++;
      }
      randomAccessFile.seek(tailPosition);
      String line;
      String[] lines = new String[n];
      int nLine = 0;
      while ((line = randomAccessFile.readLine()) != null) {
        lines[nLine++] = line;
      }
      return lines;


    } catch (IOException e) {
    }
    return null;
  }

  public List<String> tailFile() throws IOException {
    File file = new File(fileRead);
    lastModifiedTime = file.lastModified();
    List<String> res = new ArrayList<>();
    System.out.println(file.getAbsolutePath() + lastModifiedTime);
    if (file.exists() && file.canRead()) {
      long fileLength = file.length();
      while (true) {
        if (file.lastModified() > lastModifiedTime) {
          readFile(file, fileLength);
          fileLength = file.length();
          lastModifiedTime = file.lastModified();
        }
      }
    }
    return res;
  }

  public static void main(String args[]) throws Exception {
    File file = new File(fileRead);
    lastModifiedTime = file.lastModified();
    System.out.println(file.getAbsolutePath() + lastModifiedTime);
    if (file.exists() && file.canRead()) {
      long fileLength = file.length();
      String[] lastNLines = readLastNLinesFromAFile(10);
      for (String i : lastNLines) {
        System.out.println(i);
      }
      while (true) {
        if (file.lastModified() > lastModifiedTime) {
          readFile(file, fileLength);
          fileLength = file.length();
          lastModifiedTime = file.lastModified();
        }
      }
    }
  }
}
