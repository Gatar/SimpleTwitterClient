package com.gatar.Service;

import com.gatar.Domain.TweetDTO;
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

    private List<String> filterWords = new ArrayList<>();
    private String lastUsedUsername = "JAVA";
    private final WebTwitterRepository webTwitterRepository;

    @Autowired
    public WebTwitterService(WebTwitterRepository webTwitterRepository) {
        this.webTwitterRepository = webTwitterRepository;
    }

    public List<TweetDTO> getTweets(String username){
        List<Tweet> tweets = getTweetListFromRepository(username);
        List<TweetDTO> tweetsDTO =
                filterTweetDTOList(
                parseToTweetDTOList(tweets));
        rememberUsername(username);
        return tweetsDTO;
    }

    public String getFilterWords() {
        return (filterWords.isEmpty()) ? "No filters used" : "Used filter words: " + filterWords.toString();
    }

    public String getLastUsedUsername() {
        return lastUsedUsername;
    }

    public void addFilterWord(String word){
        if(!filterWords.contains(word)) filterWords.add(word);
    }

    public void clearFilters(){
        filterWords.clear();
    }

    private List<Tweet> getTweetListFromRepository(String username){
        return webTwitterRepository.getUserTimeline(username.equals("") ? lastUsedUsername : username);
    }

    private List<TweetDTO> parseToTweetDTOList(List<Tweet> tweets){
        return tweets.stream()
                .map(t -> toTweetDTO(t))
                .collect(Collectors.toList());
    }

    private List<TweetDTO> filterTweetDTOList(List<TweetDTO> tweetsDTO){
        if(filterWords.isEmpty()) return tweetsDTO;
        List<TweetDTO> filteredList = null;

        for(String word : filterWords){
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

    private void prepareLinksInTweet(TweetDTO tweetDTO){
        List<String> urlList = extractUrls(tweetDTO.getText());
        fillTweetDTOLinkList(tweetDTO,urlList);
        prepareTweetText(tweetDTO,urlList);

    }

    private void fillTweetDTOLinkList(TweetDTO tweetDTO, List<String> urlList){
        urlList.forEach(link -> tweetDTO.getUrl().add(link));
    }

    /**
     * Preparing tweet text by:
     * - erase links
     * - add tab at the begining
     */
    private void prepareTweetText(TweetDTO tweetDTO, List<String> urlList){
        String text = tweetDTO.getText();
        for(String url : urlList){
            text = text.replaceAll(url,"");
        }
        text = "\t" + text;
        tweetDTO.setText(text);
    }

    private List<String> extractUrls(String text)
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

    private void rememberUsername(String username){
        if(!username.equals("")) lastUsedUsername = username;
    }
}
