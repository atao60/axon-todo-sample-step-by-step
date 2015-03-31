package helloworld;

import helloworld.domain.Helloworld;
import helloworld.domain.command.CreateHelloworldCommand;
import helloworld.domain.handler.HelloworldEventHandler;

import java.io.File;
import java.util.UUID;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;

public class HelloworldRunner {
	 
    private static final String EVENTS_STORAGE_FILE_PATH = "./target/events";
	private static final String NEED_TO_BE_GREETED = "World";

	public static void main(String[] args) {
        // let's start with the Command Bus
        CommandBus commandBus = new SimpleCommandBus();
 
        // the CommandGateway provides a friendlier API
        CommandGateway commandGateway = new DefaultCommandGateway(commandBus);
 
        // we'll store Events on the FileSystem, in the "events/" folder
        EventStore eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File(EVENTS_STORAGE_FILE_PATH)));
 
        // a Simple Event Bus will do
        EventBus eventBus = new SimpleEventBus();
 
        // we need to configure the repository
        EventSourcingRepository<Helloworld> repository = new EventSourcingRepository<>(Helloworld.class, eventStore);
        repository.setEventBus(eventBus);
 
        // Axon needs to know that our Helloworld Aggregate can handle commands
        AggregateAnnotationCommandHandler.subscribe(Helloworld.class, repository, commandBus);
        
        // do something with instances of Event
        AnnotationEventListenerAdapter.subscribe(new HelloworldEventHandler(), eventBus);
 
        // and let's send some Commands on the CommandBus.
        final String itemId = UUID.randomUUID().toString();
        commandGateway.send(new CreateHelloworldCommand(itemId, NEED_TO_BE_GREETED));
    }
}