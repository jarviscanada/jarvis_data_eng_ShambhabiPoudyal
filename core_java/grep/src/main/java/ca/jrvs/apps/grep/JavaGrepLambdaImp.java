package ca.jrvs.apps.grep;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import org.apache.log4j.BasicConfigurator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp{

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //use default logger config
    BasicConfigurator.configure();

    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {
      javaGrepLambdaImp.process();
    } catch (Exception ex) {
      javaGrepLambdaImp.logger.error("ERROR: JavaGrepImp process failed", ex);
    }
  }

  @Override
  public List<String> readLines(File inputFile) {
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException("ERROR: inputFile is not a file");
    }
    List<String> lines = new ArrayList<>();

    Path path = Paths.get(inputFile.getPath());
    try (Stream<String> stream = Files.lines(path)) {
      lines = stream.collect(Collectors.toList());
    } catch (Exception ex) {
      logger.error("ERROR: Read lines from file failed", ex);
    }
    return lines;
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> listFiles = new ArrayList<>();
    try {
      listFiles = Files.list(Paths.get(rootDir))
          .map(Path::toFile)
          .collect(Collectors.toList());
    } catch (Exception ex) {
      logger.error("ERROR: List files from directory failed", ex);
      throw new RuntimeException(ex);
    }
    return listFiles;
  }
}