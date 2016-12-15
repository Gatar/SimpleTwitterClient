package com.gatar.Controller;

import com.gatar.Models.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Required methods with @RequestMapping for Controller.
 * Set session time parameters:
 * <ul>
 * <li>- for end session with close browser remove from application.properties line 'server.session.cookie.max-age='</li>
 * <li>- for set own time session  - setvalue of above parameter (in minutes)</li>
 * </ul>
 */
public interface WebTwitterControllerInt {
    /**
     * Load start page with default feed.
     * @param model Repositories
     * @return html page adress
     */
    String startPage(@ModelAttribute("visitor") User user, Model model);

    /**
     * Load tweets for from specified user timeline
     * @param model Repositories
     * @param username name of user for show timeline
     * @return html page adress
     */
    String getUser(@ModelAttribute("visitor") User user, Model model, @RequestParam("username") String username);

    /**
     * Add new filter word for filtering tweets adn reload page.
     * @param model Repositories
     * @param filterWord specified filter word
     * @return html page adress
     */
    String addFilter(@ModelAttribute("visitor") User user, Model model, @RequestParam("filter") String filterWord);

    /**
     * Clear all filters and reload page.
     * @param model Repositories
     * @return html page adress
     */
    String clearFilters(@ModelAttribute("visitor") User user, Model model);
}
