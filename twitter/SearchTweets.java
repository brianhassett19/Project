package twitter;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import util.DB;
import util.Util;

/**
 * @author Brian
 * 
 * This code uses a keyword to search for relevant tweets for analysis. */
public class SearchTweets
{
    public static void main(String[] args) throws Exception {   	

    	String key; // Search term
    	
    	/* Standard input used for an example here, JSP used to retrieve this argument in final version	*/
    	Scanner userInput = new Scanner(System.in);
    	System.out.println("Please enter search term:");
    	key = userInput.nextLine();
    	int tweetCount = 0;
    	
    	/* Configure using OAuth details and create instance of Twitter class:				*/
    	ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("");
        cb.setOAuthConsumerSecret("");
        cb.setOAuthAccessToken("");
        cb.setOAuthAccessTokenSecret("");        
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();


        /* Use the RateLimitStatus class to monitor the API call limits: 		*/
        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets"); // finds the rate limit specifically for doing the search API call we use in this program

        // Print API call limit details:
        System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds\n",
        		searchTweetsRateLimit.getRemaining(),
        		searchTweetsRateLimit.getLimit(),
        		searchTweetsRateLimit.getSecondsUntilReset());
        
        
        /* Connect to DB and write search results:		*/
        Connection con = DB.getConnection();
        
        try {
        	Query query = new Query(key);
        	//query.setCount(25);
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                	/* Print to console, write to file, or commit to database as required:			*/
                	System.out.println("Tweet no." + ++tweetCount + " - " + tweet.getText() );
                	Util.writeToFile("Tweet no." + ++tweetCount + "@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + " - Date: " + tweet.getCreatedAt());
                	String tweetText = tweet.getText();		// For writing to DB, only save tweet text:
                    DB.insertTweet(con, tweetText);
                }
            } while ((query = result.nextQuery()) != null);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }
}
