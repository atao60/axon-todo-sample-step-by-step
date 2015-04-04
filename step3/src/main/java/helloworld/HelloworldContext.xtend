package helloworld

import helloworld.domain.Helloworld
import helloworld.domain.handler.HelloworldEventHandler
import java.io.File
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.eventhandling.SimpleEventBus
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventstore.fs.FileSystemEventStore
import org.axonframework.eventstore.fs.SimpleEventFileResolver
import org.eclipse.xtend.lib.annotations.Accessors

class HelloworldContext {

    val CommandBus commandBus
    @Accessors(PUBLIC_GETTER)
    val DefaultCommandGateway commandGateway
    val FileSystemEventStore eventStore
    val SimpleEventBus eventBus
    val EventSourcingRepository<Helloworld> eventSourcingRepository

    static class Builder {
        val String storageFilePath
        CommandBus commandBus
        DefaultCommandGateway commandGateway
        FileSystemEventStore eventStore
        SimpleEventBus eventBus
        EventSourcingRepository<Helloworld> eventSourcingRepository

        new(String storageFilePath) {
            this.storageFilePath = storageFilePath
        }

        private def createCommandBus() {
            commandBus = new SimpleCommandBus
        }

        private def createCommandGateway() {
            commandGateway = new DefaultCommandGateway(commandBus)
        }

        private def createEventStore() {
            eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File(storageFilePath)))
        }

        private def createEventBus() {
            eventBus = new SimpleEventBus
        }

        private def createEventSourcingRepository() {
            eventSourcingRepository = new EventSourcingRepository(Helloworld, eventStore)
            eventSourcingRepository.setEventBus(eventBus)
        }

        def build() {
            createCommandBus
            createCommandGateway
            createEventStore
            createEventBus
            createEventSourcingRepository
        
            AggregateAnnotationCommandHandler.subscribe(Helloworld, eventSourcingRepository, commandBus)
            AnnotationEventListenerAdapter.subscribe(new HelloworldEventHandler, eventBus)

            new HelloworldContext(this)
        }
    }

    private new(extension Builder builder) {
        commandBus = builder.commandBus
        commandGateway = builder.commandGateway
        eventStore = builder.eventStore
        eventBus = builder.eventBus
        eventSourcingRepository = builder.eventSourcingRepository
    }

}
