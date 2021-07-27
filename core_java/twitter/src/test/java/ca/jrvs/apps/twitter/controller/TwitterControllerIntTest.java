package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterControllerIntTest {

  static final Logger logger = LoggerFactory.getLogger(TwitterControllerIntTest.class);
  private TwitterController controller;


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

    //pass dependency
    CrdDao dao = new TwitterDao(httpHelper);
    Service service= new TwitterService(dao);
    controller = new TwitterController(service);
  }



  @Test
  public void postTweet() {
    String[] args = new String[]{"post", "testing post" + System.currentTimeMillis(), "10:20"};

    Tweet testTweet = controller.postTweet(args);

    assertEquals(args[1], testTweet.getText());
    assertNotNull(testTweet.getCoordinates());
    assertEquals(2, testTweet.getCoordinates().getCoordinates().size());
  }

  @Test
  public void showTweet() {
    //test post
    String[] argsPost = new String[]{"post", "testing post" + System.currentTimeMillis(), "10:20"};
    Tweet testTweet = controller.postTweet(argsPost);

    String[] argsShow = new String[]{"show", testTweet.getIdStr(), "id,idStr,text,reTweeted"};
    Tweet viewTweet = controller.showTweet(argsShow);

    assertNotNull(viewTweet.getCoordinates());
    assertEquals(2, viewTweet.getCoordinates().getCoordinates().size());
    assertEquals(testTweet.getId(), viewTweet.getId());
  }

  @Test
  public void deleteTweet() {
    //test post
    String[] argsPost = new String[]{"post", "testing post" + System.currentTimeMillis(), "10:20"};
    Tweet testTweet = controller.postTweet(argsPost);

    String[] argsDelete = new String[]{"delete", testTweet.getIdStr()};
    List<Tweet> tweets = controller.deleteTweet(argsDelete);

    assertEquals(testTweet, tweets.get(0));
  }
}