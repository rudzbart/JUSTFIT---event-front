package pl.rudz.eventsapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @RequestMapping("/events")
    public String events() {
        return "events";
    }

    @RequestMapping("/newevent")
    public String newEvent() {
        return "newevent";
    }

    @RequestMapping("/redirectToAddEvent")
    public ModelAndView redirectToSignUp() {
        return new ModelAndView("redirect:/newevent");
    }

}
