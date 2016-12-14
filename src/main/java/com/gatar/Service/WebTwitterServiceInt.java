package com.gatar.Service;

import com.gatar.Domain.TweetDTO;
import com.gatar.Domain.Visitor;

import java.util.List;

/**
 * Interface used by controller class with required methods.
 */
public interface WebTwitterServiceInt {
    /**
     * Get list of tweets as {@link TweetDTO} objects after filtering.
     * @param visitor {@link Visitor} obejct containing as minimum username String
     * @return list of tweets
     */
     List<TweetDTO> getTweets(Visitor visitor);
}


