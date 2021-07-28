package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class TwitterService implements Service{

  private CrdDao dao;

  @Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  @Override
  public Tweet postTweet(Tweet tweet) {
    validatePostTweet(tweet);
    return  (Tweet) dao.create(tweet);
  }


  @Override
  public Tweet showTweet(String id, String[] fields) {
    validateId(id);
    return  (Tweet) dao.findById(id);
  }

  @Override
  public List<Tweet> deleteTweets(String[] ids) {

    Arrays.stream(ids).forEach(s -> validateId(s));
    List<Tweet> deletedList = new ArrayList<>();
    Arrays.stream(ids).forEach(s -> deletedList.add((Tweet) dao.deleteById(s)));
    return deletedList;
  }

  private void validatePostTweet(Tweet tweet) {
    String errorStatement = "";
    double lon = tweet.getCoordinates().getCoordinates().get(0);
    double lat = tweet.getCoordinates().getCoordinates().get(1);

    if (tweet.getText().length() > 140)
      errorStatement = "ERROR: Tweet text exceeds 140 characters";
    else if (lon >180 || lon < -180 )
      errorStatement = "ERROR: Longitude not in the range [-180,180]";
    else if (lat >90 || lat < -90 )
      errorStatement = "ERROR: Latitude not in the range [-90,90]";

    if(!errorStatement.isEmpty())
      throw new IllegalArgumentException(errorStatement);
  }

  private void validateId(String id) {
    try {
      Long.parseLong(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("ERROR: Invalid tweet id " + id);
    }
  }
}
