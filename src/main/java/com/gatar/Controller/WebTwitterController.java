package com.gatar.Controller;

import com.gatar.Domain.TweetDTO;
import com.gatar.Service.WebTwitterService;
import com.gatar.Service.WebTwitterServiceInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebTwitterController implements WebTwitterControllerInt{

    private final WebTwitterService webTwitterService;

    @Autowired
    public WebTwitterController(WebTwitterService webTwitterService) {
        this.webTwitterService = webTwitterService;
    }

    @RequestMapping(value = "/")
    public String startPage(Model model){
        loadPageAttributes(model,"");
        return "twitterclient";
    }

    @RequestMapping(value = "/getuser")
    public String getUser(Model model, @RequestParam("username") String username){
        loadPageAttributes(model,username);
        return "twitterclient";
    }

    @RequestMapping(value = "/addfilter")
    public String addFilter(Model model, @RequestParam("filter") String filterWord){
        webTwitterService.addFilterWord(filterWord);
        loadPageAttributes(model,"");
        return "twitterclient";
    }

    @RequestMapping(value = "/clearfilters")
    public String clearFilters(Model model){
        webTwitterService.clearFilters();
        loadPageAttributes(model,"");
        return "twitterclient";
    }

    private void loadPageAttributes(Model model, String username){
        List<TweetDTO> tweets = webTwitterService.getTweets(username);
        String filterWords = webTwitterService.getFilterWords();
        model.addAttribute("tweets",tweets);
        model.addAttribute("filters",filterWords);
        model.addAttribute("actualUser",(username.equals("")) ? webTwitterService.getLastUsedUsername() : username);
    }
}
