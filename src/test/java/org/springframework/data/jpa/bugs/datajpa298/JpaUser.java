package org.springframework.data.jpa.bugs.datajpa298;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class JpaUser implements User {

	// Y U NO GET INJECTED?
	@Transient//
	// @Autowired //
	@Inject//
	private Environment environment;

	@Id private String username;

	public JpaUser(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	// Y U NO TRANSACTIONAL?
	@Transactional
	public Environment getEnvironment() {
		return environment;
	}

}
