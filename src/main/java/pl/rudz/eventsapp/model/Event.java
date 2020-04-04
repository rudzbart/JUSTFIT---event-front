package pl.rudz.eventsapp.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "workerId",
        "clientIds",
        "eventName",
        "eventStartDate",
        "eventEndDate"
})
public class Event {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("workerId")
    private Integer workerId;
    @JsonProperty("clientIds")
    private Object clientIds;
    @JsonProperty("eventName")
    private String eventName;
    @JsonProperty("eventStartDate")
    private String eventStartDate;
    @JsonProperty("eventEndDate")
    private String eventEndDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }


    public Event() {
    }

    @JsonProperty("workerId")
    public Integer getWorkerId() {
        return workerId;
    }

    @JsonProperty("workerId")
    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    @JsonProperty("clientIds")
    public Object getClientIds() {
        return clientIds;
    }

    @JsonProperty("clientIds")
    public void setClientIds(Object clientIds) {
        this.clientIds = clientIds;
    }

    @JsonProperty("eventName")
    public String getEventName() {
        return eventName;
    }

    @JsonProperty("eventName")
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @JsonProperty("eventStartDate")
    public String getEventStartDate() {
        return eventStartDate;
    }

    @JsonProperty("eventStartDate")
    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    @JsonProperty("eventEndDate")
    public String getEventEndDate() {
        return eventEndDate;
    }

    @JsonProperty("eventEndDate")
    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
