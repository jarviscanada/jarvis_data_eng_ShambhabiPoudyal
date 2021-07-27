package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

  private TwitterDao dao;

  @Before
  public void setup() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

    //set up dependency
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);

    //pass dependency
    this.dao = new TwitterDao(httpHelper);
  }

  @Test
  public void create() throws Exception {
    String hashTag = "#abc";
    String text = "@someone sometext " + hashTag + " " + System.currentTimeMillis();
    Double lat = 1d;
    Double lon = -1d;
    Tweet postTweet = TweetUtil.buildTweet(text, lon, lat);
    
    Tweet tweet = dao.create(postTweet);

    assertEquals(text, tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getCoordinates().size());
    assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));

    assertTrue(hashTag.contains(tweet.getEntities().getHashtags().get(0).getText()));
  }

  @Test
  public void findById() throws Exception{
    String testText = "Testing text for findById";
    Tweet tweet = dao.create(TweetUtil.buildTweet(testText, null, null));

    Tweet newTweet = dao.findById(tweet.getIdStr());

    assertEquals(testText, newTweet.getText());
    assertNull(newTweet.getCoordinates());
  }

  @Test
  public void deleteById() throws Exception {
    String testText = "Testing text for deleteById";
    Tweet tweet = dao.create(TweetUtil.buildTweet(testText, null, null));

    Tweet deletedTweet = dao.deleteById(tweet.getIdStr());

    assertEquals(testText, deletedTweet.getText());
  }
}
