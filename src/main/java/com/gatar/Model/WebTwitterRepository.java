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

@Repository
public class WebTwitterRepository {

    private Twitter twitter;

    @Inject
    public WebTwitterRepository(Twitter twitter) {
        this.twitter = twitter;
    }

    public List<Tweet> getUserTimeline(String username){
        List<Tweet> tweetList = new ArrayList<>();;
        try{
            tweetList = twitter.timelineOperations().getUserTimeline(username);
        }catch(ResourceNotFoundException e){
            tweetList.add(new Tweet(0," ****Chosen profile doesn't exist!****",new Date(System.currentTimeMillis()),null,null,null,0,null,null));
        }catch (NotAuthorizedException e){
            tweetList.add(new Tweet(0," ****Profile access unauthorized****",new Date(System.currentTimeMillis()),null,null,null,0,null,null));
        }
        return tweetList;
    }
}
