package helloworld

import helloworld.config.MainConfig
import helloworld.domain.command.CreateHelloworldCommand
import java.util.UUID
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class HelloworldWithJpaRunner {
    
    static val NAME_TO_BE_GREETED = "World"
    
    def static void main(String[] args) {
        val appCtx = new AnnotationConfigApplicationContext(MainConfig)
        appCtx.registerShutdownHook
        extension val commandGateway = appCtx.getBean(CommandGateway)

        val itemId = UUID.randomUUID.toString
        send(new CreateHelloworldCommand(itemId, NAME_TO_BE_GREETED))
    }
    
}