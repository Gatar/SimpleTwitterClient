package com.gatar.Repositories;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

import java.util.List;

/**
 * Class for provide tweets list from selected source or return one tweet with error message.
 */
@Repository
public class WebTwitterRepository {

    private Twitter twitter;

    @Inject
    public WebTwitterRepository(Twitter twitter) {
        this.twitter = twitter;
    }

    /**
     * Return list of Tweets for specified username
     * @param username value of username
     * @return list with 20 Tweets
     */
    public List<Tweet> getUserTimeline(String username){
         return twitter.timelineOperations().getUserTimeline(username);
    }
}
