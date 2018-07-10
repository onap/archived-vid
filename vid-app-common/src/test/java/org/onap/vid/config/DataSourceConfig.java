package org.onap.vid.config;


import org.hibernate.SessionFactory;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.service.DataAccessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Bean
    @Autowired
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        //I used this class org.openecomp.portalsdk.core.conf.HibernateConfiguration to learn how to config the session factory
        // and use the following url for actual h2 properties
        //https://github.com/levi-putna/Hibernate-H2-Example/blob/master/hibernate-h2-example/src/hibernate.cfg.xml
        Properties properties = getH2Properties();

        properties.put("hibernate.default_schema", "PUBLIC");
        properties.put("connection.pool_size", 10);
        properties.put("cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
        properties.put("hibernate.show_sql", false);
        properties.put("hbm2ddl.auto", "create");
        properties.put("hibernate.hbm2ddl.auto", "create");

        sessionFactory.setHibernateProperties(properties);
        sessionFactory.setPackagesToScan("org.onap");
        return sessionFactory;
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    public Properties getH2Properties() {
        Properties properties = new Properties();
        properties.put("dialect", "org.hibernate.dialect.H2Dialect");
        return properties;
    }

    public Properties getSqliteProperties() {
        Properties properties = new Properties();
        properties.put("connection.driver_class", "org.sqlite.JDBC");
        properties.put("connection.url", "jdbc:sqlite:memory:myDb");
        properties.put("connection.username", "sa");
        properties.put("connection.password", "sa");
        properties.put("dialect", "com.enigmabridge.hibernate.dialect.SQLiteDialect");
        return properties;
    }

    @Bean
    public DataAccessService dataAccessService() {
        return new DataAccessServiceImpl();
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
