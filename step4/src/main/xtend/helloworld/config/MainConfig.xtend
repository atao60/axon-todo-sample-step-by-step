package helloworld.config

import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment

@Configuration
@ComponentScan(#["helloworld"/*, "org.axonframework.eventstore.jpa"*/])
//@Import(value = #[PersistenceConfig, CqrsConfig])
class MainConfig {
 
 
    val static LOOKING_FOR_SPRING_PROFILES = "Looking for Spring profiles..."
    val static RUNNING_WITH_DEFAULT_CONFIGURATION = 
            "No Spring profile configured, running with default configuration."
    val static DETECTED_SPRING_PROFILE = "Detected Spring profile: {}"

    val static logger = LoggerFactory.getLogger(MainConfig)

    @Autowired var Environment env

    /**
     * Application custom initialization code.
     * <p/>
     * Spring profiles can be configured with a system property
     * -Dspring.profiles.active=javaee
     * <p/>
     */
    @PostConstruct
    def initApp() {
        logger.debug(LOOKING_FOR_SPRING_PROFILES)
        if (env.activeProfiles.length == 0) {
            logger.info(RUNNING_WITH_DEFAULT_CONFIGURATION)
            return
        }

        for (profile : env.activeProfiles) {
            logger.info(DETECTED_SPRING_PROFILE, profile)
        }
    }
    
}