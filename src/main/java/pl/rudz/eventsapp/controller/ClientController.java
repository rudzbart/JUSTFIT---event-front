package pl.rudz.eventsapp.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ClientController {

    RestTemplate restTemplate = new RestTemplate();

    //TODO pozbierac linki
    //PRZEMEK - pobieram token danego usera
    public String getUserToken(){
        ResponseEntity<String> forEntity = restTemplate.getForEntity("GET TOKENA LINK", String.class);
        return forEntity.getBody();
    }

    //PRZEMEK - dodaje klienta do eventu
    public void addClientToEvent(Long clientId){
        HttpHeaders headers = new HttpHeaders();
        headers.add("clientId", String.valueOf(clientId));
        HttpEntity<Long> httpEntity = new HttpEntity<Long>(headers);
        restTemplate.postForObject("ADD USERA DO EVENTU LINK", httpEntity, Long.class);
    }

    //PRZEMEK
    //TODO Zmienic na przekazywanie przez body
    public void userNotActive(Long clientId){
        if(!isActive(clientId)){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization:Token", String.valueOf(clientId));
           // restTemplate.postForObject("https://justfitclient.pythonanywhere.com/account/token/destroy/", httpEntity, Long.class);
        }
    }

    //DANIEL
    //TODO przez body
    public boolean isActive(Long clientId){
        ResponseEntity<Boolean> forEnitty = restTemplate.getForEntity("https://justfitclient.pythonanywhere.com/account/token/properties/" + clientId, boolean.class);
        return forEnitty.getBody();
    }

}
