package helloworld

import helloworld.domain.command.CreateHelloworldCommand
import java.util.UUID

class HelloworldRunner {
    
    static val EVENTS_STORAGE_FILE_PATH = "./target/events"
    static val NAME_TO_BE_GREETED = "World"
    
    def static void main(String[] args) {
    	extension val context = new HelloworldContext.Builder(EVENTS_STORAGE_FILE_PATH).build

        val itemId = UUID.randomUUID.toString
        commandGateway.send(new CreateHelloworldCommand(itemId, NAME_TO_BE_GREETED))
    }
    
}