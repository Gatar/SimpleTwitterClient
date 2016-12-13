package com.gatar.Controller;

import com.gatar.Domain.TweetDTO;
import com.gatar.Service.WebTwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.ResourceNotFoundException;
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
        return loadPageAttributes(model,"");
    }

    @RequestMapping(value = "/getuser")
    public String getUser(Model model, @RequestParam("username") String username){
        return loadPageAttributes(model,username);
    }

    @RequestMapping(value = "/addfilter")
    public String addFilter(Model model, @RequestParam("filter") String filterWord){
        webTwitterService.addFilterWord(filterWord);
        return loadPageAttributes(model,"");
    }

    @RequestMapping(value = "/clearfilters")
    public String clearFilters(Model model){
        webTwitterService.clearFilters();
        return loadPageAttributes(model,"");
    }

    private String loadPageAttributes(Model model, String username){
        try {
            List<TweetDTO> tweets = webTwitterService.getTweets(username);
            String filterWords = webTwitterService.getFilterWords();
            model.addAttribute("tweets", tweets);
            model.addAttribute("filters", filterWords);
            model.addAttribute("actualUser", (username.equals("")) ? webTwitterService.getLastUsedUsername() : username);
            model.addAttribute("pictureUrl", webTwitterService.getUserPicture());

        }catch(ResourceNotFoundException e){
            model.addAttribute("error", "Chosen profile doesn't exist!");
            return "error";

        }catch (NotAuthorizedException e){
            model.addAttribute("error", "Access to profile needs authorization");
            return "error";

        }catch(Exception e){
            model.addAttribute("error","Error " + e.toString());
            return "error";
        }

        return "twitterclient";
    }
}
