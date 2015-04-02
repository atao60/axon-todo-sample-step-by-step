package helloworld.domain.handler

import org.axonframework.eventhandling.annotation.EventHandler
import helloworld.domain.event.HelloworldCreatedEvent

class HelloworldEventHandler {
    
    @EventHandler
    def handle(HelloworldCreatedEvent event) {
        println('''Greeting: Hello «event.description» («event.id»)''')
    }
    
}