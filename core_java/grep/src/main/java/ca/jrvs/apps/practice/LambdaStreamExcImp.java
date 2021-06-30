package ca.jrvs.apps.practice;

import java.util.List;
import java.util.Arrays;
import java.lang.Integer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.regex.Pattern;

public class LambdaStreamExcImp implements LambdaStreamExc {

  @Override
  public Stream<String> createStrStream(String... strings) {
    Stream<String> streamString = Arrays.stream(strings);
    return streamString;
  }

  @Override
  public Stream<String> toUpperCase(String... strings) {
    Stream<String> inputStreamString= createStrStream(strings);
    return inputStreamString.map(str -> str.toUpperCase());
  }

  @Override
  public Stream<String> filter(Stream<String> stringStream, String pattern) {
    Pattern regexPattern = Pattern.compile(pattern);
    return stringStream.filter(str -> !str.contains(pattern));
  }

  @Override
  public IntStream createIntStream(int[] arr) {
    IntStream intStream = Arrays.stream(arr);
    return intStream;
  }

  @Override
  public <E> List<E> toList(Stream<E> stream) {
    List<E> list = stream.collect(Collectors.toList());
    return list;
  }

  //boxed to box each element of the stream to an integer
  @Override
  public List<Integer> toList(IntStream intStream) {
    List<Integer> intList = intStream.boxed().collect(Collectors.toList());
    return intList;
  }

  @Override
  public IntStream createIntStream(int start, int end) {
    IntStream intStream = IntStream.range(start, end+1);
    return intStream;
  }

  @Override
  public DoubleStream squareRootIntStream(IntStream intStream) {
    DoubleStream doubleStream = intStream.mapToDouble(val -> Math.sqrt(val));
    return doubleStream;
  }

  @Override
  public IntStream getOdd(IntStream intStream) {
    IntStream outIntStream = intStream.filter(val -> val%2 == 1);
    return outIntStream;
  };

  @Override
  public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
    Consumer<String> consumer = msg -> System.out.println(prefix + msg + suffix);
    return consumer;
  };

  @Override
  public void printMessages(String[] messages, Consumer<String> printer) {
    for (String msg : messages) {
      printer.accept(msg);
    }
  }

  @Override
  public void printOdd(IntStream intStream, Consumer<String> printer) {
    getOdd(intStream).forEach(val -> printer.accept(String.valueOf(val)));
  }

  @Override
  public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
    Stream<Integer> streamInt = ints.flatMap(val -> val.stream()).map(input -> input * input);
    return streamInt;
  }

  public static void main(String[] args) {
    LambdaStreamExc lse = new LambdaStreamExcImp();

    String[] stringsArray = {"This", "is", "a", "practice", "set"};
    int[] intArray = {10,  15, 20, 25, 30};
    Consumer<String> printer;

    //string operations
    lse.createStrStream(stringsArray).forEach(System.out::println);

    lse.toUpperCase(stringsArray).forEach(System.out::println);

    lse.filter(lse.createStrStream(stringsArray), "i").forEach(System.out::println);

    List<String > newStringList = lse.toList(lse.createStrStream(stringsArray));
    System.out.println(newStringList);

    lse.printMessages(stringsArray, lse.getLambdaPrinter("msg:", "!") );

    //integer operations
    lse.createIntStream(intArray).forEach(System.out::println);

    List<Integer> newIntList = lse.toList(lse.createIntStream(intArray));
    System.out.println(newIntList);

    lse.squareRootIntStream(lse.createIntStream(intArray)).forEach(System.out::println);

    lse.getOdd(lse.createIntStream(intArray)).forEach(System.out::println);

    printer = lse.getLambdaPrinter("start>", "<end");
    printer.accept("Message Body");

    printer = lse.getLambdaPrinter("to>", ".");
    printer.accept("Message Body");

    lse.printOdd(lse.createIntStream(intArray), lse.getLambdaPrinter("odd number:", "!"));

    List<List<Integer>> list = Arrays.asList(Arrays.asList(0, 5), Arrays.asList(1, 4, 2));
    Stream<List<Integer>> strList = list.stream();
    lse.flatNestedInt(strList).forEach(System.out::println);
  }

}