package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

  static final Logger logger = LoggerFactory.getLogger(TwitterDao.class);
  //URI Constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  @Override
  public Tweet create(Tweet entity) {
    //Construct URI
    URI uri;
    uri = getPostUri(entity);

    //Execute HTTP Request
    HttpResponse response = httpHelper.httpPost(uri);

    //Validate response and deserialize response to Tweet object
    return parseResponseBody(response, HTTP_OK);
  }

  /**
   * Check response status code Convert Response Entity to Tweet
   * @param response
   * @param expectedStatusCode
   * @return
   */
  public Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode) {
    Tweet tweet = null;

    //Check response status
    int status = response.getStatusLine().getStatusCode();
    if (status != expectedStatusCode) {
      try {
        logger.error(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        logger.error("ERROR: Response has no entity");
      }
      throw new RuntimeException("ERROR: Unexpected HTTP status:" + status);
    }

    if (response.getEntity() == null) {
      throw new RuntimeException("ERROR: Empty response body");
    }

    //Convert Response Entity to String
    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("ERROR: Failed to convert entity to String", e);
    }

    //Desec JSON string to Tweet object
    try {
      tweet = JsonUtil.toObjectFromJson(jsonStr, Tweet.class);
    } catch (IOException e) {
      throw new RuntimeException("ERROR: Unable to covert JSON string to Object", e);
    }
    return tweet;
  }

  private URI getPostUri(Tweet entity) {
    URI uri;

    try {
      if (entity.getCoordinates() != null) {
        uri = new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL
            + URLEncoder.encode(entity.getText(), StandardCharsets.UTF_8.toString())
            + AMPERSAND + "lat" + EQUAL + entity.getCoordinates().getCoordinates().get(1)
            + AMPERSAND + "long" + EQUAL + entity.getCoordinates().getCoordinates().get(0));
      }
      else {
        uri = new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL
            + URLEncoder.encode(entity.getText(), StandardCharsets.UTF_8.toString()));
      }

      return uri;
    } catch (URISyntaxException | UnsupportedEncodingException e) {
      throw new IllegalArgumentException("ERROR: Invalid URI input", e);
    }
  }

  @Override
  public Tweet findById(String s) {
    URI uri;
    try {
      uri = new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM  + "id" + EQUAL + s);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("ERROR: Invalid tweet input", e);
    }

    HttpResponse response = httpHelper.httpGet(uri);
    return parseResponseBody(response, HTTP_OK);
  }

  @Override
  public Tweet deleteById(String s) {
    URI uri;
    try {
      uri = new URI(API_BASE_URI + DELETE_PATH + "/" + s + ".json");
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("ERROR: Invalid tweet input", e);
    }

    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);
  }
}
