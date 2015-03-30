package helloworld.domain.event;

public class HelloworldCreatedEvent {

    private final String id;
    private final String description;
 
    public HelloworldCreatedEvent(String id, String description) {
        this.id = id;
        this.description = description;
    }
 
    public String getId() {
        return id;
    }
 
    public String getDescription() {
        return description;
    }

}
