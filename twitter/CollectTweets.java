package twitter;

import java.sql.Connection;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import util.DB;
import util.Util;


/**
 * @author Brian
 *
 * This code uses a keyword to search for relevant tweets and store in the database for analysis or training.
 */
public class CollectTweets {
	static int tweetCount = 0;
	static Connection con = DB.getConnection();
	
    public static void main(String[] args) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("");
        cb.setOAuthConsumerSecret("");
        cb.setOAuthAccessToken("");
        cb.setOAuthAccessTokenSecret("");

        
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        
        StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStatus(Status status) {
            	User user = status.getUser();
                
            	// Print to console:
            	tweetCount++;
                System.out.println("Tweet No. " + tweetCount);
                System.out.println("Date: " + status.getCreatedAt());
                String username = status.getUser().getScreenName();
                System.out.println(username);
                String profileLocation = user.getLocation();
                System.out.println(profileLocation);
                long tweetId = status.getId(); 
                System.out.println(tweetId);
                String content = status.getText();
                System.out.println(content +"\n");

                // Write data to file:
                Util.writeToFile("Tweet No. " + tweetCount + status.getUser().getScreenName() + " - " + status.getText() + " -> "+ status.getCreatedAt() +"\n");
                
                // Commit to database:
                try {
					DB.insertTweet(con, status.getText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }

            @Override
            public void onTrackLimitationNotice(int arg0) {
                // TODO Auto-generated method stub

            }

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}

        };
        
        FilterQuery fq = new FilterQuery();
    
        String keywords[] = {"tiger woods"}; // Hard coded for an example, but JSP is used to pass this argument.

        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);  
    }
}
