package ca.jrvs.apps.practice;

import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

  public static void main(String[] args) {
    RegexExcImp regexExcImp = new RegexExcImp();

    String filename1 = "testRegex1.jpeg";
    String filename2 = "testRegex2.JPG";
    String filename3 = "testRegex3.png";

    System.out.println(regexExcImp.matchJpeg(filename1));
    System.out.println(regexExcImp.matchJpeg(filename2));
    System.out.println(regexExcImp.matchJpeg(filename3));

    String ip1 = "255.255.255.255";
    String ip2 = "999.999.999.999";
    String ip3 = "192.168.0.25";

    System.out.println(regexExcImp.matchIp(ip1));
    System.out.println(regexExcImp.matchIp(ip2));
    System.out.println(regexExcImp.matchIp(ip3));

    String line1 = "";
    String line2 = " ";
    String line3 = "\t";

    System.out.println(regexExcImp.isEmptyLine(line1));
    System.out.println(regexExcImp.isEmptyLine(line2));
    System.out.println(regexExcImp.isEmptyLine(line3));
  }

  @Override
  public boolean matchJpeg(String filename) {
    String regex = ".*\\.(jpeg|jpg)";
    return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(filename).matches();
  }

  @Override
  public boolean matchIp(String ip) {
    String regex= "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
    return Pattern.compile(regex).matcher(ip).matches();
  }

  @Override
  public boolean isEmptyLine(String line) {
    String regex= "^\\s*$";
    return Pattern.compile(regex).matcher(line).matches();
  }
}