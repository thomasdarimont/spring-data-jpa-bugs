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
package org.springframework.data.jpa.bugs.datajpaCUSTOMREPO;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.bugs.datajpaCUSTOMREPO.DATAJPACustomRepoTests.Config;
import org.springframework.data.jpa.bugs.domain.User;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@Transactional
public class DATAJPACustomRepoTests {

	// @Configuration
	@EnableTransactionManagement
	@EnableJpaRepositories
	static class Config {

		@Bean
		public DataSource dataSource() {
			return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		}

		@Bean
		public UserRepositoryCustom userRepositoryImpl(DataSource dataSource) {
			UserRepositoryImplJdbc repo = new UserRepositoryImplJdbc();
			repo.setDataSource(dataSource);
			return repo;
		}

		@Bean
		public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
			return new JpaTransactionManager(emf);
		}

		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
			LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
			factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
			factoryBean.setPackagesToScan(User.class.getPackage().getName());
			factoryBean.setDataSource(dataSource());
			return factoryBean;
		}

		@Bean
		public JpaVendorAdapter jpaVendorAdapter() {
			HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
			jpaVendorAdapter.setDatabase(Database.H2);
			jpaVendorAdapter.setShowSql(true);
			jpaVendorAdapter.setGenerateDdl(true);
			return jpaVendorAdapter;
		}
	}

	@Autowired UserRepository repository;

	/**
	 * @see DATAJPA-XXX
	 */
	@Test
	public void foo() {

		User user = new User("Oliver", "Gierke", "gierke@synyx.de");
		user.setAge(28);
		repository.save(user);

		repository.myCustomBatchOperation();

		repository.findAll();
	}
}
