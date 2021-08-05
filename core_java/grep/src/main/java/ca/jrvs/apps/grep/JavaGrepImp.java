package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.apache.log4j.BasicConfigurator;

public class JavaGrepImp implements JavaGrep {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;



  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();
    for (File file : listFiles(getRootPath())) {
      for (String line : readLines(file)) {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);

  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> listFiles = new ArrayList<>();
    File root = new File(rootDir);
    if (root.listFiles() != null) {
      listFilesRecursively(root, listFiles);
    }
    return listFiles;
  }

  public void listFilesRecursively(File root, List<File> listFiles) {
    File[] listOfFiles = root.listFiles();
    if(listOfFiles == null) {
      throw new NullPointerException("ERROR: root contains no files");
    }
    for (File file : listOfFiles) {
        if (file.isDirectory()) {
          listFilesRecursively(file, listFiles);
        }
        else {
          listFiles.add(file);
        }
    }
  }

  @Override
  public List<String> readLines(File inputFile){
    if(!inputFile.isFile()) {
      throw new IllegalArgumentException("ERROR: inputFile is not a file");
    }
    List<String> lines = new ArrayList<>();

    // both BufferedReader and FileReader within try to catch both exceptions
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
      String line;
      while((line = bufferedReader.readLine()) != null) {
        lines.add(line);
      }
    } catch (Exception ex) {
      logger.error("ERROR: BufferedReader or FileReader failed", ex);
    }
    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    Pattern pattern = Pattern.compile(getRegex());
    Matcher matcher = pattern.matcher(line);

    return matcher.matches();
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    File outFile = new File(getOutFile());
    FileOutputStream fileOutStream = new FileOutputStream(outFile);

    try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutStream))) {
      for (String line : lines){
        bufferedWriter.write(line);
        bufferedWriter.newLine();
      }
    } catch (IOException ex) {
      throw new RuntimeException("ERROR: Write to outFile failed", ex);
    }
  }

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }
  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }


  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //use default logger config
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error("ERROR: JavaGrepImp process failed", ex);
    }
  }
}
