package helloworld.domain.handler;

import helloworld.domain.event.HelloworldCreatedEvent;

import org.axonframework.eventhandling.annotation.EventHandler;

public class HelloworldEventHandler {
	 
    @EventHandler
    public void handle(HelloworldCreatedEvent event) {
        System.out.println("Greeting: Hello " + event.getDescription() + " (" + event.getId() + ")");
    }
 
}