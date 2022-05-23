package org.datavaultplatform.broker.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionManager;

@Configuration
//Scan for the all the DAO classes which are spring components
@ConditionalOnExpression("${broker.database.enabled:true}")
@ComponentScan("org.datavaultplatform.common.model.dao")
public class DatabaseConfig {

  @Bean
  public LocalSessionFactoryBean sessionFactory(DataSource datasource, JpaProperties jpaProps) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

    sessionFactory.setDataSource(datasource);

    // We have to tell hibernate about the Entity classes
    sessionFactory.setPackagesToScan(
        "org.datavaultplatform.common.event",
        "org.datavaultplatform.common.model"
    );
    sessionFactory.setHibernateProperties(getProperties(jpaProps));
    return sessionFactory;
  }

  private Properties getProperties(JpaProperties jpaProps) {
    final Properties props = new Properties();
    props.putAll(jpaProps.getProperties());
    return props;
  }

  @Bean
  TransactionManager transactionManager(LocalSessionFactoryBean sessionFactoryBean){
    HibernateTransactionManager result = new HibernateTransactionManager();
    result.setSessionFactory(sessionFactoryBean.getObject());
    return result;
  }
}
