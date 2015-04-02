package helloworld.domain


import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import popsuite.xtend.annotation.DefaultSerializable
import helloworld.domain.command.CreateHelloworldCommand
import helloworld.domain.event.HelloworldCreatedEvent

@DefaultSerializable
class Helloworld extends AbstractAnnotatedAggregateRoot<String> {
    
    @AggregateIdentifier
    String id

    new() {
    }

    @CommandHandler
    new(CreateHelloworldCommand command) {
        apply(new HelloworldCreatedEvent(command.id,
                command.description))
    }
    
    @EventHandler
    def on(HelloworldCreatedEvent event) {
        id = event.id
    }
    
}