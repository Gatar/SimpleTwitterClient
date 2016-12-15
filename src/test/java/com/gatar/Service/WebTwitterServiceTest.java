package com.gatar.Service;

import com.gatar.Models.TweetDTO;
import com.gatar.Models.User;
import com.gatar.Repositories.WebTwitterRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;

/**
 * Class for test parsing Tweet list fo TweetDTO list. Tweet class constructor is used only for test purpose although it's deprecated.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class WebTwitterServiceTest {


    WebTwitterService webTwitterService;

    @Mock
    WebTwitterRepository webTwitterRepository;

    @Before
    public void setupMock(){
        webTwitterService = new WebTwitterService(webTwitterRepository);
    }

    @Test
    public void filterSampleTweetsList() throws Exception {
        //Given filter words
        User user = new User();
        user.getFilterWords().add("Word");
        user.getFilterWords().add("Test");

        //Given Tweets
        Date date = new Date(System.currentTimeMillis());
        Tweet withStandaloneFilterWords = new Tweet(0,"Sample tweet with word and test",date ,null,null,null,0,null,null);
        Tweet withOneFilterWord = new Tweet(0,"Sample tweet with word and nothing more",date,null,null,null,0,null,null);
        Tweet withoutFilterWords = new Tweet(0,"Sample tweet with filter elements",date,null,null,null,0,null,null);
        Tweet withFilterWordsInsideWords = new Tweet(0,"Sample tweet with swords and untestable link",date,null,null,null,0,null,null);
        List<Tweet> tweetList = Arrays.asList(withFilterWordsInsideWords,withOneFilterWord,withoutFilterWords,withStandaloneFilterWords);
        Mockito.when(webTwitterRepository.getUserTimeline(Mockito.anyString())).thenReturn(tweetList);

        //Given result
        TweetDTO withStandaloneFilterWordsResult = new TweetDTO();
        withStandaloneFilterWordsResult.setText("Sample tweet with word and test");
        TweetDTO withFilterWordsInsideWordsResult = new TweetDTO();
        withFilterWordsInsideWordsResult.setText("Sample tweet with swords and untestable link");
        List<TweetDTO> expectedList = Arrays.asList(withFilterWordsInsideWordsResult,withStandaloneFilterWordsResult);

        //When
        List<TweetDTO> resultList = webTwitterService.getTweets(user);

        //Before then
        Collections.sort(expectedList);
        Collections.sort(resultList);

        //Then
        Assert.assertTrue(expectedList.size()==resultList.size());
        Assert.assertEquals(expectedList.get(0).getText(),resultList.get(0).getText());
        Assert.assertEquals(expectedList.get(1).getText(),resultList.get(1).getText());
    }

    @Test
    public void extractEmailsFromTweetList() throws Exception {
        //Given filter words
        User user = new User();

        //Given Tweets
        Date date = new Date(System.currentTimeMillis());
        Tweet withOneURL = new Tweet(0,"a Sample tweet with word and test https://t.co/abcdef123hkj",date ,null,null,null,0,null,null);
        Tweet withTwoURL = new Tweet(0,"b Sample tweet with word and test https://t.co/abcdef123hkj https://t.co/abcdef456hkj",date,null,null,null,0,null,null);
        Tweet withInsideTextURL = new Tweet(0,"c Sample tweet withhttps://t.co/abcdef123hkj word and test",date,null,null,null,0,null,null);
        Tweet withoutURL = new Tweet(0,"d Sample tweet with word and test",date,null,null,null,0,null,null);
        List<Tweet> tweetList = Arrays.asList(withOneURL,withTwoURL,withInsideTextURL,withoutURL);
        Mockito.when(webTwitterRepository.getUserTimeline(Mockito.anyString())).thenReturn(tweetList);

        //Given result
        TweetDTO withOneURLResult = new TweetDTO();
        withOneURLResult.setText("a Sample tweet with word and test ");
        withOneURLResult.getUrl().add("https://t.co/abcdef123hkj");

        TweetDTO withTwoUrlResult = new TweetDTO();
        withTwoUrlResult.setText("b Sample tweet with word and test  ");
        withTwoUrlResult.getUrl().add("https://t.co/abcdef123hkj");
        withTwoUrlResult.getUrl().add("https://t.co/abcdef456hkj");

        TweetDTO withInsideTextURLResult = new TweetDTO();
        withInsideTextURLResult.getUrl().add("https://t.co/abcdef123hkj");
        withInsideTextURLResult.setText("c Sample tweet with word and test");

        TweetDTO withoutURLResult = new TweetDTO();
        withoutURLResult.setText("d Sample tweet with word and test");

        List<TweetDTO> expectedList = Arrays.asList(withOneURLResult,withTwoUrlResult,withInsideTextURLResult,withoutURLResult);

        //When
        List<TweetDTO> resultList = webTwitterService.getTweets(user);

        //Before then
        Collections.sort(expectedList);
        Collections.sort(resultList);

        //Then check tweet text after extract
        Assert.assertTrue(expectedList.size()==resultList.size());
        Assert.assertEquals(expectedList.get(0).getText(),resultList.get(0).getText());
        Assert.assertEquals(expectedList.get(1).getText(),resultList.get(1).getText());
        Assert.assertEquals(expectedList.get(2).getText(),resultList.get(2).getText());
        Assert.assertEquals(expectedList.get(3).getText(),resultList.get(3).getText());

        //Then check URL lists size
        Assert.assertTrue(expectedList.get(0).getUrl().size()==resultList.get(0).getUrl().size());
        Assert.assertTrue(expectedList.get(1).getUrl().size()==resultList.get(1).getUrl().size());
        Assert.assertTrue(expectedList.get(2).getUrl().size()==resultList.get(2).getUrl().size());
        Assert.assertTrue(expectedList.get(3).getUrl().size()==resultList.get(3).getUrl().size());

        //Then check URLs values
        Assert.assertEquals(expectedList.get(0).getUrl().get(0),resultList.get(0).getUrl().get(0));
        Assert.assertEquals(expectedList.get(1).getUrl().get(0),resultList.get(1).getUrl().get(0));
        Assert.assertEquals(expectedList.get(1).getUrl().get(1),resultList.get(1).getUrl().get(1));
        Assert.assertEquals(expectedList.get(2).getUrl().get(0),resultList.get(2).getUrl().get(0));
    }
}