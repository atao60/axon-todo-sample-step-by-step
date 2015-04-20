package helloworld.config

import java.sql.Driver
import java.util.HashMap
import org.hibernate.cfg.ImprovedNamingStrategy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.TransactionManagementConfigurer
import org.springframework.util.ClassUtils

import static helloworld.config.DataSourceLabel.*
import static java.lang.String.format
import static org.hibernate.cfg.AvailableSettings.*
import static org.hibernate.jpa.AvailableSettings.NAMING_STRATEGY
import org.axonframework.common.Assert

@Configuration
@EnableTransactionManagement
@PropertySource(#["classpath:META-INF/persistence.properties"])
class PersistenceConfig implements TransactionManagementConfigurer {
    
    static val PACKAGES_TO_SCAN = #["helloworld"]
    static val PERSISTENCE_UNIT_NAME = "tutorial"
    static val LOADED_JDBC_DRIVER = "Loaded JDBC driver: {}"
    static val DRIVER_CLASS_NAME_MUST_BE_PROVIDED = "A driver class name must be provided."
    static val COULD_NOT_LOAD_JDBC_DRIVER_CLASS = "Could not load JDBC driver class [%s]"

    protected val logger = LoggerFactory.getLogger(getClass)

    @Autowired Environment env

    def private getProperty(DataSourceLabel label) {
        return env.getProperty(label.label) 
    }
    
    def private <T> T getProperty(String label, Class<T> type) {
        return env.getProperty(label, type)
    }
    
    def private <T> T getProperty(String label, Class<T> type, T defaultValue) {
        return env.getProperty(label, type, defaultValue)
    }
    @Bean
    def dataSource() {
        extension val source = new SimpleDriverDataSource
        
        driverClass = getDriverType(getProperty(driverClassName))
        url = getProperty(url)
        username = getProperty(username)
        password = getProperty(password)
        
        return source;
    }
    
    def private getDriverType(String typeName) {
        Assert.notNull(typeName, DRIVER_CLASS_NAME_MUST_BE_PROVIDED)
        val driverClassNameToUse = typeName.trim
        try {
            val type = Class.forName(driverClassNameToUse, true, ClassUtils.defaultClassLoader)
            logger.info(LOADED_JDBC_DRIVER, type)
            if (! Driver.isAssignableFrom(type)) {
                throw new IllegalStateException(format(COULD_NOT_LOAD_JDBC_DRIVER_CLASS, driverClassNameToUse))
            }
            val driverType = type as Class<Driver>
            return driverType;
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException(format(COULD_NOT_LOAD_JDBC_DRIVER_CLASS, driverClassNameToUse), ex)
        }
    }
    
    @Bean(name="transactionManager")
    override annotationDrivenTransactionManager() {
        new JpaTransactionManager(entityManagerFactory)
    }
/*
    @Bean
    def transactionTemplate() {
        val transactionTemplate = new TransactionTemplate
        transactionTemplate.transactionManager = annotationDrivenTransactionManager
        return transactionTemplate
    }    
*/
    @Bean
    def entityManagerFactory() {
        extension val em = new LocalContainerEntityManagerFactoryBean
        dataSource = dataSource
        persistenceUnitName = PERSISTENCE_UNIT_NAME
        packagesToScan = PACKAGES_TO_SCAN
        jpaVendorAdapter = jpaVendorAdaper
        jpaPropertyMap = additionalProperties
        afterPropertiesSet
        return em.object
    }

    @Bean
    def jpaVendorAdaper() {
        extension val vendorAdapter = new HibernateJpaVendorAdapter
        database = getProperty(DIALECT, Database)
        showSql = getProperty(SHOW_SQL, Boolean, false)
        generateDdl = getProperty("jpa.generateDdl", Boolean, false)
        return vendorAdapter;
    }

    def private additionalProperties() {
        val properties = new HashMap<String, Object>
        properties.put("hibernate.validator.apply_to_ddl", getProperty("hibernate.validator.apply_to_ddl", Boolean, false))
        properties.put("hibernate.validator.autoregister_listeners", getProperty("hibernate.validator.autoregister_listeners", Boolean, false))
        properties.put(NAMING_STRATEGY, ImprovedNamingStrategy.name)
        properties.put(HBM2DDL_AUTO, getProperty(HBM2DDL_AUTO, String, ""))
        properties.put(GENERATE_STATISTICS, getProperty(GENERATE_STATISTICS, Boolean, false))
        return properties
    }

     
}