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
package org.springframework.data.jpa.bugs.datajpa105;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.bugs.domain.Person;
import org.springframework.data.jpa.bugs.domain.Person_;
import org.springframework.data.jpa.bugs.domain.User;
import org.springframework.data.jpa.domain.Specification;
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
public class DATAJPA105Tests {

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

	@Autowired private PersonRepository personRepo;

	@SuppressWarnings("unused")
	@Test
	public void testPagedSpecificationProjection() {
		Specification<Person> spec = new Specification<Person>() {
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (!Long.class.equals(query.getResultType())) {
					root.fetch(Person_.addresses);
				}
				return cb.conjunction();
			}
		};
		Pageable pageable = new PageRequest(0, 1);
		Page<Person> page = personRepo.findAll(spec, pageable);
	}

}
