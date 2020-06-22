package pl.rudz.eventsapp.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import pl.rudz.eventsapp.model.Client;
import pl.rudz.eventsapp.model.Event;


@Controller
public class EventController {
    private RestTemplate restTemplate = new RestTemplate();
    private Client client;

    public EventController() {
        client = new Client();
    }

    public Event[] getAllEvents(){
        ResponseEntity<Event[]> forEntity = restTemplate.getForEntity("https://frozen-falls-21272.herokuapp.com/events/getAll", Event[].class);
        return forEntity.getBody();
    }

    public Event getEvent(long id){
        ResponseEntity<Event> forEntity = restTemplate.getForEntity("https://frozen-falls-21272.herokuapp.com/events/get/" + id, Event.class);
        return forEntity.getBody();
    }

    public void addNewEvent(Event event){
        HttpEntity httpEntity = new HttpEntity(event);
        restTemplate.exchange("https://frozen-falls-21272.herokuapp.com/events/add",
                HttpMethod.POST,
                httpEntity,
                Void.class);
    }

    @RequestMapping("/events/{clientID}")
    public String event(Model model, @PathVariable long clientID){
        client = isActive(clientID);
        if(client == null) return "events-fail";

        model.addAttribute("client", client);

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

    @RequestMapping("/events-admin")
    public String eventAdmin(Model model){
        //sprawdzamy token
        if(client.getIsAdmin() != true)
            return "events-fail";

        //wyswietlanie SIEMA USERNAME TUTAJ SOM IWENTY

        Event[] eventList = getAllEvents();
        for (Event e: eventList
        ) {
            String newStartDate = e.getEventStartDate().replace('T', ' ').substring(0,e.getEventStartDate().length()-3);
            e.setEventStartDate(newStartDate);
            String newEndDate = e.getEventEndDate().replace('T', ' ').substring(0,e.getEventEndDate().length()-3);
            e.setEventEndDate(newEndDate);
        }
        model.addAttribute("eventList", eventList);
        return "events-admin";
    }

    @RequestMapping("/editevent/{eventID}")
    public ModelAndView updateEvent(@PathVariable long eventID, Event event){



        HttpEntity httpEntity = new HttpEntity(event);
        restTemplate.exchange("https://frozen-falls-21272.herokuapp.com/events/update",
                HttpMethod.PUT,
                httpEntity,
                Void.class);
        return new ModelAndView("redirect:/events-admin");
    }

    @RequestMapping("/events/join/{eventID}")
    public ModelAndView joinEvent(Model model, @PathVariable long eventID){

        String url = "https://frozen-falls-21272.herokuapp.com/events/get/" + eventID;
        ResponseEntity<Event> forEntity = restTemplate.getForEntity(url, Event.class);
        HttpEntity httpEntity = new HttpEntity(forEntity.getBody());
        restTemplate.exchange( "https://frozen-falls-21272.herokuapp.com/events/join/" + client.getId(),
                HttpMethod.POST,
                httpEntity,
                Void.class);
        return new ModelAndView("redirect:/events/" + client.getId());
    }

    @RequestMapping("/events-edit/{eventID}")
    public String editEvent(Model model, @PathVariable long eventID){
        model.addAttribute("event", getEvent(eventID));
        return "eventedit";
    }

    @RequestMapping("events/delete/{eventID}")
    public ModelAndView deleteEvent(@PathVariable long eventID){
        String url = "https://frozen-falls-21272.herokuapp.com/events/get/" + eventID;
        ResponseEntity<Event> forEntity = restTemplate.getForEntity(url, Event.class);
        HttpEntity httpEntity = new HttpEntity(forEntity.getBody());
        restTemplate.exchange("https://frozen-falls-21272.herokuapp.com/events/delete",
                HttpMethod.DELETE,
                httpEntity,
                Void.class);
        return new ModelAndView("redirect:/events-admin/" + client.getId());
    }

    @RequestMapping("/events-fail")
    public String redirectToEventFail() {return "events-fail";}

    @RequestMapping("/redirectToAddEvent")
    public String redirectToAddEvent(){
        return "redirect:/addevent";
    }

    @RequestMapping("/redirectToEditEvent")
    public String redirectToEditEvent(){
        return "redirect:/events-edit";
    }

    @RequestMapping("/redirectToEvents")
    public String redirectToEvents(){
        return "redirect:/events/" + client.getId();
    }

    @RequestMapping("/redirectToEventsAdmin")
    public String redirectToEventsAdmin(){
        return "redirect:/events-admin";
    }

    @RequestMapping("/addevent")
    public String addEvent(Model model) {
        model.addAttribute("event", new Event());
        return "eventadd";
    }

    public String getUserToken(Long clientId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("id", String.valueOf(clientId));
        HttpEntity<Long> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> token = restTemplate.exchange("https://frozen-falls-21272.herokuapp.com/clients/getToken", HttpMethod.GET, httpEntity, String.class);
        return token.getBody();
    }

    @RequestMapping(value = "/saveevent", method = RequestMethod.POST)
    public ModelAndView saveEvent(Event event) {
        addNewEvent(event);
        return new ModelAndView("redirect:/events-admin");
    }

    public Client isActive(Long clientId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + getUserToken(clientId));
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Client> forEntity = restTemplate.exchange("https://justfitclient.pythonanywhere.com/account/properties/",
                HttpMethod.GET,
                httpEntity,
                Client.class);
        if(forEntity.getStatusCodeValue() == 200)
        return forEntity.getBody();
        else return null;
    }
}
