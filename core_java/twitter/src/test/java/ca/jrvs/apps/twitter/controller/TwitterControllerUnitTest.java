package ca.jrvs.apps.twitter.controller;

import static ca.jrvs.apps.twitter.util.TweetUtil.buildTweet;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.StringUtils;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  @Mock
  Service service;

  @InjectMocks
  TwitterController controller;

  @Test
  public void postTweet() {
    when(service.postTweet(any())).thenReturn(new Tweet());
    Tweet tweet = controller.postTweet(new String[]{"post", "test", "10:20"});

    assertNotNull(tweet);
  }

  @Test
  public void showTweet() {
    when(service.showTweet(any(), any())).thenReturn(new Tweet());
    Tweet tweet = controller.showTweet(new String[]{"show", "12345", "id,idStr,text,reTweeted"});

    assertNotNull(tweet);
  }

  @Test
  public void deleteTweet() {
    when(service.deleteTweets(any())).thenReturn(Arrays.asList(new Tweet(), new Tweet()));
    List<Tweet> tweets = controller.deleteTweet(new String[]{"delete", "12345, 23456"});

    assertNotNull(tweets.get(0));
    assertNotNull(tweets.get(1));
  }
}