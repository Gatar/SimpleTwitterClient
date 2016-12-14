package com.gatar.Controller;

import com.gatar.Domain.TweetDTO;
import com.gatar.Domain.Visitor;
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
@SessionAttributes("visitor")
public class WebTwitterController implements WebTwitterControllerInt {

    private final WebTwitterService webTwitterService;

    @Autowired
    public WebTwitterController(WebTwitterService webTwitterService) {
        this.webTwitterService = webTwitterService;
    }

    @RequestMapping(value = "/")
    public String startPage(@ModelAttribute("visitor") Visitor visitor, Model model){
        return loadPageAttributes(model,visitor);
    }

    @RequestMapping(value = "/getuser")
    public String getUser(@ModelAttribute("visitor") Visitor visitor, Model model, @RequestParam("username") String username){
        visitor.setUsername(username);
        return loadPageAttributes(model,visitor);
    }

    @RequestMapping(value = "/addfilter")
    public String addFilter(@ModelAttribute("visitor") Visitor visitor, Model model, @RequestParam("filter") String filterWord){
        visitor.getFilterWords().add(filterWord);
        return loadPageAttributes(model,visitor);
    }

    @RequestMapping(value = "/clearfilters")
    public String clearFilters(@ModelAttribute("visitor") Visitor visitor, Model model){
        visitor.getFilterWords().clear();
        return loadPageAttributes(model,visitor);
    }

    @ModelAttribute("visitor")
    public Visitor getVisitor(){
        return new Visitor();
    }


    private String loadPageAttributes(Model model, Visitor visitor){
        try {
            List<TweetDTO> tweets = webTwitterService.getTweets(visitor);
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
