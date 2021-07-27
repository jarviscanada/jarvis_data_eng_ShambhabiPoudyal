package ca.jrvs.apps.twitter.controller;

import static ca.jrvs.apps.twitter.util.TweetUtil.buildTweet;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class TwitterController implements Controller {

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private Service service;

  @Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  @Override
  public Tweet postTweet(String[] args) {
    if(args.length != 3) {
      throw new IllegalArgumentException("ERROR: Usage TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    String tweetText = args[1];
    String coord = args[2];
    String[] coordArray = coord.split(COORD_SEP);
    if(coordArray.length !=2 || StringUtils.isEmpty(tweetText)) {
      throw  new IllegalArgumentException("ERROR: Invalid location format "
          + "\n Usage: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"");
    }

    Double lat = null;
    Double lon = null;
    try {
      lat = Double.parseDouble(coordArray[0]);
      lon = Double.parseDouble(coordArray[1]);
    } catch (Exception e) {
      throw new IllegalArgumentException("ERROR: Invalid location format "
          + "\n Usage: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"", e);
    }

    Tweet newTweet = buildTweet(tweetText, lon, lat);
    return service.postTweet(newTweet);
  }

  @Override
  public Tweet showTweet(String[] args) {
    if(args.length != 3) {
      throw new IllegalArgumentException("ERROR: Usage TwitterCLIApp show \"tweet_id\" [field1,field2,...]");
    }

    String tweetId = args[1];
    String fields = args[2];
    String[] fieldsArray = fields.split(COMMA);

    return service.showTweet(tweetId, fieldsArray);
  }

  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if(args.length != 2) {
      throw new IllegalArgumentException("ERROR: Usage TwitterCLIApp delete [id1,id2,...]");
    }

    String ids = args[1];
    String[] idsArray = ids.split(COMMA);

    return service.deleteTweets(idsArray);
  }
}
