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

  List<String> readLines(File inputFile);


  boolean containsPattern(String line);

  void writeToFile(List<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);
}