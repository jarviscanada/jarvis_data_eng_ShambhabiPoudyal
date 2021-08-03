package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {
  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterService service;

  @Test
  public void postTweet() {
    when(dao.create(any())).thenReturn(new Tweet());
    service.postTweet(TweetUtil.buildTweet("test", 50.0, 0.0));
  }

  @Test
  public void showTweet() {
    when(dao.findById(any())).thenReturn(new Tweet());
    service.showTweet("12345", new String[]{"id", "idStr", "text", "reTweeted"});
  }

  @Test
  public void deleteTweet() {
    when(dao.deleteById(any())).thenReturn(new Tweet());
    service.deleteTweets(new String[]{"12345", "2345", "3456"});
  }
}