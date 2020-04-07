package pl.rudz.eventsapp.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import pl.rudz.eventsapp.model.Event;


@Controller
public class EventController {
    private RestTemplate restTemplate = new RestTemplate();

    public EventController() {

    }

    public Event[] getAllEvents(){
        ResponseEntity<Event[]> forEntity = restTemplate.getForEntity("https://frozen-falls-21272.herokuapp.com/events/getAll", Event[].class);
        return forEntity.getBody();
    }

    public void addNewEvent(Event event){
        HttpEntity httpEntity = new HttpEntity(event);
        restTemplate.exchange("https://frozen-falls-21272.herokuapp.com/events/add",
                HttpMethod.POST,
                httpEntity,
                Void.class);
    }


    @RequestMapping("/events")
    public String event(Model model){
        Event[] eventList = getAllEvents();
        for (Event e: eventList
             ) {
            String newStartDate = e.getEventStartDate().replace('T', ' ').substring(0,e.getEventStartDate().length()-3);
            e.setEventStartDate(newStartDate);
            String newEndDate = e.getEventEndDate().replace('T', ' ').substring(0,e.getEventEndDate().length()-3);
            e.setEventEndDate(newEndDate);
        }
        model.addAttribute("eventList", eventList);
        return "events";
    }

    @RequestMapping("/events-user")
    public String eventForUser(Model model){
        Event[] eventList = getAllEvents();
        for (Event e: eventList
        ) {
            String newStartDate = e.getEventStartDate().replace('T', ' ').substring(0,e.getEventStartDate().length()-3);
            e.setEventStartDate(newStartDate);
            String newEndDate = e.getEventEndDate().replace('T', ' ').substring(0,e.getEventEndDate().length()-3);
            e.setEventEndDate(newEndDate);
        }
        model.addAttribute("eventList", eventList);
        return "events-user";
    }

    @RequestMapping("/redirectToAddEvent")
    public String redirectToAddEvent(){
        return "redirect:/addevent";
    }

    @RequestMapping("/redirectToEvents")
    public String redirectToEvents(){
        return "redirect:/events";
    }

    @RequestMapping("/addevent")
    public String addEvent(Model model) {
        model.addAttribute("event", new Event());
        return "eventadd";
    }

    @RequestMapping(value = "/saveevent", method = RequestMethod.POST)
    public ModelAndView saveGoal(Event event) {
        addNewEvent(event);
        return new ModelAndView("redirect:/events");
    }
}
