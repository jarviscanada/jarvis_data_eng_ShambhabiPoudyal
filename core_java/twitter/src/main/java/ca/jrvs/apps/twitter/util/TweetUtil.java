package ca.jrvs.apps.twitter.util;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;

public class TweetUtil {

  /**
   * Set text and coordinates for tweet
   * @param text
   * @param lat
   * @param lon
   * @return
   */
  public static Tweet buildTweet(String text, Double lon, Double lat) {
    Tweet tweet = new Tweet();
    tweet.setText(text);

    Coordinates coordinates = new Coordinates();
    List<Double> coordinateList  = new ArrayList<>();
    coordinateList.add(lon);
    coordinateList.add(lat);
    coordinates.setCoordinates(coordinateList);
    coordinates.setType("point");
    System.out.println(coordinates);
    if(lat!=null && lon!=null)
      tweet.setCoordinates(coordinates);
    return tweet;
  }

}
