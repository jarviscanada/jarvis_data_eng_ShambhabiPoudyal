package ca.jrvs.apps.grep;

import java.io.*;
import java.util.*;

public interface JavaGrep {

  /**
   * Top level search workflow
   * @throws IOException
*/
  void process() throws IOException;

  /**
   * Traverse a given root directory and return all files
   * @param rootDir input directory
   * @return files under the rootDir
   */
  List<File> listFiles(String rootDir);

  /**
   * read a file and return all the lines
   * FileReader, BufferedReader
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalArgumentException if inputFile is not a file
   */
  List<String> readLines(File inputFile) throws IOException;

  /**
   * check if a line contains regex pattern (passed by user)
   * @param line input string
   * @return true is there is a match
   */
  boolean containsPattern(String line);

  /**
   * write lines to a file
   * FileOutputStream, OutputStreamWriter, BufferedWriter
   * @param lines matched lines
   * @throws IOException if write failed
   */
  void writeToFile(List<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);
}