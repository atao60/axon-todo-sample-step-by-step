package helloworld.domain.command

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier
import org.eclipse.xtend.lib.annotations.Data 

@Data
class CreateHelloworldCommand {
    
    @TargetAggregateIdentifier
    val String id
    val String description
 
 }