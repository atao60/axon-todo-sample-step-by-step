package helloworld.domain;

import helloworld.domain.command.CreateHelloworldCommand;
import helloworld.domain.event.HelloworldCreatedEvent;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

public class Helloworld extends AbstractAnnotatedAggregateRoot<String> {

	private static final long serialVersionUID = 1L;

	@AggregateIdentifier
	private String id;

	public Helloworld() {
	}

	@CommandHandler
	public Helloworld(CreateHelloworldCommand command) {
		apply(new HelloworldCreatedEvent(command.getId(),
				command.getDescription()));
	}
	
	@EventHandler
	public void on(HelloworldCreatedEvent event) {
		this.id = event.getId();
	}
}