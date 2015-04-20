package helloworld.config

import helloworld.domain.Helloworld
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import org.axonframework.commandhandling.SimpleCommandBus
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor
import org.axonframework.commandhandling.gateway.DefaultCommandGateway
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider
import org.axonframework.eventhandling.SimpleEventBus
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventstore.jpa.JpaEventStore
import org.axonframework.unitofwork.SpringTransactionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class CqrsConfig {
    
    @Autowired var PlatformTransactionManager transactionManager
    @PersistenceContext var EntityManager entityManager
    
    /* <axon:annotation-config /> */
    
    @Bean
    def annotationEventListenerBeanPostProcessor() {
        val processor = new AnnotationEventListenerBeanPostProcessor
        processor.eventBus = eventBus
        processor
    }
    
    @Bean
    def annotationCommandHandlerBeanPostProcessor() {
        val processor = new AnnotationCommandHandlerBeanPostProcessor
        processor.commandBus = commandBus
        processor
    }

    
    @Bean
    def AggregateAnnotationCommandHandler aggregateAnnotationCommandHandler() {
          AggregateAnnotationCommandHandler.subscribe(Helloworld, helloworldRepository, commandBus)
    }
     
    /*
        <axon:event-sourcing-repository id="orderRepository"
            aggregate-type="com.acme.oms.commandhandling.Order" />
    */
    @Bean
    def helloworldRepository() {
        val repository = new EventSourcingRepository<Helloworld>(Helloworld, eventStore)
        repository.eventBus = eventBus
        repository
    }

    // <axon:jpa-event-store id="eventStore" />
    @Bean
    def eventStore() {
        val entityManagerProvider = new ContainerManagedEntityManagerProvider
        entityManagerProvider.entityManager = entityManager
        new JpaEventStore(entityManagerProvider)
    }

    // <axon:event-bus id="eventBus" />
    @Bean
    def eventBus() {
        new SimpleEventBus
    }

    // <axon:command-bus id="commandBus"/>
    @Bean
    def commandBus() {
        val commandBus = new SimpleCommandBus
        commandBus.transactionManager = new SpringTransactionManager(transactionManager)
        commandBus
    }

    @Bean
    def commandGateway() {
        new DefaultCommandGateway(commandBus)
    }

}