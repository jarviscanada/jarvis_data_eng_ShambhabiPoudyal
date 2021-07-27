package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterServiceIntTest {

  static final Logger logger = LoggerFactory.getLogger(TwitterServiceIntTest.class);
  private TwitterService twitterService;

  @Before
  public void setUp() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

    //set up dependency
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    CrdDao dao = new TwitterDao(httpHelper);

    //pass dependency
    this.twitterService = new TwitterService(dao);
  }

  @Test
  public void postTweet() {
    Double lat = 1d;
    Double lon = -1d;
    Tweet tweet = TweetUtil.buildTweet("test service", lon, lat);

    Tweet newTweet = twitterService.postTweet(tweet);

    assertNotNull(newTweet.getCoordinates());
    assertEquals(2, newTweet.getCoordinates().getCoordinates().size());
    assertEquals(lon, newTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, newTweet.getCoordinates().getCoordinates().get(1));
  }
}