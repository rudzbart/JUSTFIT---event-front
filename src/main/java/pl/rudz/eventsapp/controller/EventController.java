package pl.rudz.eventsapp.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.ServerRequest;
import pl.rudz.eventsapp.model.Event;

import java.security.Principal;
import java.util.List;

@Controller
public class EventController {
    private RestTemplate restTemplate = new RestTemplate();

    public EventController() {

    }

    public Event[] getAllEvents(){
        ResponseEntity<Event[]> forEntity = restTemplate.getForEntity("https://frozen-falls-21272.herokuapp.com/getAllEvents", Event[].class);
        return forEntity.getBody();
    }

    public void addNewEvent(String workerId, String eventStartDate, String eventName, String eventEndDate){
        HttpHeaders headers = new HttpHeaders();
        headers.add("workerId", workerId);
        headers.add("eventName",eventName);
        headers.add("eventStartDate",eventStartDate);
        headers.add("eventEndDate",eventEndDate);
        HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
        restTemplate.postForObject("https://frozen-falls-21272.herokuapp.com/addEvent", httpEntity, String.class);
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

    @RequestMapping("/redirectToAddEvent")
    public String redirectToAddEvent(){
        return "redirect:/addevent";
    }

    @RequestMapping("/redirectToEvents")
    public String redirectToEvents(){
        return "events";
    }

    @RequestMapping("/addevent")
    public String addEvent(Model model) {
        model.addAttribute("event", new Event());
        return "eventadd";
    }


    @RequestMapping(value = "/saveevent", method = RequestMethod.POST)
    public ModelAndView saveGoal(Event event) {
        addNewEvent(event.getWorkerId().toString(), event.getEventStartDate(), event.getEventName(), event.getEventEndDate());
        return new ModelAndView("redirect:/events");
    }
}
