package helloworld;

import helloworld.domain.Helloworld;
import helloworld.domain.command.CreateHelloworldCommand;
import helloworld.domain.event.HelloworldCreatedEvent;

import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

public class HelloworldTest {

	private static final String HELLOWORD_NAME_TO_BE_GREETED = "World";
	private static final String HELLOWORD_ID = "hw1";
	
	private FixtureConfiguration<Helloworld> fixture;

	@Before
	public void setUp() {
		fixture = Fixtures.newGivenWhenThenFixture(Helloworld.class);
	}

	@Test
	public void testCreateToDoItem() {
		fixture.given()
				.when(new CreateHelloworldCommand(HELLOWORD_ID,
						HELLOWORD_NAME_TO_BE_GREETED))
				.expectEvents(
						new HelloworldCreatedEvent(HELLOWORD_ID,
								HELLOWORD_NAME_TO_BE_GREETED));
	}

}
