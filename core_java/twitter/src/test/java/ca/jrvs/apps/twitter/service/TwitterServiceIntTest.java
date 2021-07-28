package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
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

  @Test
  public void showTweet() {
    Tweet tweet = TweetUtil.buildTweet("test service show", -1d, 1d);
    Tweet testTweet = twitterService.postTweet(tweet);

    Tweet newTweet = twitterService.showTweet(testTweet.getIdStr(), new String[]{"id", "idStr", "text", "reTweeted"});

    assertEquals(testTweet.getId(), newTweet.getId());
  }

  @Test
  public void deleteTweet() {
    Tweet tweet = TweetUtil.buildTweet("test service delete", -1d, 1d);
    Tweet testTweet = twitterService.postTweet(tweet);

    String[] ids = new String[]{testTweet.getIdStr()};
    List<Tweet> newTweets = twitterService.deleteTweets(ids);

    assertEquals(testTweet.getId(), newTweets.get(0).getId());
  }
}