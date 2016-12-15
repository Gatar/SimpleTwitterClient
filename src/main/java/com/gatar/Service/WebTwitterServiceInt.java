package com.gatar.Service;

import com.gatar.Models.TweetDTO;
import com.gatar.Models.User;

import java.util.List;

/**
 * Interface used by controller class with required methods.
 */
public interface WebTwitterServiceInt {
    /**
     * Get list of tweets as List of {@link TweetDTO} objects after filtering.
     * @param user {@link User} obejct containing as minimum username String
     * @return list of tweets
     */
     List<TweetDTO> getTweets(User user);
}


