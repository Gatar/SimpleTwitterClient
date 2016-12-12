package com.gatar.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Required methods with @RequestMapping for Controller
 */
public interface WebTwitterControllerInt {
    /**
     * Load start page with default feed.
     * @param model Model
     * @return html page adress
     */
    public String startPage(Model model);

    /**
     * Load tweets for from specified user timeline
     * @param model Model
     * @param username name of user for show timeline
     * @return html page adress
     */
    public String getUser(Model model, @RequestParam("username") String username);

    /**
     * Add new filter word for filtering tweets adn reload page.
     * @param model Model
     * @param filterWord specified filter word
     * @return html page adress
     */
    public String addFilter(Model model, @RequestParam("filter") String filterWord);

    /**
     * Clear all filters and reload page.
     * @param model Model
     * @return html page adress
     */
    public String clearFilters(Model model);

}
