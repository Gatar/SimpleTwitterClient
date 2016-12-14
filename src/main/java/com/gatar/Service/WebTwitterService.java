package com.gatar.Service;

import com.gatar.Domain.TweetDTO;
import com.gatar.Domain.Visitor;
import com.gatar.Model.WebTwitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class WebTwitterService implements WebTwitterServiceInt {

    private List<Tweet> tweets;
    private Visitor visitor;

    private final WebTwitterRepository webTwitterRepository;

    @Autowired
    public WebTwitterService(WebTwitterRepository webTwitterRepository) {
        this.webTwitterRepository = webTwitterRepository;
    }

    public List<TweetDTO> getTweets(Visitor visitor){
        this.visitor = visitor;
        tweets = getTweetListFromRepository(visitor.getUsername());
        List<TweetDTO> tweetsDTO =
                filterTweetDTOList(
                parseToTweetDTOList(tweets));
        return tweetsDTO;
    }

    private List<Tweet> getTweetListFromRepository(String username){
        return webTwitterRepository.getUserTimeline(username);
    }

    private List<TweetDTO> parseToTweetDTOList(List<Tweet> tweets){
        return tweets.stream()
                .map(t -> toTweetDTO(t))
                .collect(Collectors.toList());
    }

    private List<TweetDTO> filterTweetDTOList(List<TweetDTO> tweetsDTO){
        if(visitor.getFilterWords().isEmpty()) return tweetsDTO;
        List<TweetDTO> filteredList = null;

        for(String word : visitor.getFilterWords()){
            filteredList = tweetsDTO.stream()
                    .filter(t -> t.getText().toLowerCase().contains(word.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return filteredList;
    }

    private TweetDTO toTweetDTO(Tweet tweet){
        TweetDTO tweetDTO = new TweetDTO();
        tweetDTO.setText(tweet.getText());
        prepareLinksInTweet(tweetDTO);
        tweetDTO.setCreateDate(tweet.getCreatedAt().toString().substring(4,16));
        return tweetDTO;
    }

    /**
     * Method preparing TweetDTO object to use by:
     * <ul>
     *     <li>extract URLs from tweet text body</li>
     *     <li>fill List of links with extracted values</li>
     *     <li>remove links from text body</li>
     * </ul>
     * @param tweetDTO object to prepare
     */
    private void prepareLinksInTweet(TweetDTO tweetDTO){
        List<String> urlList = extractURLs(tweetDTO.getText());
        fillTweetURLList(tweetDTO,urlList);
        removeURLsFromText(tweetDTO,urlList);

    }

    private void fillTweetURLList(TweetDTO tweetDTO, List<String> urlList){
        urlList.forEach(link -> tweetDTO.getUrl().add(link));
    }

    private void removeURLsFromText(TweetDTO tweetDTO, List<String> urlList){
        String text = tweetDTO.getText();
        for(String url : urlList){
            text = text.replaceAll(url,"");
        }
        tweetDTO.setText(text);
    }

    /**
     * Extract all URLs from given String.
     * @param text String with URLs
     * @return List with separated URL values
     */
    private List<String> extractURLs(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

}
