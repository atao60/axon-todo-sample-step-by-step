package helloworld.domain.command;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class CreateHelloworldCommand {
	 
    @TargetAggregateIdentifier
    private final String id;
    private final String description;
 
    public CreateHelloworldCommand(String id, String description) {
        this.id = id;
        this.description = description;
    }
 
    public String getId() {
        return id;
    }
 
    public String getDescription() {
        return description;
    }
}