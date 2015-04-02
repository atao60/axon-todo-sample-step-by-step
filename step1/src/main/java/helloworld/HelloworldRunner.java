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
	private static final String NOT_READY = "Helloworld runner must be ready before sending a command.";
	
	private final String storageFilePath;
	private CommandBus commandBus;
	private CommandGateway commandGateway;
	private EventStore eventStore;
	private EventBus eventBus;
	private EventSourcingRepository<Helloworld> eventSourcingRepository;
	private boolean ready;
	
	public HelloworldRunner(String storageFilePath) {
		this.storageFilePath = storageFilePath;
		
	}

	protected void createCommandBus() {
			commandBus = new SimpleCommandBus();
	}
	
    /* 
     * the CommandGateway provides a friendlier API
     */
	protected void createCommandGateway() {
			commandGateway = new DefaultCommandGateway(commandBus);
	}
	
    /*
     *  store Events on the FileSystem, in the "events/" folder
     */
	protected void createEventStore() {
			eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File(storageFilePath)));
	}
	
	/*
	 *  a Simple Event Bus will do
	 */
	protected void createEventBus() {
			eventBus = new SimpleEventBus();
	}
	
	protected void createEventSourcingRepository() {
			eventSourcingRepository = new EventSourcingRepository<>(Helloworld.class, eventStore);
			// we need to configure the repository
			eventSourcingRepository.setEventBus(eventBus);
	}
	
	public HelloworldRunner start() {
		createCommandBus();
		createCommandGateway();
		createEventStore();
		createEventBus();
		createEventSourcingRepository();

		// Axon needs to know that our Helloworld Aggregate can handle commands
        AggregateAnnotationCommandHandler.subscribe(Helloworld.class, eventSourcingRepository, commandBus);
        
        // do something with instances of Event
        AnnotationEventListenerAdapter.subscribe(new HelloworldEventHandler(), eventBus);
		ready = true;
		return this;
	}
	
	public void send(Object command) {
		if(!ready) throw new IllegalStateException(NOT_READY);
		commandGateway.send(command);
	}
	
	public static void main(String[] args) {
		
		HelloworldRunner runner = new HelloworldRunner(EVENTS_STORAGE_FILE_PATH).start();

        final String itemId = UUID.randomUUID().toString();
        runner.send(new CreateHelloworldCommand(itemId, NEED_TO_BE_GREETED));
    }
}