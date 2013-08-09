/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.bugs.datajpa354;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.bugs.domain.User;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class DATAJPA354Tests {

	@Configuration
	@EnableTransactionManagement
	@EnableJpaRepositories
	static class Config {

		@Bean
		public DataSource dataSourceSpied() {
			return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		}

		@Bean
		public DataSource dataSource() {
			Log4jdbcProxyDataSource ds = new Log4jdbcProxyDataSource(dataSourceSpied());
			Log4JdbcCustomFormatter cf = new Log4JdbcCustomFormatter();
			cf.setLoggingType(LoggingType.MULTI_LINE);
			cf.setMargin(19);
			cf.setSqlPrefix("SQL::");
			ds.setLogFormatter(cf);
			return ds;
		}

		@Bean
		public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
			return new JpaTransactionManager(emf);
		}

		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
			HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
			// jpaVendorAdapter.setDatabasePlatform(databaseProperties.getHibernateDialect());
			jpaVendorAdapter.setDatabase(Database.H2);
			jpaVendorAdapter.setShowSql(true);
			jpaVendorAdapter.setGenerateDdl(true);

			Map<String, String> jpaPropertiesMap = new HashMap<String, String>();
			// jpaPropertiesMap.put("hibernate.dialect", databaseProperties.getHibernateDialect());
			jpaPropertiesMap.put("hibernate.show_sql", "true");
			jpaPropertiesMap.put("hibernate.format_sql", "true");
			// jpaPropertiesMap.put("hibernate.hbm2ddl.auto", databaseProperties.getHibernateHbm2ddlAuto());
			jpaPropertiesMap.put("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
			jpaPropertiesMap.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
			jpaPropertiesMap.put("hibernate.c3p0.min_size", "5");
			jpaPropertiesMap.put("hibernate.c3p0.max_size", "20");
			jpaPropertiesMap.put("hibernate.c3p0.timeout", "1000");
			jpaPropertiesMap.put("hibernate.c3p0.max_statements", "50");

			LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
			factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
			factoryBean.setPackagesToScan(User.class.getPackage().getName());
			factoryBean.setJpaPropertyMap(jpaPropertiesMap);
			factoryBean.setDataSource(dataSource());
			return factoryBean;
		}
	}

	User firstUser, secondUser, thirdUser, fourthUser, fifthUser;
	@Autowired UserRepository repository;

	@Before
	public void setUp() throws Exception {

		firstUser = new User("Oliver", "Gierke", "gierke@synyx.de");
		firstUser.setAge(28);
		secondUser = new User("Joachim", "Arrasz", "arrasz@synyx.de");
		secondUser.setAge(35);
		Thread.sleep(10);
		thirdUser = new User("Dave", "Matthews", "no@email.com");
		thirdUser.setAge(43);
		fourthUser = new User("kevin", "raymond", "no@gmail.com");
		fourthUser.setAge(31);
		fifthUser = new User("David", "k", "no@imail.com");
		fifthUser.setAge(28);
	}

	protected void flushTestUsers() {

		firstUser = repository.save(firstUser);
		secondUser = repository.save(secondUser);
		thirdUser = repository.save(thirdUser);
		fourthUser = repository.save(fourthUser);
		fifthUser = repository.saveAndFlush(fifthUser);

		repository.flush();

		assertThat(secondUser.getId(), is(notNullValue()));
		assertThat(thirdUser.getId(), is(notNullValue()));
		assertThat(fourthUser.getId(), is(notNullValue()));

		assertThat(repository.exists(secondUser.getId()), is(true));
		assertThat(repository.exists(thirdUser.getId()), is(true));
		assertThat(repository.exists(fourthUser.getId()), is(true));
	}

	/**
	 * @see DATAJPA-354
	 */
	@Test
	public void executesManualQueryWithNamedLikeExpressionUsesCorrectlyQuotedStrings() {

		flushTestUsers();

		List<User> result = repository.findByFirstnameAndLastnameLikeWithConcat("Da", "tthew");

		assertThat(result.size(), is(1));
		assertThat(result, hasItem(thirdUser));
	}

	/**
	 * @see DATAJPA-354
	 */
	@Test
	public void executesManualQueryWithNamedLikeLowerExpressionUsesCorrectlyQuotedStrings() {

		flushTestUsers();

		List<User> result = repository.findByFirstnameAndLastNameLikeLower("Da", "TThews");

		assertThat(result.size(), is(1));
		assertThat(result, hasItem(thirdUser));
	}

	/**
	 * @see DATAJPA-354
	 */
	@Test
	public void foo() {

		flushTestUsers();

		List<User> result = repository.findByFirstname("Da", new PageRequest(0, 2, new Sort(Sort.Direction.DESC,
				"firstname")));

		assertThat(result.size(), is(2));
		assertThat(result.get(0).getFirstname(), is("Dave"));
		assertThat(result.get(1).getFirstname(), is("David"));
	}
}
