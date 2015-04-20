package helloworld.domain.handler

import org.axonframework.eventhandling.annotation.EventHandler
import helloworld.domain.event.HelloworldCreatedEvent
import org.springframework.stereotype.Component

@Component
class HelloworldEventHandler {
    
    @EventHandler
    def handle(HelloworldCreatedEvent event) {
        println('''Greeting: Hello «event.description» («event.id»)''')
    }
    
}