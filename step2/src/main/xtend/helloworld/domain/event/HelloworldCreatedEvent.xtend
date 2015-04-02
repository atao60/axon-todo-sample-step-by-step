package helloworld.domain.event

import org.eclipse.xtend.lib.annotations.Data 

@Data
class HelloworldCreatedEvent {
    
    val String id
    val String description

}