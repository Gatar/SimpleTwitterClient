package com.gatar.Model;

import org.springframework.social.NotAuthorizedException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
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

    public List<Tweet> getUserTimeline(String username){
         return twitter.timelineOperations().getUserTimeline(username);
    }
}
