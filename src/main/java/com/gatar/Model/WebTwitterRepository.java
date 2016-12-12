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

    //Tweet with text information that chosen profile not exist
    private final Tweet PROFILE_NOT_EXIST = new Tweet(0," ****Chosen profile doesn't exist!****",new Date(System.currentTimeMillis()),null,null,null,0,null,null);

    //Tweet with text information that application doesn't have authorization to view the chosen user timeline
    private final Tweet ACCESS_TO_PROFILE_PROHIBITED = new Tweet(0," ****Access to profile needs authorization****",new Date(System.currentTimeMillis()),null,null,null,0,null,null);

    @Inject
    public WebTwitterRepository(Twitter twitter) {
        this.twitter = twitter;
    }

    public List<Tweet> getUserTimeline(String username){
        List<Tweet> tweetList = new ArrayList<>();;
        try{
            tweetList = twitter.timelineOperations().getUserTimeline(username);
        }catch(ResourceNotFoundException e){
            tweetList.add(PROFILE_NOT_EXIST);
        }catch (NotAuthorizedException e){
            tweetList.add(ACCESS_TO_PROFILE_PROHIBITED);
        }
        return tweetList;
    }
}
