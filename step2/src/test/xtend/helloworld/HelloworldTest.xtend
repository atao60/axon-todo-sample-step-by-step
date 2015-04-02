package helloworld

import org.axonframework.test.FixtureConfiguration
import org.axonframework.test.Fixtures
import org.junit.Before
import org.junit.Test

import helloworld.domain.Helloworld
import helloworld.domain.command.CreateHelloworldCommand
import helloworld.domain.event.HelloworldCreatedEvent

class HelloworldTest {

    static val HELLOWORD_NAME_TO_BE_GREETED = "World"
    static val HELLOWORD_ID = "hw1"
    
    FixtureConfiguration<Helloworld> fixture

    @Before
    def void setUp() {
        fixture = Fixtures.newGivenWhenThenFixture(Helloworld)
    }

    @Test
    def void testCreateToDoItem() {
        fixture.given
                .when(new CreateHelloworldCommand(HELLOWORD_ID,
                        HELLOWORD_NAME_TO_BE_GREETED))
                .expectEvents(
                        new HelloworldCreatedEvent(HELLOWORD_ID,
                                HELLOWORD_NAME_TO_BE_GREETED))
        
    }

    
}