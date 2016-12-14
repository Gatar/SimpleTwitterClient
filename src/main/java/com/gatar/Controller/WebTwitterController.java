package com.gatar.Controller;

import com.gatar.Models.TweetDTO;
import com.gatar.Models.User;
import com.gatar.Service.WebTwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("user")
public class WebTwitterController implements WebTwitterControllerInt {

    private final WebTwitterService webTwitterService;

    @Autowired
    public WebTwitterController(WebTwitterService webTwitterService) {
        this.webTwitterService = webTwitterService;
    }

    @RequestMapping(value = "/")
    public String startPage(@ModelAttribute("user") User user, Model model){
        return loadPageAttributes(model, user);
    }

    @RequestMapping(value = "/getuser")
    public String getUser(@ModelAttribute("user") User user, Model model, @RequestParam("username") String username){
        user.setUsername(username);
        return loadPageAttributes(model, user);
    }

    @RequestMapping(value = "/addfilter")
    public String addFilter(@ModelAttribute("user") User user, Model model, @RequestParam("filter") String filterWord){
        user.getFilterWords().add(filterWord);
        return loadPageAttributes(model, user);
    }

    @RequestMapping(value = "/clearfilters")
    public String clearFilters(@ModelAttribute("user") User user, Model model){
        user.getFilterWords().clear();
        return loadPageAttributes(model, user);
    }

    @ModelAttribute("user")
    public User getVisitor(){
        return new User();
    }


    private String loadPageAttributes(Model model, User user){
        try {
            List<TweetDTO> tweets = webTwitterService.getTweets(user);
            model.addAttribute("tweets", tweets);
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
