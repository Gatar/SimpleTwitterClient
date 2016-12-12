package com.gatar.Service;

import com.gatar.Domain.TweetDTO;
import java.util.List;

/**
 * Interface used by controller class with required methods.
 */
public interface WebTwitterServiceInt {
    /**
     * Get list of tweets as {@link TweetDTO} objects after filtering.
     * @param username name of profile to show it's timeline
     * @return list of tweets
     */
     List<TweetDTO> getTweets(String username);

    /**
     * Get list of filter words.
     * @return filter words as list of Strings
     */
     String getFilterWords();

    /**
     *
     * @return get last shown username
     */
     String getLastUsedUsername();

    /**
     * Add to memory new filter word
     * @param word new word to usa as filter
     */
     void addFilterWord(String word);

     void clearFilters();
    
}


