package helloworld

import helloworld.domain.Helloworld
import java.io.File
import java.util.UUID
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.eventhandling.SimpleEventBus
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventstore.fs.FileSystemEventStore
import org.axonframework.eventstore.fs.SimpleEventFileResolver
import helloworld.domain.command.CreateHelloworldCommand
import helloworld.domain.handler.HelloworldEventHandler

class HelloworldRunner {
    
    static val EVENTS_STORAGE_FILE_PATH = "./target/events"
    static val NEED_TO_BE_GREETED = "World"
    static val NOT_READY = "Helloworld runner must be ready before sending a command."

    val String storageFilePath
    CommandBus commandBus
    DefaultCommandGateway commandGateway
    FileSystemEventStore eventStore
    SimpleEventBus eventBus
    EventSourcingRepository<Helloworld> eventSourcingRepository
    boolean ready

    new(String storageFilePath) {
        this.storageFilePath = storageFilePath
    }
    
    def createCommandBus() {
            commandBus = new SimpleCommandBus
    }
    
    def createCommandGateway() {
            commandGateway = new DefaultCommandGateway(commandBus)
    }
    
    def createEventStore() {
            eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File(storageFilePath)))
    }
    
    def createEventBus() {
            eventBus = new SimpleEventBus
    }
    
    def createEventSourcingRepository() {
            eventSourcingRepository = new EventSourcingRepository(Helloworld, eventStore)
            eventSourcingRepository.setEventBus(eventBus)
    }
    
    def start() {
        createCommandBus
        createCommandGateway
        createEventStore
        createEventBus
        createEventSourcingRepository

        AggregateAnnotationCommandHandler.subscribe(Helloworld, eventSourcingRepository, commandBus)
        AnnotationEventListenerAdapter.subscribe(new HelloworldEventHandler, eventBus)
        ready = true
        return this
    }
    
    def send(Object command) {
        if(!ready) throw new IllegalStateException(NOT_READY)
        
        commandGateway.send(command)
    }
    
    def static void main(String[] args) {
    	val runner = new HelloworldRunner(EVENTS_STORAGE_FILE_PATH).start

        val itemId = UUID.randomUUID().toString()
        runner.send(new CreateHelloworldCommand(itemId, NEED_TO_BE_GREETED))
    }
    
}